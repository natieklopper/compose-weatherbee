/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.domain.PexelApi
import com.example.androiddevchallenge.domain.Photos
import com.example.androiddevchallenge.domain.WeatherApi
import com.example.androiddevchallenge.domain.getLocaleUnit
import com.example.androiddevchallenge.service.Locatonator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TheViewModel : ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    val model = MutableLiveData(WeatherBeeModel())
    private var latLong: Pair<Double, Double> = Pair(-33.879582, 151.210244)
    lateinit var location: Locatonator
    lateinit var weatherApi: WeatherApi
    lateinit var pexelApi: PexelApi
    lateinit var locale: String

    private val listen: (Pair<Double, Double>) -> Unit = {
        latLong = it
        getMeTheWeatherMate()
    }

    fun setup(
        weatherApi: WeatherApi,
        pexelApi: PexelApi,
        location: Locatonator,
        locale: String
    ) {
        this.weatherApi = weatherApi
        this.pexelApi = pexelApi
        this.location = location
        this.locale = locale
        this.location.setup(listen)
    }

    fun getMeTheWeatherMate() {
        launch {
            try {
                val weatherResponse = withContext(Dispatchers.Default) {
                    weatherApi.getWeather(
                        latLong.first.toString(),
                        latLong.second.toString(),
                        getLocaleUnit(locale)
                    )
                }

                if (weatherResponse.isSuccessful) {
                    weatherResponse.body()?.let { wr ->
                        val pexelResponse = withContext(Dispatchers.Default) {
                            pexelApi.getWeatherPic(
                                wr.timezone.replace('/', ' ') + " " +
                                    wr.current.weather.first().description
                            )
                        }

                        val image = if (pexelResponse.isSuccessful) {
                            pexelResponse.body()?.let { pr ->
                                val photo = getRandomPic(pr.photos)
                                Pair(photo.src.portrait, photo.avg_color)
                            } ?: Pair("", "")
                        } else {
                            Pair("https://picsum.photos/720/1080", "#000000")
                        }

                        model.value = WeatherBeeModel(
                            isLoading = false,
                            title = wr.current.weather.first().description,
                            image = image.first,
                            timezone = wr.timezone.substring(wr.timezone.indexOf('/') + 1)
                                .replace('_', ' '),
                            temp = getNiceTemp(wr.current.temp),
                            icon = getIconUrl(wr.current.weather.first().icon)
                        )
                    }
                }
            } catch (e: Exception) {
                print(e.toString())
            }
        }
    }

    private fun getNiceTemp(temp: Double) = "$temp°"
    private fun getRandomPic(picUrls: List<Photos>) = picUrls.shuffled().take(1)[0]
    private fun getIconUrl(icon: String) = "https://openweathermap.org/img/wn/$icon@2x.png"
}
