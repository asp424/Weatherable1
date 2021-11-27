package com.example.weatherable.ui.cells

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherable.ui.screens.FeatureScreen
import com.example.weatherable.ui.screens.WeatherScreen
import com.example.weatherable.ui.viewmodel.MainViewModel

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun NavController(viewModel: MainViewModel) {
    Surface(color = Color.Green, modifier = Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        val bottomItems = listOf("Погода", "Прогноз")
        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = Color.Black) {
                    bottomItems.forEach { screen ->
                        BottomNavigationItem(selected = false,
                            onClick = {
                                navController.navigate(screen) {
                                    launchSingleTop = true
                                    popUpTo(screen)
                                }
                            },
                            label = { WaterName(string = screen, color = Color.White, paddingStart = 0.dp, paddingTop = 10.dp) },
                            icon = {
                                Icon(Icons.Default.WbSunny, "ass", tint = Color.White)
                            })
                    }
                }
            }
        ) {
            NavHost(navController = navController, startDestination = "Погода") {
                composable("Погода") {
                    WeatherScreen(viewModel)
                }
                composable("Прогноз") {
                    FeatureScreen()
                }
            }

        }
    }
}