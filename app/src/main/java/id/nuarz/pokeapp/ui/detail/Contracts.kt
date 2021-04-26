package id.nuarz.pokeapp.ui.detail

sealed class Event {
    data class OnViewCreated(val id: Int) : Event()
    object StatClick : Event()
    object EvolutionClick : Event()
    object RetryStat : Event()
    object RetryEvolution : Event()
}

sealed class State {
    sealed class DetailStatState : State() {
        object Loading : DetailStatState()
        data class Loaded(
            val overview: String?,
            val list: List<DetailUiModel>
        ) : DetailStatState()

        data class Failed(val message: String?) : DetailStatState()
    }

    sealed class EvolutionState : State() {
        object Loading : EvolutionState()
        data class Loaded(
            val list: List<EvolutionItemModel>
        ) : EvolutionState()

        data class Failed(val message: String?) : EvolutionState()
    }

    object ConnectionFailed : State()
}