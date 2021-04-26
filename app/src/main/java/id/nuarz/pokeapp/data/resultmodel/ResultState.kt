package id.nuarz.pokeapp.data.resultmodel

sealed class ResultState<out R> {
    object Loading : ResultState<Nothing>()
    data class Loaded<out R>(val result: R) : ResultState<R>()
    sealed class Failed : ResultState<Nothing>() {
        object Connection : Failed()
        data class Error(val message: String?) : Failed()
    }

    suspend fun fetch(
        onLoading: suspend () -> Unit = {},
        onFailed: suspend (Failed) -> Unit = {},
        onSuccess: suspend (R) -> Unit
    ) {
        when (this) {
            Loading -> onLoading()
            is Loaded -> onSuccess(result)
            is Failed -> onFailed(this)
        }
    }
}
