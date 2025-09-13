package com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.model.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * 작업 일정 섹션
 */
@Composable
fun WorkScheduleSection(
    workSchedule: WorkSchedule,
    onDateToggle: (LocalDate) -> Unit,
    onStartTimeClick: () -> Unit,
    onEndTimeClick: () -> Unit,
    onBreakTimeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "작업 일정",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // 선택된 날짜 표시
            if (workSchedule.selectedDates.isNotEmpty()) {
                Text(
                    text = "선택된 날짜: ${workSchedule.selectedDates.size}일",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B7BFF)
                )
            }
            
            // 시간 선택
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onStartTimeClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("시작: ${workSchedule.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                }
                
                OutlinedButton(
                    onClick = onEndTimeClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("종료: ${workSchedule.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
                }
            }
        }
    }
}

/**
 * 작업 조건 섹션
 */
@Composable
fun WorkConditionsSection(
    workConditions: WorkConditions,
    onEnvironmentChange: (String) -> Unit,
    onPickupToggle: () -> Unit,
    onPickupLocationChange: (String) -> Unit,
    onMealToggle: () -> Unit,
    onMealDescriptionChange: (String) -> Unit,
    onParkingOptionChange: (ParkingOption) -> Unit,
    onParkingDescriptionChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "작업 조건",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // 작업 환경
            OutlinedTextField(
                value = workConditions.workEnvironment,
                onValueChange = onEnvironmentChange,
                label = { Text("작업 환경") },
                placeholder = { Text("예: 실내 작업, 안전장비 착용 필수") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    focusedLabelColor = Color(0xFF4B7BFF)
                )
            )
            
            // 픽업 제공
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("픽업 제공", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = workConditions.pickupProvided,
                    onCheckedChange = { onPickupToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4B7BFF)
                    )
                )
            }
            
            // 식사 제공
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("식사 제공", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = workConditions.mealProvided,
                    onCheckedChange = { onMealToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF4B7BFF)
                    )
                )
            }
        }
    }
}

/**
 * 연락처 정보 섹션
 */
@Composable
fun ContactInfoSection(
    contactInfo: ContactInfo,
    onManagerNameChange: (String) -> Unit,
    onContactNumberChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "연락처 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            OutlinedTextField(
                value = contactInfo.managerName,
                onValueChange = onManagerNameChange,
                label = { Text("담당자 이름 *") },
                placeholder = { Text("홍길동") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    focusedLabelColor = Color(0xFF4B7BFF)
                )
            )
            
            OutlinedTextField(
                value = contactInfo.contactNumber,
                onValueChange = onContactNumberChange,
                label = { Text("연락처 *") },
                placeholder = { Text("010-0000-0000") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    focusedLabelColor = Color(0xFF4B7BFF)
                )
            )
        }
    }
}

/**
 * 추가 정보 섹션
 */
@Composable
fun AdditionalInfoSection(
    additionalInfo: AdditionalInfo,
    onRequirementsChange: (String) -> Unit,
    onUrgentToggle: () -> Unit,
    onUrgentReasonChange: (String) -> Unit,
    onPhotoAdd: (String) -> Unit,
    onPhotoRemove: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "추가 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            OutlinedTextField(
                value = additionalInfo.requirements,
                onValueChange = onRequirementsChange,
                label = { Text("준비물") },
                placeholder = { Text("예: 신분증, 안전화, 작업복") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    focusedLabelColor = Color(0xFF4B7BFF)
                )
            )
            
            // 긴급 모집
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("긴급 모집", style = MaterialTheme.typography.bodyLarge)
                }
                Switch(
                    checked = additionalInfo.isUrgent,
                    onCheckedChange = { onUrgentToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFFFFA000)
                    )
                )
            }
        }
    }
}

/**
 * 시간 선택 다이얼로그
 */
@Composable
fun TimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    // 간단한 구현 (실제로는 더 복잡한 TimePicker 사용)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("시간 선택") },
        text = {
            Text("현재 시간: ${initialTime.format(DateTimeFormatter.ofPattern("HH:mm"))}")
        },
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(initialTime)
                onDismiss()
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

/**
 * 긴급 모집 사유 다이얼로그
 */
@Composable
fun UrgentRecruitmentDialog(
    urgentReason: String,
    onReasonChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFFA000)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("긴급 모집 사유")
            }
        },
        text = {
            OutlinedTextField(
                value = urgentReason,
                onValueChange = onReasonChange,
                placeholder = { Text("긴급 모집 사유를 입력하세요") },
                minLines = 3
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}