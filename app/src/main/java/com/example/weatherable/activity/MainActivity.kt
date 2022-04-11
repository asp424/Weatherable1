package com.example.weatherable.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherable.application.appComponent
import com.example.weatherable.ui.cells.NavController
import com.example.weatherable.ui.viewmodel.MainViewModel
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: Lazy<ViewModelProvider.Factory>

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory.get()
        )[MainViewModel::class.java]
        setContent {
            NavController(viewModel)
        }
    }
}
