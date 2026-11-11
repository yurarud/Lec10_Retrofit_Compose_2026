package com.example.lec10network.ui

import com.example.lec10network.domain.model.Post

data class NetworkDemoUiState(
    val posts: List<Post> = emptyList(),
    val selectedPost: Post? = null,
    val createdPost: Post? = null,
    val changedPost: Post? = null,

    val userIdFilter: String = "",
    val postIdInput: String = "1",
    val newPostUserId: String = "1",
    val newPostTitle: String = "Compose + Retrofit",
    val newPostBody: String = "Створено з Android-застосунку",

    val changePostId: String = "1",
    val changePostUserId: String = "1",
    val changePostTitle: String = "Оновлений заголовок",
    val changePostBody: String = "Повне оновлення через PUT",
    val patchTitle: String = "Змінено лише title через PATCH",
    val deleteMessage: String? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val lastRequest: String = "Ще не виконано",
    val lastHttpStatusCode: Int? = null
)
