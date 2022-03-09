package com.example.sygicfencak.data.location_service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

fun runLocationService(run: Boolean, context: Context) {
    val intent = Intent(context, LocationService::class.java)
    if (run) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    } else if (!run) {
        context.stopService(intent)
    }
}