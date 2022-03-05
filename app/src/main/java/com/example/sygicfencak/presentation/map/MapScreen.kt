package com.example.sygicfencak.presentation.map


import android.Manifest
import androidx.compose.runtime.*
import com.example.sygicfencak.utils.FeatureWhichRequirePermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@ExperimentalPermissionsApi
@Composable
fun MapScreen(){
    FeatureWhichRequirePermission(
        permissions = listOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION),
        basicDialogText = "Training tracking requires location permission",
        rationaleDialogText = "We cannot track your training without knowing your location",
        onlySomePermissionGrantedText = "Thank you for access to your approximate location "+
                "but we need precise location for training tracking")
}
