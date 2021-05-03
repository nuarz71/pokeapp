package id.nuarz.pokeapp.core.ext

import id.nuarz.pokeapp.data.resultmodel.ErrorResult
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

suspend fun Throwable.handleError(
    connection:suspend (ErrorResult.Connection) -> Unit,
    error: suspend (ErrorResult.Failed) -> Unit
) {
    when (this) {
        is SocketTimeoutException -> error(ErrorResult.Failed("Timeout"))
        is IOException, is ConnectException -> connection(ErrorResult.Connection)
        is HttpException -> error(ErrorResult.Failed("Something wrong"))
        else -> error(ErrorResult.Failed(message))
    }
}