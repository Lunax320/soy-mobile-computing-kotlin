package com.example.soymusicreviewapp.data.local

import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Notification

object LocalNotificationProvider {
    val notifications = listOf(
        Notification(
            profileImageId = R.drawable.img_avatar_one,
            userName = "sarah_music",
            actionText = "started following you",
            time = "745 days ago",
            rightIconId = R.drawable.ic_profile,
        ),
        Notification(
            profileImageId = R.drawable.img_avatar_two,
            userName = "carlos_beats",
            actionText = "liked your review of \"Midnight City\"",
            time = "745 days ago",
            rightIconId = R.drawable.ic_heart,
        ),
        Notification(
            profileImageId = R.drawable.img_avatar_three,
            userName = "ana_songs",
            actionText = "commented on your review of \"The Mother We Share\"",
            time = "745 days ago",
            rightIconId = R.drawable.ic_profile,
        ),
        Notification(
            profileImageId = R.drawable.img_avatar_four,
            userName = "david_tunes",
            actionText = "liked your review of \"Genesis\"",
            time = "746 days ago",
            rightIconId = R.drawable.ic_heart,
        )
    )
}