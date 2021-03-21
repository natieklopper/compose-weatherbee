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

    private val listen: (Pair<Double, Double>) -> Unit = {
        update(WeatherBeeModel(latLong = it))
        getMeTheWeatherMate()
    }

    fun setup(
        weatherApi: WeatherApi,
        pexelApi: PexelApi,
        location: Locatonator
    ) {
        this.weatherApi = weatherApi
        this.pexelApi = pexelApi
        this.location = location
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
                        loc?.second.toString()
                    )
                }

                if (weatherResponse.isSuccessful) {
                    val pexelResponse = withContext(Dispatchers.Default) {
                        pexelApi.getWeatherPic("computers")
                    }

                    val image = if (pexelResponse.isSuccessful) {
                        pexelResponse.body()?.let {
                            val photo = getRandomPic(it.photos)
                            Pair(
                                photo.src.portrait,
                                photo.avg_color
                            )
                        } ?: Pair("", "")
                    } else {
                        Pair(
                            "https://picsum.photos/720/1080",
                            "#000000"
                        )
                    }

                    weatherResponse.body()?.let {
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

    private fun getForegroundColor(hex: String): Int {
//        if (red*0.299 + green*0.587 + blue*0.114) > 186 use #000000 else use #ffffff
        return 0xf
    }

    private fun update(new: WeatherBeeModel) {
        model.value?.update(new, model)
    }
}
