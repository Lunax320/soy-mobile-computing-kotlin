package com.example.soymusicreviewapp.data.datasource.remotedatasource

import com.example.soymusicreviewapp.data.dtos.SongDto

interface SongRemoteDataSource {
    suspend fun getAllSongs(): List<SongDto>
    suspend fun getSongById(songId: String): SongDto
}