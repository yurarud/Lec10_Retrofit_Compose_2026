package com.example.lec10network

import com.example.lec10network.data.remote.NetworkModule
import com.example.lec10network.data.remote.RemotePostDataSource
import com.example.lec10network.data.repository.NetworkPostRepository
import com.example.lec10network.data.repository.PostRepository

object AppContainer {
    private val remoteDataSource by lazy {
        RemotePostDataSource(NetworkModule.postApi)
    }

    val postRepository: PostRepository by lazy {
        NetworkPostRepository(remoteDataSource)
    }
}
