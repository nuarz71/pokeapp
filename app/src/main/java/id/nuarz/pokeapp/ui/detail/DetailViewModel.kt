package id.nuarz.pokeapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nuarz.pokeapp.core.BaseViewModel
import id.nuarz.pokeapp.data.Repository
import id.nuarz.pokeapp.data.RepositoryImpl
import id.nuarz.pokeapp.data.resultmodel.ResultState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectIndexed
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) :
    BaseViewModel<Event, State>() {

    private val evolutionId = MutableLiveData<Int>()
    private val pokemonId = MutableLiveData<Int>()

    override fun onEvent(event: Event) {
        when (event) {
            is Event.OnViewCreated -> {
                pokemonId.value = event.id
                requestDetailStat()
            }
            Event.StatClick -> requestDetailStat()
            Event.EvolutionClick -> requestEvolution()
        }
    }

    private fun requestEvolution() = viewModelScope.launch {
        if (evolutionId.value == null) return@launch
        repository.evolution(0, evolutionId.value!!).collectIndexed { _, value ->
            value.fetch(
                onLoading = {
                    pushState(State.EvolutionState.Loading)
                },
                onFailed = { cause ->
                    when (cause) {
                        ResultState.Failed.Connection -> pushState(State.ConnectionFailed)
                        is ResultState.Failed.Error -> pushState(State.DetailStatState.Failed(cause.message))
                    }
                }
            ) {
                val items = it.map { evolve ->
                    EvolutionItemModel(
                        "https://pokeres.bastionbot.org/images/pokemon/${evolve.fromId}.png",
                        evolve.from,
                        "https://pokeres.bastionbot.org/images/pokemon/${evolve.toId}.png",
                        evolve.to,
                        evolve.trigger
                    )
                }
                pushState(State.EvolutionState.Loaded(items))
            }
        }
    }

    private fun requestDetailStat() = viewModelScope.launch {
        if (pokemonId.value == null) {
            return@launch
        }
        val id = pokemonId.value!!
        repository.detail(id).collectIndexed { _, value ->
            value.fetch(
                onLoading = {
                    pushState(State.DetailStatState.Loading)
                },
                onFailed = { cause ->
                    when (cause) {
                        ResultState.Failed.Connection -> pushState(State.ConnectionFailed)
                        is ResultState.Failed.Error -> pushState(State.DetailStatState.Failed(cause.message))
                    }
                }
            ) { result ->
                evolutionId.value = result.evolutionId
                val list = listOf(
                    StatItemModel(
                        result.stat.hp,
                        result.stat.atk,
                        result.stat.def,
                        result.stat.satk,
                        result.stat.sdef,
                        result.stat.spd
                    ),
                    AbilitiesItemModel(
                        result.abilities.map {
                            AbilityItemModel(it.name, it.overview)
                        }
                    ),
                    BreedingItemModel(
                        result.breeding.eggGroups.joinToString("\n"),
                        result.breeding.hatchCounter,
                        result.breeding.genderRate
                    ),
                    CaptureItemModel(
                        result.capture.habitat,
                        result.capture.generation,
                        result.capture.captureRate
                    ),
                    SpritesItemModel(
                        "https://pokeres.bastionbot.org/images/pokemon/$id.png",
                        "https://pokeres.bastionbot.org/images/pokemon/$id.png"
                    )
                )
                pushState(
                    State.DetailStatState.Loaded(
                        result.overview,
                        list
                    )
                )
            }
        }
    }

    override fun onCleared() {
        viewModelScope.coroutineContext.cancel()
        super.onCleared()
    }
}