package com.example.soymusicreviewapp.ui.screens.notifications

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.local.LocalNotificationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(NotificationState())

    val uiState: StateFlow<NotificationState> = _uiState.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val notificationsData = LocalNotificationProvider.notifications
        _uiState.update { currentState ->
            currentState.copy(notifications = notificationsData)
        }
    }
}