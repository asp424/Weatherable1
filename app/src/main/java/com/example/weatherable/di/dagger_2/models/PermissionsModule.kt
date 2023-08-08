package com.example.weatherable.di.dagger_2.models

import com.example.weatherable.permissions.Permissions
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface PermissionsModule {

    @Binds
    @Singleton
    fun bindPermissionsModule(permissions: Permissions.Base): Permissions
}