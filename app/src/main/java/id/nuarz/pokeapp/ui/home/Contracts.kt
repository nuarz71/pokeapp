package id.nuarz.pokeapp.ui.home

sealed class Event {
    object OnViewCreate : Event()
    object RetryClick : Event()
}

sealed class State {
    object Loading : State()
    data class Loaded(val items: List<PokemonItemModel>) : State()
    data class Failed(val message: String?) : State()
    object ConnectionError : State()
}