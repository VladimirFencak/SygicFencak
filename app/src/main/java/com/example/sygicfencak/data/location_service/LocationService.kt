package com.example.sygicfencak.data.location_service

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sygicfencak.MainActivity
import com.example.sygicfencak.domain.model.Location
import com.example.sygicfencak.domain.repository.DataRepository
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var dataRepository: DataRepository

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private lateinit var notificationManager: NotificationManager

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.e("logujeme","Service OnCreate")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(2)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            maxWaitTime = TimeUnit.MINUTES.toMillis(3)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                scope.launch { dataRepository.insertLocation(
                    Location(
                        time = locationResult.lastLocation.time,
                        latitude = locationResult.lastLocation.latitude,
                        longitude = locationResult.lastLocation.longitude,
                        accuracy = locationResult.lastLocation.accuracy,
                        source = locationResult.lastLocation.provider
                    )
                ) }
                Log.e("location", locationResult.toString())
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("logujeme","Service OnStartCommand")
        startForeground(12345,createNotification())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.getMainLooper())
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Log.e("logujeme","Service onDestroy")
        stopForeground(true)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun createNotification(): Notification {
        Log.e("logujeme","Service notification")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "location_notification",
                    "SygicFencak",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(applicationContext, "location_notification")
            .setContentTitle("tracking your training")
            .setContentText("SygicFencak")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

}