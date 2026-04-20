package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.SongRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.SongDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SongFirestoreDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : SongRemoteDataSource {

    override suspend fun getAllSongs(): List<SongDto> {
        val snapshot = db.collection("songs").get().await()

        return snapshot.documents.mapNotNull { doc ->
            val song = doc.toObject(SongDto::class.java)
            song?.copy(id = doc.id)
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

    override fun listenAllSongs(): Flow<List<SongDto>> = callbackFlow {
        val listener = db.collection("songs").addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val songs = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(SongDto::class.java)?.copy(id = doc.id)
                }
                trySend(songs).isSuccess
            }
        }
        awaitClose { listener.remove() }
    }
}
