package com.example.soymusicreviewapp

import android.util.Log
import com.example.soymusicreviewapp.data.datasource.impl.firestore.ReviewFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.CreateReviewDto
import com.example.soymusicreviewapp.data.dtos.RegisterUserDto
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class FirebaseUserDataSourceTest {
    private val db = Firebase.firestore
    private lateinit var dataSource: UserFirestoreDataSourceImpl
    private lateinit var reviewDataSource: ReviewFirestoreDataSourceImpl
    private fun generateUser(i: Int): UserDto = UserDto(
        id = "user_$i",
        username = "username_$i",
        name = "Name $i",
        profileImage = "https://picsum.photos/200/200?random=$i",
        followersCount = i * 10,
        followingCount = i * 5,
        followed = false
    )

    //ESTRUCTURA TEST: 1.Nombre de lo que pruebo _ 2. Precondiciones _ 3. Lo que quiero que responda
    //replicables: Siempre debe ser el mismo resultado si el programa no ha cambiado
    //autonomas: No deben depender del resultado de otras pruebas
    //Antes y depues de iniciar el test, la base de datos debe quedar exactamente igual

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8085)
            //db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) { }

        dataSource = UserFirestoreDataSourceImpl(db)
        reviewDataSource = ReviewFirestoreDataSourceImpl(db)

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

    @Test
    fun getUserById_validId_correctUser() = runTest {
        //AAA
        //Arrange: Preparar la prueba
        val userId = "user_9"
        val expectedname = "Name 9"

        //Act: Generar sobre lo que realizamos la prueba
        val result = dataSource.getUserById(userId, "")

        //Assert: Verificar
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.name).isEqualTo(expectedname)
        Truth.assertThat(result?.id).isEqualTo(userId)
    }

    @Test
    fun getUserById_invalidId_null() = runTest {
        //AAA
        //Arrange: Preparar la prueba
        val userId = "user_999"
        val expectedname = "Name 9"

        //Act: Generar sobre lo que realizamos la prueba
        val result = dataSource.getUserById(userId, "")

        //Assert: Verificar
        Truth.assertThat(result).isNull()
    }

    @Test
    fun resgisterUser_insertDocument_DocumentExists() = runTest {
        //AAA
        val user = RegisterUserDto(
            username = "username",
            name = "name",
            FCMToken = "token"
        )

        //Arrange: Preparar la prueba
        dataSource.registerUser(user, "999")

        //Assert: Verificar
        val result = dataSource.getUserById("999", "")
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.username).isEqualTo("username")
        Truth.assertThat(result?.id).isEqualTo("999")
    }

    @Test
    fun followOrUnfollowUser_followUser_UserFollowed() = runTest {
        //AAA
        //Arrange: Preparar la prueba
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        //Act: Generar sobre lo que realizamos la prueba
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        //Assert: Verificar
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult?.followed).isTrue()
    }

    @Test
    fun followOrUnfollowUser_followUser_followerCountIncrement() = runTest {
        //AAA
        //Arrange: Preparar la prueba
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        val oldData = dataSource.getUserById(targetUser.id)

        //Act: Generar sobre lo que realizamos la prueba
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        //Assert: Verificar
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult?.followersCount).isGreaterThan(oldData?.followersCount)
    }

    @Test
    fun followOrUnfollowUser_unfollow_followedFalse() = runTest {
        //AAA
        //Arrange: Preparar la prueba
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)

        //Act: Generar sobre lo que realizamos la prueba
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        //Assert: Verificar
        val targetUserResult = dataSource.getUserById(targetUser.id, currentUser.id)
        Truth.assertThat(targetUserResult?.followed).isFalse()
    }

    @Test
    fun followOrUnfollowUser_followTwice_followerCountNoChange() = runTest {
        // Arrange
        val currentUser = generateUser(1)
        val targetUser = generateUser(2)
        val originalFollowersCount = dataSource.getUserById(targetUser.id)?.followersCount ?: 0

        // Act
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)
        dataSource.followOrUnfollowUser(currentUser.id, targetUser.id)

        // Assert
        val finalFollowersCount = dataSource.getUserById(targetUser.id)?.followersCount ?: 0
        Truth.assertThat(finalFollowersCount).isEqualTo(originalFollowersCount)
    }

    @Test
    fun getUserReviews_validUserId_correctReviews() = runTest {
        //AAA
        //Arrange: Preparar la prueba
        val user = generateUser(1)

        val userDto = UserDto(
            id = user.id,
            username = user.username,
            name = user.name,
            profileImage = user.profileImage,
            followersCount = user.followersCount,
            followingCount = user.followingCount,
            followed = false
        )

        val createReviewDto = CreateReviewDto(
            userId = user.id,
            songId = "songId",
            songName = "songName",
            artistName = "artistName",
            reviewText = "reviewText",
            rating = 5,
            date = "date",
            parentId = null,
            user = userDto
        )

        reviewDataSource.createReview(createReviewDto)

        //Act: Generar sobre lo que realizamos la prueba
        val reviews = reviewDataSource.getUserReviews(user.id)

        //Assert: Verificar
        Truth.assertThat(reviews).isNotEmpty()
        Truth.assertThat(reviews.first().reviewText).isEqualTo(createReviewDto.reviewText)
    }

    @Test
    fun getUserReviews_emptyUser_noReviews() = runTest {
        //Arrange: Preparar la prueba
        val user = generateUser(1)

        //Act: Generar sobre lo que realizamos la prueba
        val reviews = reviewDataSource.getUserReviews(user.id, "")

        //Assert: Verificar
        Truth.assertThat(reviews).isEmpty()
    }

    @Test
    fun updateUserInfo_validData_successfullyUpdated() = runTest {
        // Arrange
        val user = generateUser(1)
        val newName = "newName"
        val newUsername = "newUsername"

        // Act
        dataSource.updateUserInfo(user.id, newName, newUsername)

        // Assert
        val updatedUser = dataSource.getUserById(user.id, "")
        Truth.assertThat(updatedUser).isNotNull()
        Truth.assertThat(updatedUser?.name).isEqualTo(newName)
        Truth.assertThat(updatedUser?.username).isEqualTo(newUsername)
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