package com.example.hava

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hava.ui.components.CityListScreen
import com.example.hava.ui.components.FavoritesScreen
import com.example.hava.ui.components.WeatherDetailScreen
import com.example.hava.ui.theme.HavaTheme
import com.example.hava.ui.viewmodel.CityViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by rememberSaveable { mutableStateOf(true) }

            HavaTheme(darkTheme = isDarkTheme) {
                WeatherAppScreen(
                    isDarkTheme   = isDarkTheme,
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

@Composable
fun WeatherAppScreen(
    viewModel: CityViewModel = viewModel(),
    isDarkTheme: Boolean = true,
    onThemeToggle: () -> Unit = {}
) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val isFavoritesOpen by viewModel.isFavoritesScreenOpen.collectAsState()
    val listState = rememberLazyListState()

    if (selectedCity != null) {
        WeatherDetailScreen(
            city         = selectedCity!!,
            weatherState = weatherState,
            viewModel    = viewModel,
            isDarkTheme  = isDarkTheme,
            onBackClick  = { viewModel.clearSelectedCity() }
        )
    } else if (isFavoritesOpen) {
        FavoritesScreen(
            viewModel     = viewModel,
            onBackClick   = { viewModel.closeFavorites() },
            onCityClick   = { city -> viewModel.selectCity(city) },
            isDarkTheme   = isDarkTheme,
            onThemeToggle = onThemeToggle
        )
    } else {
        CityListScreen(
            viewModel       = viewModel,
            listState       = listState,
            isDarkTheme     = isDarkTheme,
            onThemeToggle   = onThemeToggle,
            onOpenFavorites = { viewModel.openFavorites() },
            onCityClick     = { city ->
                viewModel.selectCity(city)
                android.util.Log.d("WeatherApp", "Seçildi: ${city.name}")
            }
        )
    }
}
