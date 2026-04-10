package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.datasource.impl.SongRetrofitDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toSong
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val remoteDataSource: SongRetrofitDataSourceImpl
) {
    suspend fun getSongs(): Result<List<Song>> {
        return try {
            val dtoList = remoteDataSource.getAllSongs()
            val songList = dtoList.map { it.toSong() }
            Result.success(songList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSongById(songId: String): Result<Song> {
        return try {
            val dto = remoteDataSource.getSongById(songId)
            Result.success(dto.toSong())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}