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

data class WeatherBeeModel(
    val isLoading: Boolean = true,
    val title: String = "WeatherBee",
    val image: String = "https://picsum.photos/720/1080",
    val foregroundColor: Int = 0xf,
    val latLong: Pair<Double, Double> = Pair(-33.879582, 151.210244),
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
            image = new.image,
            foregroundColor = new.foregroundColor,
            latLong = new.latLong,
            timezone = new.timezone,
            sunrise = new.sunrise,
            sunset = new.sunset
        )
        data.value = tmp
    }
}
