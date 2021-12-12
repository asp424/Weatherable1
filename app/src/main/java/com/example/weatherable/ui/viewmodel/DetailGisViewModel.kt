package com.example.weatherable.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.example.weatherable.data.repository.Repository
import com.example.weatherable.data.view_states.InternetResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailGisViewModel @Inject constructor(private val repository: Repository) : ViewModel(),
    DefaultLifecycleObserver {
    private val _internetValues: MutableStateFlow<InternetResponse?> =
        MutableStateFlow(InternetResponse.Loading)
    val internetValues = _internetValues.asStateFlow()
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        getGisData(owner as Context)
    }
    fun getGisData(context: Context) {
        viewModelScope.launch {
            repository.getGisData(context).collect {
                _internetValues.value = it
            }
        }
    }
}