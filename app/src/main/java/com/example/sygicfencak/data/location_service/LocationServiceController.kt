package com.example.sygicfencak.data.location_service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

fun runLocationService(run: Boolean, context: Context) {
    val intent = Intent(context, LocationService::class.java)
    if (run) {
        Log.e("logujeme","RunLocationService zapina")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    } else if (!run) {
        Log.e("logujeme","RunLocationService vypina")
        context.stopService(intent)
    }
}