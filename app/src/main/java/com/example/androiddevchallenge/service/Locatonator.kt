package com.example.androiddevchallenge.service

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import com.google.android.gms.location.LocationServices

private const val PERMISSION_ID = 1234

class Locatonation(private val ctx: AppCompatActivity) : Locatonator {

    private lateinit var listen: (Pair<Double, Double>) -> Unit

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
                LocationServices.getFusedLocationProviderClient(ctx)
                    .lastLocation.addOnCompleteListener { task ->
                        val location = task.result
                        listen(
                            Pair(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
            } else {
                //TODO ask to enable location
                listen(
                    Pair(
                        -33.879582,
                        151.210244
                    )
                )
            }
        } else {
            askPermission()
        }
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