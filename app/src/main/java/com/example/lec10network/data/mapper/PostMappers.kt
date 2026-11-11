package com.example.lec10network.data.mapper

import com.example.lec10network.data.remote.dto.CreatePostRequestDto
import com.example.lec10network.data.remote.dto.PostDto
import com.example.lec10network.domain.model.NewPost
import com.example.lec10network.domain.model.Post

fun PostDto.toDomain(): Post = Post(
    id = id,
    userId = userId,
    title = title,
    body = body
)

fun NewPost.toRequestDto(): CreatePostRequestDto = CreatePostRequestDto(
    title = title,
    body = body,
    userId = userId
)
