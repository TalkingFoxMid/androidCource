package com.talkingfox.composedtrabbithacker.ui.viewmodels.load

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.talkingfox.composedtrabbithacker.usecases.PreloadUseCase
import com.talkingfox.composedtrabbithacker.usecases.TokenUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadViewModel @Inject constructor(private val tokenUseCases: TokenUseCases, private val preloadUseCase: PreloadUseCase): ViewModel() {

    fun reduce(event: Event): Unit {

        when(event) {
            is Event.TryLoad -> {
                viewModelScope.launch(Dispatchers.IO) {

                    val token = tokenUseCases.getToken()
                    if(token == null) {
                        viewModelScope.launch {
                            mState.value = State.NeedAuthorization("")
                        }
                    } else {
                        viewModelScope.launch(Dispatchers.IO) {
                            val isPreloaded = preloadUseCase.preloadCache()
                            println("PRELOADED: ${isPreloaded}")
                            if(isPreloaded) {
                                viewModelScope.launch {
                                    mState.value = State.Ready
                                }
                            } else {
                                viewModelScope.launch {
                                    mState.value = State.NeedAuthorization(token)
                                }
                            }
                        }
                    }

                }
            }
            is Event.EditToken -> {
                if(mState.value is State.NeedAuthorization) {
                    mState.value = State.NeedAuthorization(event.s)
                }
            }
            Event.Authorize -> {
                val st = mState.value
                if(st is State.NeedAuthorization) {
                    viewModelScope.launch(Dispatchers.IO) {
                        tokenUseCases.setToken(st.bufferToken)
                        viewModelScope.launch { mState.value = State.Loading }
                    }

                }

            }
        }
    }

    private val mState: MutableLiveData<State> = MutableLiveData(State.Loading)

    val state: LiveData<State> = mState
}