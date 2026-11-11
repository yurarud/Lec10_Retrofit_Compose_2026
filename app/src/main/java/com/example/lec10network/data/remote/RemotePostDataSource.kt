package com.example.lec10network.data.remote

import com.example.lec10network.data.remote.dto.CreatePostRequestDto
import com.example.lec10network.data.remote.dto.PatchPostRequestDto
import com.example.lec10network.data.remote.dto.PostDto
import com.example.lec10network.data.remote.dto.UpdatePostRequestDto
import retrofit2.HttpException
import retrofit2.Response

class RemotePostDataSource(
    private val api: PostApi
) {
    suspend fun getPosts(userId: Int?): RemoteResponse<List<PostDto>> =
        api.getPosts(userId).requireBody()

    suspend fun getPost(id: Int): RemoteResponse<PostDto> =
        api.getPost(id).requireBody()

    suspend fun createPost(request: CreatePostRequestDto): RemoteResponse<PostDto> =
        api.createPost(request).requireBody()

    suspend fun updatePost(id: Int, request: UpdatePostRequestDto): RemoteResponse<PostDto> =
        api.updatePost(id, request).requireBody()

    suspend fun patchPost(id: Int, request: PatchPostRequestDto): RemoteResponse<PostDto> =
        api.patchPost(id, request).requireBody()

    suspend fun deletePost(id: Int): RemoteResponse<Unit> {
        val response = api.deletePost(id)
        if (!response.isSuccessful) throw HttpException(response)
        return RemoteResponse(Unit, response.code())
    }

    suspend fun forceHttp404(): RemoteResponse<Unit> {
        val response = api.forceHttp404()
        if (!response.isSuccessful) throw HttpException(response)
        return RemoteResponse(Unit, response.code())
    }
}

private fun <T> Response<T>.requireBody(): RemoteResponse<T> {
    if (!isSuccessful) throw HttpException(this)

    val responseBody = body()
        ?: throw IllegalStateException("Server returned an empty response body")

    return RemoteResponse(
        body = responseBody,
        statusCode = code()
    )
}
