package com.example.lec10network.data.repository

import com.example.lec10network.data.result.DataResult
import com.example.lec10network.domain.model.NewPost
import com.example.lec10network.domain.model.Post

interface PostRepository {
    suspend fun getPosts(userId: Int? = null): DataResult<List<Post>>
    suspend fun getPost(id: Int): DataResult<Post>
    suspend fun createPost(post: NewPost): DataResult<Post>
    suspend fun updatePost(post: Post): DataResult<Post>
    suspend fun patchTitle(id: Int, title: String): DataResult<Post>
    suspend fun deletePost(id: Int): DataResult<Unit>
    suspend fun forceHttp404(): DataResult<Unit>
}
