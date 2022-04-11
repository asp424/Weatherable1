package com.example.weatherable.di.dagger_2.models

import com.example.weatherable.ResourceProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ResourceProviderModule {

    @Binds
    @Singleton
    fun bindsResourceProvider(resourceProvider: ResourceProvider.Base): ResourceProvider

}