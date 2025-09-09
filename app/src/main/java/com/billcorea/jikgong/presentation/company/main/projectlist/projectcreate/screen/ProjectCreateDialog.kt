package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.ui.theme.AppTypography
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ProjectCreateData(
    val title: String = "",
    val location: String = "",
    val locationDetail: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val startDate: String = "",
    val endDate: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCreateDialog(
    onDismiss: () -> Unit,
    onConfirm: (ProjectCreateData) -> Unit
) {
    var projectData by remember { mutableStateOf(ProjectCreateData()) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var datePickerType by remember { mutableStateOf("") } // "start" or "end"
    var showLocationDialog by remember { mutableStateOf(false) }
    
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val dialogHeight = screenHeight * 0.8f // 화면 높이의 80%
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // 전체 너비 사용
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f) // 화면 너비의 95%
                .heightIn(max = dialogHeight)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 헤더
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF4B7BFF),
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "새 프로젝트 등록",
                            style = AppTypography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "닫기",
                                tint = Color.White
                            )
                        }
                    }
                }
                
                // 컨텐츠
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 프로젝트 이름
                    OutlinedTextField(
                        value = projectData.title,
                        onValueChange = { projectData = projectData.copy(title = it) },
                        label = { Text("프로젝트 이름 *") },
                        placeholder = { Text("예: 강남 오피스텔 신축 공사") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Work,
                                contentDescription = null,
                                tint = Color(0xFF4B7BFF)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4B7BFF),
                            focusedLabelColor = Color(0xFF4B7BFF)
                        )
                    )
                    
                    // 착공일
                    OutlinedTextField(
                        value = projectData.startDate,
                        onValueChange = { },
                        label = { Text("착공일 *") },
                        placeholder = { Text("날짜를 선택하세요") },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF4B7BFF)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    datePickerType = "start"
                                    showDatePickerDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "날짜 선택",
                                    tint = Color(0xFF4B7BFF)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                datePickerType = "start"
                                showDatePickerDialog = true
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4B7BFF),
                            focusedLabelColor = Color(0xFF4B7BFF)
                        )
                    )
                    
                    // 준공일
                    OutlinedTextField(
                        value = projectData.endDate,
                        onValueChange = { },
                        label = { Text("준공일 *") },
                        placeholder = { Text("날짜를 선택하세요") },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = null,
                                tint = Color(0xFF4B7BFF)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    datePickerType = "end"
                                    showDatePickerDialog = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "날짜 선택",
                                    tint = Color(0xFF4B7BFF)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                datePickerType = "end"
                                showDatePickerDialog = true
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4B7BFF),
                            focusedLabelColor = Color(0xFF4B7BFF)
                        )
                    )
                    
                    // 작업장소
                    OutlinedTextField(
                        value = projectData.location,
                        onValueChange = { },
                        label = { Text("작업장소 *") },
                        placeholder = { Text("지도에서 선택하세요") },
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF4B7BFF)
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { showLocationDialog = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Map,
                                    contentDescription = "지도 선택",
                                    tint = Color(0xFF4B7BFF)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showLocationDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4B7BFF),
                            focusedLabelColor = Color(0xFF4B7BFF)
                        )
                    )
                    
                    // 상세 주소 (선택)
                    if (projectData.location.isNotEmpty()) {
                        OutlinedTextField(
                            value = projectData.locationDetail,
                            onValueChange = { projectData = projectData.copy(locationDetail = it) },
                            label = { Text("상세 주소 (선택)") },
                            placeholder = { Text("예: 101동 지하 1층") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                
                    // 버튼
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                width = 2.dp
                            )
                        ) {
                            Text(
                                "취소",
                                style = AppTypography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        Button(
                            onClick = { 
                                if (projectData.title.isNotEmpty() && 
                                    projectData.startDate.isNotEmpty() && 
                                    projectData.endDate.isNotEmpty() && 
                                    projectData.location.isNotEmpty()) {
                                    onConfirm(projectData)
                                }
                            },
                            enabled = projectData.title.isNotEmpty() && 
                                     projectData.startDate.isNotEmpty() && 
                                     projectData.endDate.isNotEmpty() && 
                                     projectData.location.isNotEmpty(),
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4B7BFF)
                            )
                        ) {
                            Text(
                                "등록하기",
                                style = AppTypography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
    
    // 날짜 선택 다이얼로그
    if (showDatePickerDialog) {
        DatePickerDialog(
            onDateSelected = { date ->
                if (datePickerType == "start") {
                    projectData = projectData.copy(startDate = date)
                } else {
                    projectData = projectData.copy(endDate = date)
                }
                showDatePickerDialog = false
            },
            onDismiss = { showDatePickerDialog = false }
        )
    }
    
    // 위치 선택 다이얼로그
    if (showLocationDialog) {
        LocationPickerDialog(
            onLocationSelected = { location, lat, lng ->
                projectData = projectData.copy(
                    location = location,
                    latitude = lat,
                    longitude = lng
                )
                showLocationDialog = false
            },
            onDismiss = { showLocationDialog = false }
        )
    }
}

// 날짜 선택 다이얼로그
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = LocalDate.ofEpochDay(millis / 86400000)
                        onDateSelected(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    }
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

// 위치 선택 다이얼로그 (간단한 버전 - 실제로는 카카오맵 API 연동 필요)
@Composable
fun LocationPickerDialog(
    onLocationSelected: (String, Double, Double) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 헤더
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF4B7BFF))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "작업장소 선택",
                            style = AppTypography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onDismiss) {
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
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("주소를 검색하세요") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp)
                )
                
                // 지도 영역 (임시 - 실제로는 KakaoMap 컴포넌트)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color.LightGray, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Map,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "지도 API 연동 필요",
                            style = AppTypography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                }
                
                // 선택 버튼
                Button(
                    onClick = {
                        // 임시 데이터
                        onLocationSelected(
                            "서울특별시 강남구 역삼동 123-45",
                            37.5665,
                            126.9780
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B7BFF)
                    )
                ) {
                    Text(
                        "이 위치로 선택",
                        style = AppTypography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}