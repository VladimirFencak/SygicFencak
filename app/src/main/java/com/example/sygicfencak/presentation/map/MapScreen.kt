package com.example.sygicfencak.presentation.map


import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@ExperimentalPermissionsApi
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val startTracking = remember { mutableStateOf(false) }
    val isTracking = remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(
                LatLng(
                    viewModel.locationData.value.firstOrNull()?.latitude ?: 48.148598,
                    viewModel.locationData.value.firstOrNull()?.latitude ?: 17.107748
                ), 10f
            )
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            viewModel.locationData.value.forEach { location ->
                Marker(
                    position = LatLng(location.latitude, location.longitude),
                    title = location.time.toString(),
                    snippet = location.latitude.toString()
                )
                Polyline(points = viewModel.polylineData)
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
        ) {
            if (!isTracking.value) {
                Button(
                    onClick = {
                        startTracking.value = true
                    },
                    modifier = Modifier.padding(8.dp),
                    elevation = ButtonDefaults.elevation(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),

                    ) {
                    Text(text = "Start tracking")
                }
            } else {
                Button(
                    onClick = {
                        viewModel.stopLocationTracking()
                        isTracking.value = false
                    },
                    modifier = Modifier.padding(8.dp),
                    elevation = ButtonDefaults.elevation(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),

                    ) {
                    Text(text = "Stop tracking")
                }
            }
        }
    }

    //check permissions and start tracking
    if (startTracking.value) {
        if (permissionsState.allPermissionsGranted) {
            viewModel.startLocationTracking()
            isTracking.value = true
            startTracking.value = false
        } else {
            val allPermissionsRevoked =
                permissionsState.permissions.size == permissionsState.revokedPermissions.size

            val textToShow: String = if (!allPermissionsRevoked) {
                "Thank you for access to your approximate location " +
                        "but we need precise location for training tracking"
            } else if (permissionsState.shouldShowRationale) {
                "We cannot track your training without knowing your location"
            } else {
                "Training tracking requires location permission"
            }

            AlertDialog(
                onDismissRequest = { startTracking.value = false },
                text = {
                    Text(textToShow)
                },
                confirmButton = {
                    TextButton(onClick = {
                        permissionsState.launchMultiplePermissionRequest()
                        startTracking.value = false
                    })
                    { Text(text = "OK") }
                },
            )
        }
    }
}

