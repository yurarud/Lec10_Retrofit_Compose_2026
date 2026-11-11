package com.example.lec10network.data.result

sealed interface DataResult<out T> {
    data class Success<T>(
        val data: T,
        val httpStatusCode: Int
    ) : DataResult<T>

    data class Error(
        val error: NetworkError
    ) : DataResult<Nothing>
}

sealed interface NetworkError {
    data class Http(
        val code: Int,
        val message: String
    ) : NetworkError

    data object NoInternet : NetworkError
    data object Timeout : NetworkError

    data class Serialization(
        val details: String
    ) : NetworkError

    data class Io(
        val details: String
    ) : NetworkError

    data class Unknown(
        val details: String
    ) : NetworkError
}

fun NetworkError.toUserMessage(): String = when (this) {
    is NetworkError.Http -> "HTTP $code: $message"
    NetworkError.NoInternet -> "Немає доступу до мережі або DNS-сервера."
    NetworkError.Timeout -> "Час очікування мережевого запиту вичерпано."
    is NetworkError.Serialization -> "Помилка перетворення JSON: $details"
    is NetworkError.Io -> "Помилка введення/виведення: $details"
    is NetworkError.Unknown -> "Невідома помилка: $details"
}
