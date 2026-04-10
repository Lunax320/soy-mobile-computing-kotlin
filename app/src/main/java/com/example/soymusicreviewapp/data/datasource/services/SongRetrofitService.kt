package com.example.soymusicreviewapp.data.datasource.services

import com.example.soymusicreviewapp.data.dtos.SongDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SongRetrofitService {
    @GET("songs")
    suspend fun getAllSongs(): List<SongDto>

    @GET("songs/{songId}")
    suspend fun getSongById(@Path("songId") songId: Int): SongDto
}