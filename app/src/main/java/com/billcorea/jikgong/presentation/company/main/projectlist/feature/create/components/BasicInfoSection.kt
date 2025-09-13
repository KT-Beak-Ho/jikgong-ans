package com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.model.BasicJobInfo
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.model.predefinedJobCategories
import java.text.NumberFormat
import java.util.Locale

/**
 * 기본 정보 입력 섹션
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BasicInfoSection(
    basicInfo: BasicJobInfo,
    onProjectTitleChange: (String) -> Unit,
    onJobTypeToggle: (String) -> Unit,
    onRecruitmentCountChange: (String) -> Unit,
    onDailyWageChange: (String) -> Unit,
    onLocationClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 섹션 타이틀
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Work,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "기본 정보",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // 프로젝트 제목
            OutlinedTextField(
                value = basicInfo.projectTitle,
                onValueChange = onProjectTitleChange,
                label = { Text("프로젝트 제목 *") },
                placeholder = { Text("예: 아파트 신축공사 철근공 모집") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    focusedLabelColor = Color(0xFF4B7BFF)
                )
            )
            
            // 직종 선택
            Column {
                Text(
                    text = "직종 선택 * (복수 선택 가능)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    predefinedJobCategories.forEach { jobType ->
                        JobTypeChip(
                            jobType = jobType,
                            isSelected = basicInfo.selectedJobTypes.contains(jobType),
                            onClick = { onJobTypeToggle(jobType) }
                        )
                    }
                }
            }
            
            // 모집 인원 & 일급
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 모집 인원
                OutlinedTextField(
                    value = basicInfo.recruitmentCount,
                    onValueChange = onRecruitmentCountChange,
                    label = { Text("모집 인원 *") },
                    placeholder = { Text("0") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    suffix = { Text("명") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4B7BFF),
                        focusedLabelColor = Color(0xFF4B7BFF)
                    )
                )
                
                // 일급
                OutlinedTextField(
                    value = formatWage(basicInfo.dailyWage),
                    onValueChange = { value ->
                        // 콤마 제거 후 숫자만 전달
                        val cleanValue = value.replace(",", "").replace("원", "").trim()
                        onDailyWageChange(cleanValue)
                    },
                    label = { Text("일급 *") },
                    placeholder = { Text("0") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    suffix = { Text("원") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4B7BFF),
                        focusedLabelColor = Color(0xFF4B7BFF)
                    )
                )
            }
            
            // 작업 위치
            OutlinedTextField(
                value = basicInfo.workLocation,
                onValueChange = { /* 읽기 전용 */ },
                label = { Text("작업 위치") },
                placeholder = { Text("지도에서 선택하세요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLocationClick() },
                enabled = false,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = onLocationClick) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "위치 선택",
                            tint = Color(0xFF4B7BFF)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Gray,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = Color.Gray,
                    disabledTrailingIconColor = Color(0xFF4B7BFF)
                )
            )
        }
    }
}

/**
 * 직종 선택 칩
 */
@Composable
private fun JobTypeChip(
    jobType: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF4B7BFF) else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color(0xFF4B7BFF) else Color.LightGray
        ),
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = jobType,
                color = if (isSelected) Color.White else Color.Black,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

/**
 * 금액 포맷팅 함수
 */
private fun formatWage(wage: String): String {
    return if (wage.isNotEmpty() && wage.all { it.isDigit() }) {
        val formatter = NumberFormat.getNumberInstance(Locale.KOREA)
        formatter.format(wage.toLong())
    } else {
        wage
    }
}