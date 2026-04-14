package com.example.soymusicreviewapp.data.dtos

import com.example.soymusicreviewapp.data.Song

data class SongDto(
    val id: String = "",
    val name: String = "",
    val artist: String = "",
    val genre: String? = null,
    val duration: String? = null,
    val songImage: String? = null
)

fun SongDto.toSong(): Song {
    var finalGenre = "Desconocido"
    if (genre != null) {
        finalGenre = genre
    }

    var finalDuration = "0:00"
    if (duration != null) {
        finalDuration = duration
    }

    var finalImage = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500&q=80"
    if (songImage != null) {
        finalImage = songImage
    }

    return Song(
        songId = id,
        name = name,
        artist = artist,
        genre = finalGenre,
        duration = finalDuration,
        songImage = finalImage
    )
}