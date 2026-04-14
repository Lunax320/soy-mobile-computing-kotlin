package com.example.soymusicreviewapp.ui.screens.createreview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.inject.Inject
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState.asStateFlow()

    init {
        val songId = savedStateHandle.get<String>("songId") ?: "1"
        loadSongData(songId)
    }

    private fun loadSongData(songId: String) {
        viewModelScope.launch {
            val fetchedSong = withContext(Dispatchers.IO) {
                try {
                    val response = URL("http://10.0.2.2:3000/songs/$songId").readText()
                    val obj = JSONObject(response)

                    var imgUrl = if (obj.isNull("songImage")) "" else obj.getString("songImage")
                    if (imgUrl.contains("example.com") || imgUrl.isEmpty()) {
                        imgUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500&q=80"
                    }

                    Song(
                        songId = obj.getInt("id").toString(),
                        name = obj.getString("name"),
                        artist = obj.getString("artist"),
                        genre = obj.optString("genre", "Desconocido"),
                        duration = obj.optString("duration", "0:00"),
                        songImage = imgUrl
                    )
                } catch (e: Exception) {
                    Log.e("API_TRACKER", "Crear Reseña: Fallo al descargar canción - ${e.message}")
                    LocalSongsProvider.songs.find { it.songId == songId } ?: LocalSongsProvider.songs.first()
                }
            }
            _uiState.update { it.copy(song = fetchedSong) }
        }
    }

    fun onReviewTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { currentState ->
                currentState.copy(reviewText = newText)
            }
        }
    }

    fun onRatingChange(newRating: Int) {
        _uiState.update { currentState ->
            currentState.copy(rating = newRating)
        }
    }

    fun createReview(songId: String) {
        Log.d("API_TRACKER", "ViewModel: Iniciando publicación para la canción: $songId")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""

            val result = reviewRepository.createReview(
                userId = currentUserId,
                songId = songId,
                reviewText = _uiState.value.reviewText,
                rating = _uiState.value.rating,
                date = currentDate
            )

            if (result.isSuccess) {
                Log.d("API_TRACKER", "ViewModel: Publicación exitosa.")
                _uiState.update { it.copy(navigateBack = true) }
            } else {
                Log.e("API_TRACKER", "ViewModel: Fallo en la publicación.")
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al publicar") }
            }
        }
    }
}