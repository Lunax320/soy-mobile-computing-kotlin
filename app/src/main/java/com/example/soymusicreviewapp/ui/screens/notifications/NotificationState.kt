package com.example.soymusicreviewapp.ui.screens.notifications

import com.example.soymusicreviewapp.data.Notification

data class NotificationState(
    val notifications: List<Notification> = emptyList()
)