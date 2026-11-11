package com.example.lec10network.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lec10network.data.repository.PostRepository
import com.example.lec10network.data.result.DataResult
import com.example.lec10network.data.result.toUserMessage
import com.example.lec10network.domain.model.NewPost
import com.example.lec10network.domain.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NetworkDemoViewModel(
    private val repository: PostRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NetworkDemoUiState())
    val uiState: StateFlow<NetworkDemoUiState> = _uiState.asStateFlow()

    init {
        loadAllPosts()
    }

    fun onUserIdFilterChanged(value: String) = update { copy(userIdFilter = value) }
    fun onPostIdChanged(value: String) = update { copy(postIdInput = value) }
    fun onNewPostUserIdChanged(value: String) = update { copy(newPostUserId = value) }
    fun onNewPostTitleChanged(value: String) = update { copy(newPostTitle = value) }
    fun onNewPostBodyChanged(value: String) = update { copy(newPostBody = value) }
    fun onChangePostIdChanged(value: String) = update { copy(changePostId = value) }
    fun onChangePostUserIdChanged(value: String) = update { copy(changePostUserId = value) }
    fun onChangePostTitleChanged(value: String) = update { copy(changePostTitle = value) }
    fun onChangePostBodyChanged(value: String) = update { copy(changePostBody = value) }
    fun onPatchTitleChanged(value: String) = update { copy(patchTitle = value) }

    fun clearError() = update { copy(errorMessage = null) }

    fun loadAllPosts() {
        loadPosts(userId = null)
    }

    fun loadPostsByUser() {
        val userId = uiState.value.userIdFilter.toIntOrNull()
        if (userId == null) {
            update { copy(errorMessage = "Введіть числовий userId, наприклад 1.") }
            return
        }
        loadPosts(userId)
    }

    private fun loadPosts(userId: Int?) {
        viewModelScope.launch {
            val request = if (userId == null) "GET /posts" else "GET /posts?userId=$userId"
            startRequest(request)

            when (val result = repository.getPosts(userId)) {
                is DataResult.Success -> update {
                    copy(
                        posts = result.data,
                        isLoading = false,
                        errorMessage = null,
                        lastHttpStatusCode = result.httpStatusCode
                    )
                }

                is DataResult.Error -> finishWithError(result)
            }
        }
    }

    fun loadPostById() {
        val id = uiState.value.postIdInput.toIntOrNull()
        if (id == null) {
            update { copy(errorMessage = "Введіть числовий ID поста.") }
            return
        }

        viewModelScope.launch {
            startRequest("GET /posts/$id")

            when (val result = repository.getPost(id)) {
                is DataResult.Success -> update {
                    copy(
                        selectedPost = result.data,
                        isLoading = false,
                        errorMessage = null,
                        lastHttpStatusCode = result.httpStatusCode
                    )
                }

                is DataResult.Error -> finishWithError(result)
            }
        }
    }

    fun createPost() {
        val state = uiState.value
        val userId = state.newPostUserId.toIntOrNull()
        if (userId == null || state.newPostTitle.isBlank() || state.newPostBody.isBlank()) {
            update { copy(errorMessage = "Заповніть userId, title і body.") }
            return
        }

        viewModelScope.launch {
            startRequest("POST /posts")

            val newPost = NewPost(
                userId = userId,
                title = state.newPostTitle.trim(),
                body = state.newPostBody.trim()
            )

            when (val result = repository.createPost(newPost)) {
                is DataResult.Success -> update {
                    copy(
                        createdPost = result.data,
                        isLoading = false,
                        errorMessage = null,
                        lastHttpStatusCode = result.httpStatusCode
                    )
                }

                is DataResult.Error -> finishWithError(result)
            }
        }
    }


    fun putPost() {
        val state = uiState.value
        val id = state.changePostId.toIntOrNull()
        val userId = state.changePostUserId.toIntOrNull()
        if (id == null || userId == null || state.changePostTitle.isBlank() || state.changePostBody.isBlank()) {
            update { copy(errorMessage = "Для PUT заповніть id, userId, title і body.") }
            return
        }
        viewModelScope.launch {
            startRequest("PUT /posts/$id")
            val post = Post(id, userId, state.changePostTitle.trim(), state.changePostBody.trim())
            when (val result = repository.updatePost(post)) {
                is DataResult.Success -> update {
                    copy(changedPost = result.data, isLoading = false, errorMessage = null,
                        lastHttpStatusCode = result.httpStatusCode, deleteMessage = null)
                }
                is DataResult.Error -> finishWithError(result)
            }
        }
    }

    fun patchPostTitle() {
        val state = uiState.value
        val id = state.changePostId.toIntOrNull()
        if (id == null || state.patchTitle.isBlank()) {
            update { copy(errorMessage = "Для PATCH введіть id і новий title.") }
            return
        }
        viewModelScope.launch {
            startRequest("PATCH /posts/$id")
            when (val result = repository.patchTitle(id, state.patchTitle.trim())) {
                is DataResult.Success -> update {
                    copy(changedPost = result.data, isLoading = false, errorMessage = null,
                        lastHttpStatusCode = result.httpStatusCode, deleteMessage = null)
                }
                is DataResult.Error -> finishWithError(result)
            }
        }
    }

    fun deletePost() {
        val id = uiState.value.changePostId.toIntOrNull()
        if (id == null) {
            update { copy(errorMessage = "Для DELETE введіть числовий id.") }
            return
        }
        viewModelScope.launch {
            startRequest("DELETE /posts/$id")
            when (val result = repository.deletePost(id)) {
                is DataResult.Success -> update {
                    copy(changedPost = null, isLoading = false, errorMessage = null,
                        lastHttpStatusCode = result.httpStatusCode,
                        deleteMessage = "DELETE /posts/$id виконано (JSONPlaceholder імітує видалення).")
                }
                is DataResult.Error -> finishWithError(result)
            }
        }
    }

    fun forceHttp404() {
        viewModelScope.launch {
            startRequest("GET /lecture10-this-route-does-not-exist")

            when (val result = repository.forceHttp404()) {
                is DataResult.Success -> update {
                    copy(
                        isLoading = false,
                        lastHttpStatusCode = result.httpStatusCode
                    )
                }

                is DataResult.Error -> finishWithError(result)
            }
        }
    }

    private fun startRequest(label: String) = update {
        copy(
            isLoading = true,
            errorMessage = null,
            lastRequest = label,
            lastHttpStatusCode = null
        )
    }

    private fun finishWithError(result: DataResult.Error) = update {
        copy(
            isLoading = false,
            errorMessage = result.error.toUserMessage(),
            lastHttpStatusCode = when (val error = result.error) {
                is com.example.lec10network.data.result.NetworkError.Http -> error.code
                else -> null
            }
        )
    }

    private fun update(transform: NetworkDemoUiState.() -> NetworkDemoUiState) {
        _uiState.update { current -> current.transform() }
    }

    companion object {
        fun factory(repository: PostRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NetworkDemoViewModel(repository) as T
                }
            }
    }
}
