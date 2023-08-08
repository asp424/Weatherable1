package com.example.weatherable.di.dagger_2.models

import android.app.Application
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.weatherable.permissions.Permissions.Base.Companion.listOfPerm
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class HasPermissionsModule {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @[Provides Singleton Named("hasPerm")]
    fun provideHasPermissions(context: Application):() -> Boolean = {
        listOfPerm.all {
            ActivityCompat.checkSelfPermission(context, it) == PERMISSION_GRANTED
        }
    }
}