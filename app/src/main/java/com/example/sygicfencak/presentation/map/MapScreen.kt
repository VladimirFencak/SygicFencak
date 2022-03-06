package com.example.sygicfencak.presentation.map


import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.sygicfencak.utils.FeatureWhichRequirePermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@ExperimentalPermissionsApi
@Composable
fun MapScreen(){
    Box(Modifier.fillMaxSize()) {
        val singapore = LatLng(1.35, 103.87)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                position = singapore,
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
        FeatureWhichRequirePermission(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            basicDialogText = "Training tracking requires location permission",
            rationaleDialogText = "We cannot track your training without knowing your location",
            onlySomePermissionGrantedText = "Thank you for access to your approximate location " +
                    "but we need precise location for training tracking"
        )
    }
}
