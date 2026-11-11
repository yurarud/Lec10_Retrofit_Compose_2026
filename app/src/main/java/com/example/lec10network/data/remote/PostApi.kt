package com.example.lec10network.data.remote

import com.example.lec10network.data.remote.dto.CreatePostRequestDto
import com.example.lec10network.data.remote.dto.PatchPostRequestDto
import com.example.lec10network.data.remote.dto.PostDto
import com.example.lec10network.data.remote.dto.UpdatePostRequestDto
import kotlinx.serialization.json.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApi {

    @GET("posts")
    suspend fun getPosts(
        @Query("userId") userId: Int? = null
    ): Response<List<PostDto>>

    @GET("posts/{id}")
    suspend fun getPost(
        @Path("id") id: Int
    ): Response<PostDto>

    @POST("posts")
    suspend fun createPost(
        @Body request: CreatePostRequestDto
    ): Response<PostDto>

    // PUT передає повне нове представлення ресурсу.
    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body request: UpdatePostRequestDto
    ): Response<PostDto>

    // PATCH передає лише поля, які потрібно змінити.
    @PATCH("posts/{id}")
    suspend fun patchPost(
        @Path("id") id: Int,
        @Body request: PatchPostRequestDto
    ): Response<PostDto>

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Int
    ): Response<Unit>

    @GET("lecture10-this-route-does-not-exist")
    suspend fun forceHttp404(): Response<JsonObject>
}
