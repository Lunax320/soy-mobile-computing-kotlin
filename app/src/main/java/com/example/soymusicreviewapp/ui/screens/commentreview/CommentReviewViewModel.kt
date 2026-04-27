package com.example.soymusicreviewapp.ui.screens.commentreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.Review
import com.example.soymusicreviewapp.data.repository.ReviewRepository
import com.example.soymusicreviewapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CommentReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommentReviewState())
    val uiState: StateFlow<CommentReviewState> = _uiState.asStateFlow()

    private val parentReviewId: String = savedStateHandle.get<String>("reviewId") ?: ""

    init {
        loadData()
    }
    fun getParentReview(): Review? = _uiState.value.parentReview
    private fun loadData() {
        viewModelScope.launch {
            val currentUserId = authRepository.currentUser?.uid ?: ""
            _uiState.update { it.copy(currentUserId = currentUserId) }

            val parentResult = reviewRepository.getReviewById(parentReviewId)
            if (parentResult.isSuccess) {
                _uiState.update { it.copy(parentReview = parentResult.getOrNull()) }
            } else {
                _uiState.update { it.copy(errorMessage = "No se pudo cargar la review") }
            }

            reviewRepository.getCommentsForReviewLive(parentReviewId)
                .catch { e ->
                    _uiState.update {
                        it.copy(errorMessage = e.message ?: "Error al cargar comentarios")
                    }
                }
                .collect { comments ->
                    _uiState.update {
                        it.copy(comments = comments, isLoading = false)
                    }
                }
        }
    }

    fun onCommentTextChange(newText: String) {
        if (newText.length <= 500) {
            _uiState.update { it.copy(commentText = newText) }
        }
    }

    fun sendComment() {
        val commentText = _uiState.value.commentText.trim()

        if (commentText.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "No puedes enviar un comentario vacio") }
            return
        }

        if (commentText.length > 500) {
            _uiState.update { it.copy(errorMessage = "El comentario no puede tener más de 500 caracteres") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, errorMessage = null) }

            val currentDate =
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val currentUserId = authRepository.currentUser?.uid ?: ""

            val result = reviewRepository.createComment(
                parentReviewId = parentReviewId,
                userId = currentUserId,
                commentText = commentText,
                date = currentDate
            )

            if (result.isSuccess) {
                // Limpiar el campo de texto después de enviar
                _uiState.update {
                    it.copy(commentText = "", isSending = false)
                }
            } else {
                _uiState.update {
                    it.copy(isSending = false, errorMessage = "Error al enviar comentario")
                }
            }
        }
    }
}