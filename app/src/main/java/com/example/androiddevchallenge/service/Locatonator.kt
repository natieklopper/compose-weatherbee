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
package com.example.androiddevchallenge.service

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit

private const val PERMISSION_ID = 1234

class Locatonation(private val ctx: AppCompatActivity) : Locatonator {

    private lateinit var listen: (Pair<Double, Double>) -> Unit
    private val fusedLocation = LocationServices.getFusedLocationProviderClient(ctx)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val ll = locationResult.lastLocation
            listen(Pair(ll.latitude, ll.longitude))
        }
    }

    override fun setup(listen: (Pair<Double, Double>) -> Unit) {
        this.listen = listen
        updateLocation()
    }

    private fun updateLocation() {
        if (checkSelfPermission(ctx, permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(ctx, permission.ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            if (isEnabled()) {
                val loc = LocationRequest.create().apply {
                    interval = TimeUnit.SECONDS.toMillis(60)
                    fastestInterval = TimeUnit.SECONDS.toMillis(30)
                    maxWaitTime = TimeUnit.MINUTES.toMillis(2)
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
                fusedLocation.requestLocationUpdates(loc, locationCallback, Looper.getMainLooper())
            } else {
                listen(Pair(-33.879582, 151.210244))
            }
        } else {
            askPermission()
        }
    }

    fun destroy() {
        fusedLocation.removeLocationUpdates(locationCallback)
    }

    private fun isEnabled(): Boolean {
        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun askPermission() {
        requestPermissions(
            ctx,
            arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation()
            }
        }
    }
}

interface Locatonator {
    fun setup(listen: (Pair<Double, Double>) -> Unit)
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}
