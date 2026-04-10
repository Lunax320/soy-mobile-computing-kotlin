package com.example.soymusicreviewapp.data.dtos

import com.example.soymusicreviewapp.data.Song

data class SongDto(
    val id: Int,
    val name: String,
    val artist: String,
    val genre: String?,
    val duration: String?,
    val songImage: String?
)

fun SongDto.toSong(): Song {
    return Song(
        songId = id.toString(),
        name = name,
        artist = artist,
        genre = genre ?: "Desconocido",
        duration = duration ?: "0:00",
        songImage = songImage ?: "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500&q=80"
    )
}