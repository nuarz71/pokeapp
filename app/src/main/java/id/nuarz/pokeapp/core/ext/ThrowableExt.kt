package id.nuarz.pokeapp.core.ext

import id.nuarz.pokeapp.data.resultmodel.ErrorResult
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

fun Throwable.handleError(): ErrorResult {
    return when (this) {
        is SocketTimeoutException -> ErrorResult.Failed("Timeout")
        is IOException, is ConnectException -> ErrorResult.Connection
        is HttpException -> ErrorResult.Failed("Something wrong")
        else -> ErrorResult.Failed(message)
    }
}