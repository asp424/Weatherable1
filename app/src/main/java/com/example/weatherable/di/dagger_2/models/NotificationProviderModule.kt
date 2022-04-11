package com.example.weatherable.di.dagger_2.models

import com.example.weatherable.NotificationProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NotificationProviderModule {

    @Binds
    @Singleton
    fun bindsNotificationProvider(notificationProvider: NotificationProvider.Base): NotificationProvider

}