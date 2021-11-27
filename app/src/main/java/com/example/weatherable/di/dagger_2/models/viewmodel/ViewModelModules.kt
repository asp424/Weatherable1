package com.example.weatherable.di.dagger_2.models.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherable.ui.viewmodel.DetailGisViewModel
import com.example.weatherable.ui.viewmodel.DetailYanViewModel
import com.example.weatherable.ui.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
interface ViewModelModules {
    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    fun bindsMainViewModel(viewModel: MainViewModel): ViewModel
    @IntoMap
    @Binds
    @ViewModelKey(DetailGisViewModel::class)
    fun bindsDetailGisViewModel(viewModel: DetailGisViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(DetailYanViewModel::class)
    fun bindsDetailYanViewModel(viewModel: DetailYanViewModel): ViewModel
}