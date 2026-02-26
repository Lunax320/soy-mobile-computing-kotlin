package com.example.soymusicreviewapp.data.local

import com.example.soymusicreviewapp.R
import com.example.soymusicreviewapp.data.Review

object LocalReviewProvider {
    val reviews = listOf(
        Review(
            userImageId = R.drawable.img_avatar_one,
            userName = "MusicLover",
            date = "2023-08-01",
            songName = "Midnight City",
            artistName = "M83",
            reviewText = "This song is a modern electropop anthem. Powerful beats, vibrant synths, and a futuristic vibe. Perfect to turn up the volume and let go. An explosion of rhythm that defines a whole generation.",
            rating = 5
        ),
        Review(
            userImageId = R.drawable.img_avatar_two,
            userName = "Sarah_Music",
            date = "2023-08-01",
            songName = "The Mother We Share",
            artistName = "CHVRCHES",
            reviewText = "Incredible! This song never gets old. From the first second it hooks you with its energy. It's one of those you can listen to a thousand times and it always feels new. A timeless classic that lives in any playlist.",
            rating = 5
        ),
        Review(
            userImageId = R.drawable.img_avatar_three,
            userName = "Joselito Records",
            date = "2025-11-23",
            songName = "Sunset",
            artistName = "The Midnight",
            reviewText = "This song goes incredibly hard. It has a brutal attitude and an unforgiving flow. From the chorus to the final drop, pure adrenaline. Impossible to listen to it and stay still.",
            rating = 5
        ),
        Review(
            userImageId = R.drawable.img_avatar_four,
            userName = "SynthFan99",
            date = "2025-12-05",
            songName = "Oblivion",
            artistName = "Grimes",
            reviewText = "A gem of dark electropop, the bassline is spectacular. The synths create an immersive atmosphere that grabs you from the first second. It has that perfect balance between mystery and energy that makes it impossible to ignore. Every listen reveals new details.",
            rating = 4
        ),
        Review(
            userImageId = R.drawable.img_avatar_five,
            userName = "AuroraBorealis",
            date = "2026-01-10",
            songName = "Runaway",
            artistName = "AURORA",
            reviewText = "It brings me so much peace. The vocals are truly angelic. Every note feels soft and deep at the same time, as if it hugs your soul. The instrumental accompanies delicately, creating an intimate and immersive atmosphere.",
            rating = 5
        ),
        Review(
            userImageId = R.drawable.img_avatar_six,
            userName = "NightDriver",
            date = "2026-02-14",
            songName = "Electricity",
            artistName = "The Midnight",
            reviewText = "Perfect for night driving. The saxophone solo is unforgettable. It has that retro energy that perfectly matches the city lights and the empty highway. Every beat accompanies the journey as if it were the soundtrack of a movie.",
            rating = 5
        ),
        Review(
            userImageId = R.drawable.img_avatar_one,
            userName = "PopCritic",
            date = "2026-02-19",
            songName = "Clearest Blue",
            artistName = "CHVRCHES",
            reviewText = "The moment the song explodes at the end is absolute madness. Everything builds up little by little until it reaches that climax that gives you goosebumps. The energy skyrockets and you feel the song completely surrounds you.",
            rating = 4
        )
    )
}