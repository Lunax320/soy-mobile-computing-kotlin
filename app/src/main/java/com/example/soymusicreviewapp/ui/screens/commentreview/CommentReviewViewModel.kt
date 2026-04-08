package com.example.soymusicreviewapp.ui.screens.commentreview

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.local.LocalReviewProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommentReviewViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CommentReviewState())
    val uiState: StateFlow<CommentReviewState> = _uiState.asStateFlow()

    fun onCommentTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { currentState ->
                currentState.copy(commentText = newText)
            }
        }
    }

    // Busca la reseña original para sacar el nombre de la canción
    fun getParentReview(reviewId: String): Review? {
        return LocalReviewProvider.reviews.find { it.usernameId == reviewId }
    }

    // Simula la búsqueda de los comentarios de esa reseña
    fun getCommentsForReview(reviewId: String): List<Review> {
        // Por ahora devolvemos una lista vacía para mostrar el diseño de tu imagen.
        // Cuando conectes el backend, aquí filtrarás los comentarios reales.
        return emptyList()
    }
}