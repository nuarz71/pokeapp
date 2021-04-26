package id.nuarz.pokeapp.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nuarz.pokeapp.core.BaseViewModel
import id.nuarz.pokeapp.core.ext.handleError
import id.nuarz.pokeapp.data.Repository
import id.nuarz.pokeapp.data.resultmodel.ErrorResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
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
            Event.RetryStat -> requestDetailStat()
            Event.RetryEvolution -> requestEvolution()
        }
    }

    private fun requestEvolution() {
        pushState(State.EvolutionState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            if (evolutionId.value == null) return@launch
            try {
                repository.evolution(0, evolutionId.value!!).collectLatest { result ->
                    val items = result.map { evolve ->
                        EvolutionItemModel(
                            "https://pokeres.bastionbot.org/images/pokemon/${evolve.fromId}.png",
                            evolve.from,
                            "https://pokeres.bastionbot.org/images/pokemon/${evolve.toId}.png",
                            evolve.to,
                            evolve.trigger
                        )
                    }
                    withContext(Dispatchers.Main) {
                        pushState(State.EvolutionState.Loaded(items))
                    }
                }
            } catch (e: Throwable) {
                when (val error = e.handleError()) {
                    is ErrorResult.Connection -> withContext(Dispatchers.Main) {
                        pushState(State.ConnectionFailed)
                    }
                    is ErrorResult.Failed -> withContext(Dispatchers.Main) {
                        pushState(State.EvolutionState.Failed(error.message))
                    }
                }
            }
        }
    }

    private fun requestDetailStat() {
        pushState(State.DetailStatState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            if (pokemonId.value == null) {
                return@launch
            }
            try {
                val id = pokemonId.value!!
                repository.detail(id).collectLatest { result ->
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
            } catch (e: Throwable) {
                when (val error = e.handleError()) {
                    is ErrorResult.Connection -> withContext(Dispatchers.Main) {
                        pushState(State.ConnectionFailed)
                    }
                    is ErrorResult.Failed -> withContext(Dispatchers.Main) {
                        pushState(State.EvolutionState.Failed(error.message))
                    }
                }
            }
        }
    }

    override fun onCleared() {
        viewModelScope.coroutineContext.cancel()
        super.onCleared()
    }
}