package com.example.sygicfencak.presentation.map


import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
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

    Box(Modifier.fillMaxSize()) {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(1.35, 103.87), 10f)
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

        Column() {
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { startTracking.value=true }) {

            }
            Button(onClick = { viewModel.stopLocationTracking() }) {

            }

            LazyColumn(){
                items(viewModel.locationData.value){ location ->
                    Text(text = location.latitude.toString())
                    Spacer(modifier = Modifier.height(10.dp))
                }
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
