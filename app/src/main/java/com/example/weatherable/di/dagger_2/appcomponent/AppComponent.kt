package com.example.weatherable.di.dagger_2.appcomponent


import android.app.Application
import com.example.weatherable.activity.DetailGisActivity
import com.example.weatherable.activity.DetailYanActivity
import com.example.weatherable.activity.MainActivity
import com.example.weatherable.di.dagger_2.models.*
import com.example.weatherable.di.dagger_2.models.viewmodel.ViewModelFactoryModule
import com.example.weatherable.work.MyJobScheduler
import com.example.weatherable.work.WorkManagerBluetooth
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [RepositoryModule::class,
        JsoupSourceModule::class,
        ViewModelFactoryModule::class,
        RestSourceModule::class,
        DatabaseModule::class, WorkMModule::class,
        BluetoothSourceModule::class,
        BluetoothDaoModule::class,
        NotificationProviderModule::class, ResourceProviderModule::class
    ]

)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(service: MyJobScheduler)
    fun inject(activity: DetailGisActivity)
    fun inject(activity: DetailYanActivity)
    fun inject(workManagerBluetooth: WorkManagerBluetooth)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun create(): AppComponent
    }
}