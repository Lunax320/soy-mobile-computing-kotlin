package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.SongRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.SongDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SongFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : SongRemoteDataSource {

    override suspend fun getAllSongs(): List<SongDto> {
        val snapshot = db.collection("songs").get().await()

        return snapshot.documents.mapNotNull { doc ->
            val song = doc.toObject(SongDto::class.java)
            if (song != null) {
                song.copy(id = doc.id)
            } else {
                null
            }
        }
    }

    override suspend fun getSongById(songId: String): SongDto {
        val docRef = db.collection("songs").document(songId)
        val snapshot = docRef.get().await()

        val song = snapshot.toObject(SongDto::class.java)
        if (song != null) {
            return song.copy(id = snapshot.id)
        } else {
            throw Exception("No se encontro la cancion")
        }
    }
}