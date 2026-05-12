package com.example.soymusicreviewapp.repository

import com.example.soymusicreviewapp.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.datasource.remotedatasource.AuthRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.example.soymusicreviewapp.data.repository.AuthRepository
import com.example.soymusicreviewapp.data.repository.UserRepository
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class UserRepositoryIntegrationTest {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var userRepository: UserRepository

    private fun generateUser(i: Int): UserDto = UserDto(
        id = "user_$i",
        username = "username_$i",
        name = "Name $i",
        profileImage = "https://picsum.photos/200/200?random=$i",
        followersCount = i * 10,
        followingCount = i * 5,
        followed = false
    )


    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8085)
            //db.useEmulator("10.0.2.2", 8080)
        } catch (_: Exception) {
        }

        userRepository = UserRepository(UserFirestoreDataSourceImpl(db), AuthRemoteDataSource(auth))
        //userRepository = UserRepository(UserFirestoreDataSourceImpl(db), AuthRepository(AuthRemoteDataSource(auth)))




        val users = db.collection("users").get().await()
        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (f in followers) f.reference.delete().await()
            val following = userDoc.reference.collection("following").get().await()
            for (f in following) f.reference.delete().await()
            userDoc.reference.delete().await()
        }

        val reviews = db.collection("reviews").get().await()
        for (reviewDoc in reviews) {
            val likes = reviewDoc.reference.collection("likes").get().await()
            for (likeDoc in likes) {
                likeDoc.reference.delete().await()
            }
            reviewDoc.reference.delete().await()
        }

        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i + 1)
            batch.set(db.collection("users").document(user.id), user)
        }
        batch.commit().await()
    }

// si pasa
    @Test
    fun getUserById_validId_correctUser() = runTest {
        //arrange
        val id = "user_9"
        val expectedName = "Name 9"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.name).isEqualTo(expectedName)

    }

    // si pasa
    @Test
    fun getUserById_invalidId_returnFailure() = runTest {
        //arrange
        val id = "user_999"
        //act
        val result = userRepository.getUserById(id)
        //assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")

    }

    // si pasa
    @Test
    fun registerUser_validData_success() = runTest {
        //arrange
        val userId = "new_test_user"
        val username = "tester123"
        //act
        val result = userRepository.registerUser(username, "Test Name", userId)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        val doc = db.collection("users").document(userId).get().await()
        Truth.assertThat(doc.exists()).isTrue()
        Truth.assertThat(doc.getString("username")).isEqualTo(username)
    }

    // si pasa
    @Test
    fun followUser_validIds_createsRelationship() = runTest {
        //arrange
        val currentId = "user_1"
        val targetId = "user_2"
        //act
        val result = userRepository.followOrUnfollowUser(currentId, targetId)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        val followingDoc = db.collection("users").document(currentId)
            .collection("following").document(targetId).get().await()
        Truth.assertThat(followingDoc.exists()).isTrue()
    }

    // si pasa
    @Test
    fun unfollowUser_alreadyFollowing_removesRelationship() = runTest {
        //arrange
        val currentId = "user_1"
        val targetId = "user_2"
        userRepository.followOrUnfollowUser(currentId, targetId)

        // Act
        val result = userRepository.followOrUnfollowUser(currentId, targetId)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        val followingDoc = db.collection("users").document(currentId)
            .collection("following").document(targetId).get().await()
        Truth.assertThat(followingDoc.exists()).isFalse()
    }

    // si pasa
    @Test
    fun updateUserInfo_validData_updatesFields() = runTest {
        //arrange
        val userId = "user_3"
        val newName = "Updated Name"
        val newUsername = "new_nick"
        //act
        val result = userRepository.updateUserInfo(userId, newName, newUsername)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        val doc = db.collection("users").document(userId).get().await()
        Truth.assertThat(doc.getString("name")).isEqualTo(newName)
        Truth.assertThat(doc.getString("username")).isEqualTo(newUsername)
    }
// si pasa
    @Test
    fun getFollowingIds_returnsCorrectList() = runTest {
        //arrange
        val currentId = "user_4"
        val targetId = "user_5"
        userRepository.followOrUnfollowUser(currentId, targetId)
    //act
        val ids = userRepository.getFollowingIds(currentId)
    //assert
        Truth.assertThat(ids).contains(targetId)
    }

    // si pasa
    @Test
    fun getFollowing_returnsUserList() = runTest {
        //arrange
        val currentId = "user_6"
        val targetId = "user_7"
        userRepository.followOrUnfollowUser(currentId, targetId)
        //act
        val result = userRepository.getFollowing(currentId)
        //assert
        Truth.assertThat(result.isSuccess).isTrue()
        val list = result.getOrNull()
        Truth.assertThat(list).isNotNull()
        Truth.assertThat(list?.any { it.id == targetId }).isTrue()
    }


    @After
    fun tearDown() = runTest {
        val users = db.collection("users").get().await()

        for (userDoc in users) {
            val followers = userDoc.reference.collection("followers").get().await()
            for (followerDoc in followers) {
                followerDoc.reference.delete().await()
            }

            val following = userDoc.reference.collection("following").get().await()
            for (f in following) {
                f.reference.delete().await()
            }
        }

        users.documents.forEach { doc ->
            db.collection("users").document(doc.id).delete().await()
        }
    }

}
