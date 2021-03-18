package com.example.androiddevchallenge.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.domain.WeatherApiAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TheViewModel : ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    val model = MutableLiveData(WeatherBeeModel())

    init {
        getMeTheWeatherMate()
    }

    private fun getMeTheWeatherMate() {
        update(WeatherBeeModel(title = "Loading!"))

        launch {
            try {

                val response = async {
                    WeatherApiAdapter.apiClient.getWeather(
                        "-33.879582",
                        "151.210244"
                    )
                }

                val data = response.await().body()!!
                print(data.toString())

                update(
                    WeatherBeeModel(
                        title = "Done!",
                        isLoading = false,
                        timezone = data.timezone,
                        sunrise = data.current.sunrise.toString(),
                        sunset = data.current.sunset.toString(),
                    )
                )
            } catch (e: Exception) {
                print(e.toString())
            }
        }
    }

    private fun update(new: WeatherBeeModel) {
        model.value?.update(new, model)
    }
}