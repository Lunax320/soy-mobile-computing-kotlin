package com.example.soymusicreviewapp.repository

import com.example.soymusicreviewapp.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.datasource.remotedatasource.AuthRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.UserDto
import com.example.soymusicreviewapp.data.repository.UserRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test


class UserRepositoryTest {

    private val mockDataSource = mockk<UserFirestoreDataSourceImpl>()
    //private val authRepository = mockk<authRepository>()
    private val authDataSource = mockk<AuthRemoteDataSource>()

    //private val repository = UserRepository(mockDataSource, authRepository)
    private val repository = UserRepository(mockDataSource, authDataSource)



    // si pasa
    @Test
    fun `al llamar getUserById, si el id es valido retorna un Result con el userInfo`() = runTest  {

        //AAA

        val dto = UserDto(
            id = "123",
            username = "juan",
            name = "Juan",
            profileImage = "https://picsum.photos/200/200?random=1",
            followersCount = 0,
            followingCount = 0,
            followed = false
        )
        //Arrange
        coEvery { authDataSource.currentUser?.uid } returns "1"
        coEvery{ mockDataSource.getUserById("123", "1")} returns dto


        //Act
        val result = repository.getUserById("123")

        ///Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo("123")
        Truth.assertThat(result.getOrNull()?.name).isEqualTo("Juan")

    }

    // si pasa -- MAPEO de que si pasa un valor nulo del datasource a un Result.failure con el mensaje del error que saque
    @Test
    fun `al llamar getUserById, si el id es invalido retorna un Result con failure`() = runTest{

        // AAA
        coEvery { authDataSource.currentUser?.uid } returns "1"
        coEvery { mockDataSource.getUserById("999", "1") } returns null


        //Act
        val result = repository.getUserById("999")

        //Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("User not found")
    }

    // si pasa
    @Test
    fun `al llamar getFollowingIds, retorna la lista de IDs correctamente`() = runTest {
        val listaIds = listOf("id1", "id2")
        coEvery { mockDataSource.getFollowingIds("user1") } returns listaIds

        val result = repository.getFollowingIds("user1")

        Truth.assertThat(result).isEqualTo(listaIds)
    }

    // si pasa -- MAPEADO - mapea una excepcion a una lista vacia
    @Test
    fun `al llamar getFollowingIds, si hay error mapea la excepcion a una lista vacia`() = runTest {
        // Este test demuestra el MAPEO: Exception -> emptyList()
        coEvery { mockDataSource.getFollowingIds("user1") } throws Exception("Firebase Error")

        val result = repository.getFollowingIds("user1")

        Truth.assertThat(result).isEmpty()
    }

 // si pasa
    @Test
    fun `al llamar getFollowers, retorna la lista de usuarios seguidores`() = runTest {
        val mockList = listOf(UserDto("1", "user", "User", null, 0, 0, false))
        coEvery { mockDataSource.getFollowers("123") } returns mockList

        val result = repository.getFollowers("123")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()).hasSize(1)
    }
// si pasa
    @Test
    fun `al llamar followOrUnfollowUser, si el datasource tiene exito retorna Result success`() = runTest {
        coEvery { mockDataSource.followOrUnfollowUser("me", "you") } returns Unit

        val result = repository.followOrUnfollowUser("me", "you")

        Truth.assertThat(result.isSuccess).isTrue()
    }
// si pasa
    @Test
    fun `al llamar updateUserInfo, si el datasource tiene exito retorna Result success`() = runTest {
        coEvery { mockDataSource.updateUserInfo("123", "Nuevo Nombre", "new_nick") } returns Unit

        val result = repository.updateUserInfo("123", "Nuevo Nombre", "new_nick")

        Truth.assertThat(result.isSuccess).isTrue()
    }
    //si pasa - MAPEADO - Mapea el resultado exitoso de la lista del DataSource a un Result.success encapsulado
    @Test
    fun `al llamar getFollowing, si el datasource tiene exito retorna la lista de seguidos`() = runTest {
        val mockList = listOf(UserDto("2", "friend", "Friend", null, 0, 0, false))
        coEvery { mockDataSource.getFollowing("123") } returns mockList

        val result = repository.getFollowing("123")

        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.first()?.username).isEqualTo("friend")
    }

}