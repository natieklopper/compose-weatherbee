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
