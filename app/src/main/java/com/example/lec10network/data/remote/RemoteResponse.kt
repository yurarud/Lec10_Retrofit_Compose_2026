package com.example.lec10network.data.remote

data class RemoteResponse<T>(
    val body: T,
    val statusCode: Int
)
