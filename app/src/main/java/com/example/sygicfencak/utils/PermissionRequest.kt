package com.example.sygicfencak.utils

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@ExperimentalPermissionsApi
@Composable
fun FeatureWhichRequirePermission(
    permissions: List<String>,
    basicDialogText: String,
    rationaleDialogText: String,
    onlySomePermissionGrantedText: String?,
    //onPermissionsGranted: () -> Unit
) {
    val permissionsState = rememberMultiplePermissionsState(permissions)
    val checkPermission = remember { mutableStateOf(false) }

    Button(onClick = { checkPermission.value = true }) {
        Text(text = "ahoj")
    }
    if (checkPermission.value) {
        if (permissionsState.allPermissionsGranted) {
            Text("access granted")
            //onPermissionGranted
        } else {
            val allPermissionsRevoked =
                permissionsState.permissions.size == permissionsState.revokedPermissions.size

            val textToShow: String = if (!allPermissionsRevoked) {
                onlySomePermissionGrantedText ?: basicDialogText
            } else if (permissionsState.shouldShowRationale) {
                rationaleDialogText
            } else {
                basicDialogText
            }

            AlertDialog(
                onDismissRequest = { checkPermission.value = false },
                text = {
                    Text(textToShow)
                },
                confirmButton = {
                    TextButton(onClick = {
                        permissionsState.launchMultiplePermissionRequest()
                        checkPermission.value = false
                    })
                    { Text(text = "OK") }
                },
            )
        }
    }
}