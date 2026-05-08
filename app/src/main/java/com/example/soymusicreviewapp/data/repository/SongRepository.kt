package com.example.soymusicreviewapp.data.repository

import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.datasource.impl.firestore.SongFirestoreDataSourceImpl
import com.example.soymusicreviewapp.data.dtos.toSong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    fun getSongsLive(): Flow<List<Song>> {
        return remoteDataSource.listenAllSongs().map { songsDto ->
            songsDto.map { it.toSong() }
        }
    }

    suspend fun addFavorite(userId: String, songId: String): Result<Unit> {
        return try {
            remoteDataSource.addFavorite(userId, songId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavorite(userId: String, songId: String): Result<Unit> {
        return try {
            remoteDataSource.removeFavorite(userId, songId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteSongs(userId: String): Result<List<Song>> {
        return try {
            val songsDto = remoteDataSource.getFavoriteSongs(userId)
            val songs = songsDto.map { it.toSong() }
            Result.success(songs)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getFavoriteSongsLive(userId: String): Flow<List<Song>> {
        return remoteDataSource.listenFavoriteSongs(userId).map { songsDto ->
            songsDto.map { it.toSong() }
        }
    }

    suspend fun isFavorite(userId: String, songId: String): Result<Boolean> {
        return try {
            val isFav = remoteDataSource.isFavorite(userId, songId)
            Result.success(isFav)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
