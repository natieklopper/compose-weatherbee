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
    lateinit var location: Locatonator
    lateinit var weatherApi: WeatherApi
    lateinit var pexelApi: PexelApi
    lateinit var locale: String

    private val listen: (Pair<Double, Double>) -> Unit = {
        update(WeatherBeeModel(latLong = it))
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
        update(WeatherBeeModel(title = "Loading!"))
        launch {
            try {
                val weatherResponse = withContext(Dispatchers.Default) {
                    val loc = model.value?.latLong
                    weatherApi.getWeather(
                        loc?.first.toString(),
                        loc?.second.toString(),
                        getLocaleUnit(locale)
                    )
                }

                if (weatherResponse.isSuccessful) {
                    weatherResponse.body()?.let {
                        val pexelResponse = withContext(Dispatchers.Default) {
                            pexelApi.getWeatherPic(
                                it.timezone.replace('/', ' ') + " " +
                                        it.current.weather.first().description
                            )
                        }

                        val image = if (pexelResponse.isSuccessful) {
                            pexelResponse.body()?.let {
                                val photo = getRandomPic(it.photos)
                                Pair(photo.src.portrait, photo.avg_color)
                            } ?: Pair("", "")
                        } else {
                            Pair("https://picsum.photos/720/1080", "#000000")
                        }

                        update(
                            WeatherBeeModel(
                                title = "Done!",
                                isLoading = false,
                                image = image.first,
                                foregroundColor = getForegroundColor(image.second),
                                timezone = it.timezone,
                                sunrise = it.current.sunrise.toString(),
                                sunset = it.current.sunset.toString(),
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                print(e.toString())
            }
        }
    }

    private fun getRandomPic(picUrls: List<Photos>): Photos {
        return picUrls.shuffled().take(1)[0]
    }

    //https://stackoverflow.com/questions/3942878/how-to-decide-font-color-in-white-or-black-depending-on-background-color
    private fun getForegroundColor(hexVal: String): String {
        val hex = hexVal.replace("#", "")
        val r = hex.substring(0, 2).toInt(16)
        val g = hex.substring(2, 4).toInt(16)
        val b = hex.substring(4, 6).toInt(16)

        val revColor = r * .299 + g * .587 + b * .114

        return if (revColor > 186) "#000000" else "#ffffff"
    }

    private fun update(new: WeatherBeeModel) {
        model.value?.update(new, model)
    }
}
