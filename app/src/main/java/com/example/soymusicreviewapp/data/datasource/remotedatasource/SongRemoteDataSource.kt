package com.example.soymusicreviewapp.data.datasource.remotedatasource

import com.example.soymusicreviewapp.data.dtos.SongDto
import kotlinx.coroutines.flow.Flow

interface SongRemoteDataSource {
    suspend fun getAllSongs(): List<SongDto>
    suspend fun getSongById(songId: String): SongDto
    fun listenAllSongs(): Flow<List<SongDto>>
    suspend fun addFavorite(userId: String, songId: String)
    suspend fun removeFavorite(userId: String, songId: String)
    suspend fun getFavoriteSongs(userId: String): List<SongDto>
    fun listenFavoriteSongs(userId: String): Flow<List<SongDto>>
    suspend fun isFavorite(userId: String, songId: String): Boolean
}
