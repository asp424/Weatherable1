package com.example.weatherable.di.dagger_2.appcomponent


import com.example.weatherable.activity.DetailGisActivity
import com.example.weatherable.activity.DetailYanActivity
import com.example.weatherable.application.App
import com.example.weatherable.activity.MainActivity
import com.example.weatherable.di.dagger_2.models.*
import com.example.weatherable.di.dagger_2.models.viewmodel.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [RepositoryModule::class,
        JsoupSourceModule::class,
        ViewModelFactoryModule::class,
        RestSourceModule::class,
    BluetoothSourceModule::class,
    BluetoothDaoModule::class,
    DatabaseModule::class
]

)
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: DetailGisActivity)
    fun inject(activity: DetailYanActivity)
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun create(): AppComponent
    }
}