package com.example.lec10network.data.result

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

fun Throwable.toNetworkError(): NetworkError = when (this) {
    is HttpException -> NetworkError.Http(
        code = code(),
        message = message()
    )

    is UnknownHostException -> NetworkError.NoInternet
    is SocketTimeoutException -> NetworkError.Timeout

    is SerializationException -> NetworkError.Serialization(
        details = message ?: "SerializationException"
    )

    is IOException -> NetworkError.Io(
        details = message ?: "IOException"
    )

    else -> NetworkError.Unknown(
        details = message ?: this::class.java.simpleName
    )
}
