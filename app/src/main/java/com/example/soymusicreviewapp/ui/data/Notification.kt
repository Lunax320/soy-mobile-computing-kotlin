package com.example.soymusicreviewapp.ui.data

import androidx.annotation.DrawableRes

data class Notification(
    @DrawableRes val profileImageId: Int,
    val userName: String,
    val actionText: String,
    val time: String,
    @DrawableRes val rightIconId: Int
)