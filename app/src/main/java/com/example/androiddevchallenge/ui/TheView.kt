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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.WeatherBeeTheme
import com.example.androiddevchallenge.ui.theme.circleBack
import com.example.androiddevchallenge.ui.theme.gutter
import com.example.androiddevchallenge.ui.theme.progressSize
import com.example.androiddevchallenge.ui.theme.textColor

@Composable
fun MyApp(
    model: WeatherBeeModel = WeatherBeeModel(),
    interact: () -> Unit = {}
) {
    Surface(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        color = MaterialTheme.colors.background
    ) {
        UrlImage(
            model.image,
            modifier = Modifier
                .fillMaxHeight()
                .clickable { interact() }
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Circle(circleBack())
        }
        Row(
            modifier = Modifier.wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(gutter)
                    .wrapContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = model.timezone,
                    style = MaterialTheme.typography.h3,
                    color = textColor()
                )
                Text(
                    text = model.title,
                    style = MaterialTheme.typography.caption,
                    color = textColor()
                )
                Text(
                    text = model.temp,
                    style = MaterialTheme.typography.h3,
                    color = textColor()
                )
                UrlImage(
                    model.icon,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (model.isLoading) CircularProgressIndicator(progressSize)
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    WeatherBeeTheme { MyApp() }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    WeatherBeeTheme(darkTheme = true) { MyApp() }
}
