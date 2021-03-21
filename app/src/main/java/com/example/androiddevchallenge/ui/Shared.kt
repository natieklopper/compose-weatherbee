package com.example.androiddevchallenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun UrlImage(
    url: String,
    modifier: Modifier = Modifier
) {
    CoilImage(
        data = url,
        contentDescription = "My content description",
        loading = {
            Box(Modifier.matchParentSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        },
        error = {
            Box(
                Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colors.error)
            )
        },
        fadeIn = true,
        alignment = Alignment.Center,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}