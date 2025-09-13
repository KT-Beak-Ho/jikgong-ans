package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 위치 선택 다이얼로그
 * 지도 API 연동 전 UI 구현
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onLocationSelected: (LocationData) -> Unit
) {
    if (!isVisible) return
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LocationData?>(null) }
    val focusManager = LocalFocusManager.current
    
    // 샘플 검색 결과
    val searchResults = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            emptyList()
        } else {
            sampleLocationResults.filter { 
                it.address.contains(searchQuery, ignoreCase = true) 
            }
        }
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 헤더
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF4B7BFF)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "작업장소 선택",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "닫기",
                                tint = Color.White
                            )
                        }
                    }
                }
                
                // 검색바
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("주소를 검색하세요") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "검색",
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { searchQuery = "" }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "지우기",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                
                // 지도 영역 (추후 실제 지도로 교체)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "검색 결과가 없습니다",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    } else if (searchResults.isNotEmpty()) {
                        // 검색 결과 목록
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "검색 결과",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            
                            searchResults.forEach { location ->
                                LocationResultCard(
                                    location = location,
                                    isSelected = selectedLocation?.address == location.address,
                                    onClick = {
                                        selectedLocation = location
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color(0xFF4B7BFF)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "지도 API 연동 예정",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "주소를 검색하거나\n지도에서 위치를 선택하세요",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                
                // 선택된 주소 표시
                if (selectedLocation != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0F7FF)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "선택된 위치",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4B7BFF),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedLocation!!.address,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (selectedLocation!!.detailAddress.isNotEmpty()) {
                                Text(
                                    text = selectedLocation!!.detailAddress,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
                
                // 하단 버튼
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("취소")
                    }
                    
                    Button(
                        onClick = {
                            selectedLocation?.let {
                                onLocationSelected(it)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = selectedLocation != null,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B7BFF)
                        )
                    ) {
                        Text("이 위치로 선택")
                    }
                }
            }
        }
    }
}

/**
 * 검색 결과 카드
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocationResultCard(
    location: LocationData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE8F0FF) else Color.White
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF4B7BFF))
            )
        } else {
            CardDefaults.outlinedCardBorder()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isSelected) Color(0xFF4B7BFF) else Color.Gray
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = location.address,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                if (location.detailAddress.isNotEmpty()) {
                    Text(
                        text = location.detailAddress,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

/**
 * 위치 데이터 클래스
 */
data class LocationData(
    val address: String,
    val detailAddress: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

// 샘플 데이터
private val sampleLocationResults = listOf(
    LocationData(
        address = "서울특별시 강남구 테헤란로 123",
        detailAddress = "강남빌딩 5층",
        latitude = 37.5012,
        longitude = 127.0396
    ),
    LocationData(
        address = "서울특별시 강남구 역삼동 825-2",
        detailAddress = "",
        latitude = 37.4979,
        longitude = 127.0276
    ),
    LocationData(
        address = "서울특별시 서초구 서초대로 398",
        detailAddress = "서초타워 B동",
        latitude = 37.4955,
        longitude = 127.0281
    )
)

@Preview(showBackground = true)
@Composable
fun LocationPickerDialogPreview() {
    MaterialTheme {
        LocationPickerDialog(
            isVisible = true,
            onDismiss = {},
            onLocationSelected = {}
        )
    }
}