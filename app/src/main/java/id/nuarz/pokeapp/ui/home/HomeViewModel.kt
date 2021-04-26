package id.nuarz.pokeapp.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nuarz.pokeapp.core.BaseViewModel
import id.nuarz.pokeapp.data.Repository
import id.nuarz.pokeapp.data.resultmodel.ResultState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<Event, State>() {

    override fun onEvent(event: Event) {
        when (event) {
            Event.OnViewCreate -> requestPokemon()
        }
    }

    private fun requestPokemon() = viewModelScope.launch {
        repository.listPokemon(0).collectLatest { value ->
            value.fetch(
                onLoading = {
                    pushState(State.Loading)
                },
                onFailed = {
                    when (it) {
                        ResultState.Failed.Connection -> pushState(State.Failed("Connection"))
                        is ResultState.Failed.Error -> pushState(State.Failed(it.message))
                    }
                }
            ) { result ->
                val item = result.map {
                    val number = String.format("#%03d", it.id)
                    val imageUrl = "https://pokeres.bastionbot.org/images/pokemon/${it.id}.png"
                    PokemonItemModel(
                        it.id,
                        it.displayName,
                        number,
                        imageUrl,
                        it.elements.map { element ->
                            ElementItemModel(
                                element.name,
                                element.iconResId,
                                element.colorResId
                            )
                        }
                    )
                }
                pushState(State.Loaded(item))
            }
        }
    }

    override fun onCleared() {
        viewModelScope.coroutineContext.cancel()
        super.onCleared()
    }
}