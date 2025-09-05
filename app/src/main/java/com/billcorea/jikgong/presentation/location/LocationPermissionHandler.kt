package com.billcorea.jikgong.presentation.location

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.R
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )

    when {
        locationPermissionsState.allPermissionsGranted -> {
            onPermissionGranted()
            content()
        }
        locationPermissionsState.shouldShowRationale -> {
            LocationPermissionRationale(
                onRequestPermission = { locationPermissionsState.launchMultiplePermissionRequest() },
                onPermissionDenied = onPermissionDenied
            )
        }
        else -> {
            LocationPermissionRationale(
                onRequestPermission = { locationPermissionsState.launchMultiplePermissionRequest() },
                onPermissionDenied = onPermissionDenied
            )
        }
    }
}

@Composable
private fun LocationPermissionRationale(
    onRequestPermission: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_mylocation_v2),
                contentDescription = "위치 권한",
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF4B7BFF)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "위치 권한이 필요합니다",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "지도에서 현재 위치를 확인하고\n주변 정보를 검색하기 위해\n위치 권한이 필요합니다.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPermissionDenied,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("취소")
                }
                
                Button(
                    onClick = onRequestPermission,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B7BFF)
                    )
                ) {
                    Text("권한 허용")
                }
            }
        }
    }
}