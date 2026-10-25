package com.example.hava.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hava.data.model.CityDto
import com.example.hava.data.model.WeatherResponse
import com.example.hava.data.remote.ApiClient
import com.example.hava.data.repository.CityRepository
import com.example.hava.data.repository.OpenMeteoCityRepository
import com.example.hava.data.repository.OpenMeteoWeatherRepository
import com.example.hava.data.repository.WeatherRepository
import com.example.hava.ui.state.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CityViewModel(
    private val cityRepository: CityRepository = OpenMeteoCityRepository(ApiClient.cityApi),
    private val weatherRepository: WeatherRepository = OpenMeteoWeatherRepository(ApiClient.weatherApi)
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<LoadState<List<CityDto>>>(LoadState.Loading)
    val uiState: StateFlow<LoadState<List<CityDto>>> = _uiState.asStateFlow()

    private val _selectedCity = MutableStateFlow<CityDto?>(null)
    val selectedCity: StateFlow<CityDto?> = _selectedCity.asStateFlow()

    private val _weatherState = MutableStateFlow<LoadState<WeatherResponse>?>(null)
    val weatherState: StateFlow<LoadState<WeatherResponse>?> = _weatherState.asStateFlow()

    private val _favorites = MutableStateFlow<List<CityDto>>(emptyList())
    val favorites: StateFlow<List<CityDto>> = _favorites.asStateFlow()

    private val _isFavoritesScreenOpen = MutableStateFlow(false)
    val isFavoritesScreenOpen: StateFlow<Boolean> = _isFavoritesScreenOpen.asStateFlow()

    fun toggleFavorite(city: CityDto) {
        val current = _favorites.value.toMutableList()
        val existing = current.find { it.id == city.id }
        if (existing != null) {
            current.remove(existing)
        } else {
            current.add(city)
        }
        _favorites.value = current
    }

    fun isFavorite(city: CityDto): Boolean {
        return _favorites.value.any { it.id == city.id }
    }

    fun openFavorites() {
        _isFavoritesScreenOpen.value = true
    }

    fun closeFavorites() {
        _isFavoritesScreenOpen.value = false
    }

    init {
        loadDefaultCities()
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isBlank()) {
            loadDefaultCities()
        } else {
            search(newQuery)
        }
    }

    fun selectCity(city: CityDto) {
        _selectedCity.value = city
        fetchWeather(city.latitude, city.longitude)
    }

    fun clearSelectedCity() {
        _selectedCity.value = null
        _weatherState.value = null
        if (_query.value.isNotBlank()) {
            search(_query.value)
        } else {
            loadDefaultCities()
        }
    }

    private fun fetchWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            _weatherState.value = LoadState.Loading
            try {
                val weather = weatherRepository.getWeather(lat, lon)
                _weatherState.value = LoadState.Success(weather)
            } catch (e: IOException) {
                _weatherState.value = LoadState.Error("İnternet bağlantınızı kontrol edin.")
            } catch (e: HttpException) {
                _weatherState.value = LoadState.Error("Sunucu hatası: ${e.message()}")
            } catch (e: Exception) {
                _weatherState.value = LoadState.Error("Hava durumu alınamadı.")
            }
        }
    }

    fun loadDefaultCities() {
        viewModelScope.launch {
            _uiState.value = LoadState.Loading
            try {
                val majorCityNames = listOf("İstanbul", "Ankara", "İzmir", "Bursa", "Antalya")
                val allCities = mutableListOf<CityDto>()
                for (cityName in majorCityNames) {
                    val result = cityRepository.search(cityName)
                    result.firstOrNull()?.let { allCities.add(it) }
                }
                if (allCities.isEmpty()) {
                    _uiState.value = LoadState.Empty
                } else {
                    _uiState.value = LoadState.Success(allCities)
                }
            } catch (e: IOException) {
                _uiState.value = LoadState.Error("İnternet bağlantınızı kontrol edin.")
            } catch (e: HttpException) {
                _uiState.value = LoadState.Error("Sunucu hatası: ${e.message()}")
            } catch (e: Exception) {
                _uiState.value = LoadState.Error("Beklenmeyen bir hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    fun search(searchQuery: String = _query.value) {
        if (searchQuery.isBlank()) {
            loadDefaultCities()
            return
        }

        viewModelScope.launch {
            _uiState.value = LoadState.Loading
            try {
                val cities = cityRepository.search(searchQuery)
                if (cities.isEmpty()) {
                    _uiState.value = LoadState.Empty
                } else {
                    _uiState.value = LoadState.Success(cities)
                }
            } catch (e: IOException) {
                _uiState.value = LoadState.Error("İnternet bağlantınızı kontrol edin.")
            } catch (e: HttpException) {
                _uiState.value = LoadState.Error("Sunucu hatası: ${e.message()}")
            } catch (e: Exception) {
                _uiState.value = LoadState.Error("Beklenmeyen bir hata oluştu: ${e.localizedMessage}")
            }
        }
    }
}
