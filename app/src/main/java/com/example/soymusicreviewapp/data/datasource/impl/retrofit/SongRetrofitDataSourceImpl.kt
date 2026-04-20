package com.example.soymusicreviewapp.data.datasource.impl.retrofit

import com.example.soymusicreviewapp.data.datasource.remotedatasource.SongRemoteDataSource
import com.example.soymusicreviewapp.data.datasource.services.SongRetrofitService
import com.example.soymusicreviewapp.data.dtos.SongDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRetrofitDataSourceImpl @Inject constructor(
    private val service: SongRetrofitService
) : SongRemoteDataSource {

    override suspend fun getAllSongs(): List<SongDto> {
        return service.getAllSongs()
    }

    override suspend fun getSongById(songId: String): SongDto {
        val idInt = songId.toIntOrNull()
        if (idInt != null) {
            return service.getSongById(idInt)
        } else {
            throw Exception("Identificador invalido")
        }
    }

    override fun listenAllSongs(): Flow<List<SongDto>> {
        TODO("Not yet implemented")
    }
}