package id.nuarz.pokeapp.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nuarz.pokeapp.core.BaseViewModel
import id.nuarz.pokeapp.core.ext.handleError
import id.nuarz.pokeapp.data.Repository
import id.nuarz.pokeapp.data.resultmodel.ErrorResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<Event, State>() {

    override fun onEvent(event: Event) {
        when (event) {
            Event.OnViewCreate -> requestPokemon()
            Event.RetryClick -> requestPokemon()
        }
    }

    private fun requestPokemon() {
        pushState(State.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.listPokemon(0).collectLatest { result ->
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
                    withContext(Dispatchers.Main) {
                        pushState(State.Loaded(item))
                    }
                }
            } catch (e: Throwable) {
                when (val failed = e.handleError()) {
                    is ErrorResult.Connection -> withContext(Dispatchers.Main) {
                        pushState(State.ConnectionError)
                    }
                    is ErrorResult.Failed -> withContext(Dispatchers.Main) {
                        pushState(State.Failed(failed.message))
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