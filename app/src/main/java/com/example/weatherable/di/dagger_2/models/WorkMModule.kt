package com.example.weatherable.di.dagger_2.models

import android.app.Application
import androidx.work.WorkManager
import com.example.weatherable.application.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class WorkMModule {
    @Provides
    @Singleton
    fun restSource(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }
}