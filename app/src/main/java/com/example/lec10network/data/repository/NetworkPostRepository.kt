package com.example.lec10network.data.repository

import com.example.lec10network.data.mapper.toDomain
import com.example.lec10network.data.mapper.toRequestDto
import com.example.lec10network.data.remote.RemotePostDataSource
import com.example.lec10network.data.remote.RemoteResponse
import com.example.lec10network.data.remote.dto.PatchPostRequestDto
import com.example.lec10network.data.remote.dto.UpdatePostRequestDto
import com.example.lec10network.data.result.DataResult
import com.example.lec10network.data.result.toNetworkError
import com.example.lec10network.domain.model.NewPost
import com.example.lec10network.domain.model.Post

class NetworkPostRepository(
    private val remoteDataSource: RemotePostDataSource
) : PostRepository {

    override suspend fun getPosts(userId: Int?): DataResult<List<Post>> = execute(
        call = { remoteDataSource.getPosts(userId) },
        map = { dtoList -> dtoList.map { it.toDomain() } }
    )

    override suspend fun getPost(id: Int): DataResult<Post> = execute(
        call = { remoteDataSource.getPost(id) },
        map = { dto -> dto.toDomain() }
    )

    override suspend fun createPost(post: NewPost): DataResult<Post> = execute(
        call = { remoteDataSource.createPost(post.toRequestDto()) },
        map = { dto -> dto.toDomain() }
    )

    override suspend fun updatePost(post: Post): DataResult<Post> = execute(
        call = {
            remoteDataSource.updatePost(
                id = post.id,
                request = UpdatePostRequestDto(
                    id = post.id,
                    userId = post.userId,
                    title = post.title,
                    body = post.body
                )
            )
        },
        map = { dto -> dto.toDomain() }
    )

    override suspend fun patchTitle(id: Int, title: String): DataResult<Post> = execute(
        call = { remoteDataSource.patchPost(id, PatchPostRequestDto(title)) },
        map = { dto -> dto.toDomain() }
    )

    override suspend fun deletePost(id: Int): DataResult<Unit> = execute(
        call = { remoteDataSource.deletePost(id) },
        map = { Unit }
    )

    override suspend fun forceHttp404(): DataResult<Unit> = execute(
        call = { remoteDataSource.forceHttp404() },
        map = { Unit }
    )

    private suspend fun <REMOTE, DOMAIN> execute(
        call: suspend () -> RemoteResponse<REMOTE>,
        map: (REMOTE) -> DOMAIN
    ): DataResult<DOMAIN> = try {
        val response = call()
        DataResult.Success(
            data = map(response.body),
            httpStatusCode = response.statusCode
        )
    } catch (throwable: Throwable) {
        DataResult.Error(throwable.toNetworkError())
    }
}
