package id.nuarz.pokeapp.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<Event, State> : ViewModel() {

    private val mutableState = MutableLiveData<State>()
    val state: LiveData<State> = mutableState

    @SuppressLint("NullSafeMutableLiveData")
    protected fun pushState(state: State) {
        mutableState.value = state
    }

    abstract fun onEvent(event: Event)
}