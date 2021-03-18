package com.example.androiddevchallenge.ui

import androidx.lifecycle.MutableLiveData

data class WeatherBeeModel(
    val isLoading: Boolean = true,
    val title: String = "WeatherBee",
    val timezone: String = "",
    val sunrise: String = "",
    val sunset: String = "",
) {
    fun update(
        new: WeatherBeeModel,
        data: MutableLiveData<WeatherBeeModel>
    ) {
        val tmp = this.copy(
            isLoading = new.isLoading,
            title = new.title,
            timezone = new.timezone,
            sunrise = new.sunrise,
            sunset = new.sunset
        )
        data.value = tmp
    }
}