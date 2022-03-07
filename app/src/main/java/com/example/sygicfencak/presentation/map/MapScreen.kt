package com.example.sygicfencak.presentation.map


import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

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

        Column() {
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { startTracking.value=true }) {

            }
            Button(onClick = { viewModel.stopLocationTracking() }) {

            }
        }

        if (startTracking.value) {
            if (permissionsState.allPermissionsGranted) {
                Text("access granted")
                viewModel.startLocationTracking()
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
}
