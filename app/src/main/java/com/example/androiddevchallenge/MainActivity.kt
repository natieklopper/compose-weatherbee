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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import com.example.androiddevchallenge.domain.PexelApiAdapter
import com.example.androiddevchallenge.domain.WeatherApiAdapter
import com.example.androiddevchallenge.service.Locatonation
import com.example.androiddevchallenge.ui.MyApp
import com.example.androiddevchallenge.ui.TheViewModel
import com.example.androiddevchallenge.ui.WeatherBeeModel
import com.example.androiddevchallenge.ui.theme.WeatherBeeTheme
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(TheViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setup(
            WeatherApiAdapter.client,
            PexelApiAdapter.client,
            Locatonation(this),
            Locale.getDefault().language
        )
        setContent {
            WeatherBeeTheme {
                val model by viewModel.model.observeAsState(WeatherBeeModel())
                MyApp(model = model) {
                    viewModel.getMeTheWeatherMate()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        viewModel.location.onRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
