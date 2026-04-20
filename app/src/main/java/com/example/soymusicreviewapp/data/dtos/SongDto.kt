package com.example.soymusicreviewapp.data.dtos

import com.example.soymusicreviewapp.data.Song

data class SongDto(
    val id: String,
    val name: String,
    val artist: String,
    val genre: String?,
    val duration: String?,
    val songImage: String?
) {
    constructor() : this(
        id = "",
        name = "",
        artist = "",
        genre = null,
        duration = null,
        songImage = null
    )
}

fun SongDto.toSong(): Song {
    val finalGenre = genre ?: "Desconocido"
    val finalDuration = duration ?: "0:00"
    val finalImage = songImage ?: "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500&q=80"

    return Song(
        songId = id,
        name = name,
        artist = artist,
        genre = finalGenre,
        duration = finalDuration,
        songImage = finalImage
    )
}
