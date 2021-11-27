package com.example.weatherable.ui.viewmodel

import androidx.lifecycle.*
import com.example.weatherable.data.repository.Repository
import com.example.weatherable.data.view_states.InternetResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailYanViewModel @Inject constructor(private val repository: Repository) : ViewModel(), LifecycleObserver {
    private val _internetValues: MutableStateFlow<InternetResponse?> =
            MutableStateFlow(InternetResponse.Loading)
        val internetValues = _internetValues.asStateFlow()
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun getYanData() {
            viewModelScope.launch {
                repository.getYanData().collect {
                    _internetValues.value = it
                }
            }
        }
}