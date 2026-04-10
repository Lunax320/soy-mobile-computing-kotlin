package com.example.soymusicreviewapp.data.datasource.impl

import com.example.soymusicreviewapp.data.datasource.SongRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.services.SongRetrofitService
import com.example.soymusicreviewapp.data.dtos.SongDto
import javax.inject.Inject

class SongRetrofitDataSourceImpl @Inject constructor(
    private val service: SongRetrofitService
) : SongRemoteDataSource {

    override suspend fun getAllSongs(): List<SongDto> {
        return service.getAllSongs()
    }

    override suspend fun getSongById(songId: String): SongDto {
        return service.getSongById(songId.toInt())
    }
}