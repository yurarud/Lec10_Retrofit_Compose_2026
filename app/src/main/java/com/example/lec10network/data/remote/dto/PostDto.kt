package com.example.lec10network.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

@Serializable
data class CreatePostRequestDto(
    val title: String,
    val body: String,
    val userId: Int
)

@Serializable
data class UpdatePostRequestDto(
    val id: Int,
    val title: String,
    val body: String,
    val userId: Int
)

@Serializable
data class PatchPostRequestDto(
    val title: String
)
