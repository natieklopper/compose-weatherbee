package com.example.androiddevchallenge.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.theme.WeatherBeeTheme
import com.example.androiddevchallenge.ui.theme.progressSize

@Composable
fun MyApp(model: WeatherBeeModel = WeatherBeeModel()) {
    Surface(
        Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.background
    ) {
        Row {
            Column {
                Text(text = model.title)
                Text(text = model.sunrise)
                Text(text = model.sunset)
                Text(text = model.timezone)
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