package com.example.soymusicreviewapp.data.dtos

data class RegisterUserDto(
    val username: String,
    val name: String?,
    val FCMToken: String
) {
    constructor() : this(
        username = "",
        name = null,
        FCMToken = ""
    )
}
