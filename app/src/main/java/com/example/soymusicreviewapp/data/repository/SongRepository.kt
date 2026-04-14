package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.datasource.impl.firestore.SongFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toSong
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val remoteDataSource: SongFirestoreDataSourceImpl
) {
    suspend fun getSongs(): Result<List<Song>> {
        return try {
            val songsDto = remoteDataSource.getAllSongs()
            val songs = songsDto.map { it.toSong() }
            Result.success(songs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSongById(songId: String): Result<Song> {
        return try {
            val songDto = remoteDataSource.getSongById(songId)
            Result.success(songDto.toSong())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}