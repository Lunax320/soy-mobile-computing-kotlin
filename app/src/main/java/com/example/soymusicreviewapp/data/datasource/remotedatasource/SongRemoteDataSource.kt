package com.example.soymusicreviewapp.data.datasource.remotedatasource

import com.example.soymusicreviewapp.data.dtos.SongDto
import kotlinx.coroutines.flow.Flow

interface SongRemoteDataSource {
    suspend fun getAllSongs(): List<SongDto>
    suspend fun getSongById(songId: String): SongDto
    fun listenAllSongs(): Flow<List<SongDto>>
}
