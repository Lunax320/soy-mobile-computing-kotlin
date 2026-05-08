package com.example.soymusicreviewapp.data.datasource.impl.firestore

import com.example.soymusicreviewapp.data.datasource.remotedatasource.SongRemoteDataSource
import com.example.soymusicreviewapp.data.dtos.SongDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
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

    override suspend fun addFavorite(userId: String, songId: String) {
        val favoriteRef = db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(songId)

        favoriteRef.set(mapOf("timestamp" to FieldValue.serverTimestamp())).await()
    }

    override suspend fun removeFavorite(userId: String, songId: String) {
        val favoriteRef = db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(songId)

        favoriteRef.delete().await()
    }

    override suspend fun getFavoriteSongs(userId: String): List<SongDto> {
        val snapshot = db.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .await()

        val songIds = snapshot.documents.map { it.id }

        if (songIds.isEmpty()) return emptyList()

        val songsSnapshot = db.collection("songs")
            .whereIn("__name__", songIds)
            .get()
            .await()

        return songsSnapshot.documents.mapNotNull { doc ->
            doc.toObject(SongDto::class.java)?.copy(id = doc.id)
        }
    }

    override fun listenFavoriteSongs(userId: String): Flow<List<SongDto>> = callbackFlow {
        val listener = db.collection("users")
            .document(userId)
            .collection("favorites")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    launch {
                        val songIds = snapshot.documents.map { it.id }

                        if (songIds.isEmpty()) {
                            trySend(emptyList())
                            return@launch
                        }

                        val songsSnapshot = db.collection("songs")
                            .whereIn("__name__", songIds)
                            .get()
                            .await()

                        val songs = songsSnapshot.documents.mapNotNull { doc ->
                            doc.toObject(SongDto::class.java)?.copy(id = doc.id)
                        }
                        trySend(songs)
                    }
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun isFavorite(userId: String, songId: String): Boolean {
        val doc = db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(songId)
            .get()
            .await()
        return doc.exists()
    }
}
