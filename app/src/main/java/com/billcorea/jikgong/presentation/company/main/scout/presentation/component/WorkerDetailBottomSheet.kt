package com.billcorea.jikgong.presentation.company.main.scout.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Worker
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.text.NumberFormat
import java.util.*

// 금액 형식화 함수들
private fun formatWageDisplay(amount: Int): String {
    return "일당 ${amount}원"
}

private fun formatNumberWithCommas(number: Int): String {
    return NumberFormat.getNumberInstance(Locale.KOREA).format(number)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerDetailBottomSheet(
    worker: Worker,
    onDismiss: () -> Unit,
    onScoutClick: (wage: String) -> Unit
) {
    var proposedWageAmount by remember { mutableStateOf(150000) } // 기본값 15만원
    var showConfirmDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        contentColor = Color.Black,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 헤더 - 닫기 버튼과 제목
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "인력 상세정보",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                        tint = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            // 노동자 기본 정보 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = worker.name,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "${worker.jobTypes.joinToString(", ")} · ${worker.distance}km",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }

                // 평점과 가능 상태
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF0F0F0)
                    ) {
                        Text(
                            text = "⭐ ${worker.rating}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (worker.isAvailable) Color(0xFF4B7BFF).copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = if (worker.isAvailable) "작업 가능" else "진행중",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (worker.isAvailable) Color(0xFF4B7BFF) else Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 상세 정보 섹션
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF8F9FA)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "상세 정보",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WorkerInfoItem(
                            title = "경력",
                            content = "${worker.experience}년",
                            modifier = Modifier.weight(1f)
                        )
                        WorkerInfoItem(
                            title = "완료 프로젝트",
                            content = "${worker.completedProjects}건",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        WorkerInfoItem(
                            title = "키",
                            content = "175cm", // Mock data
                            modifier = Modifier.weight(1f)
                        )
                        WorkerInfoItem(
                            title = "체형",
                            content = "보통", // Mock data
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    WorkerInfoItem(
                        title = "희망 일당",
                        content = worker.desiredWage ?: "협의 가능"
                    )
                    
                    WorkerInfoItem(
                        title = "가능한 근무일",
                        content = "월, 화, 수, 금 (주 4일)"
                    )
                    
                    WorkerInfoItem(
                        title = "근무 가능 시간",
                        content = "06:00 ~ 18:00"
                    )
                    
                    WorkerInfoItem(
                        title = "자기소개",
                        content = worker.introduction ?: "등록된 소개가 없습니다."
                    )
                    
                    // 보유 자격증
                    if (worker.certificates.isNotEmpty()) {
                        WorkerInfoItem(
                            title = "보유 자격증",
                            content = worker.certificates.joinToString(", ")
                        )
                    }
                    
                    WorkerInfoItem(
                        title = "특기사항",
                        content = "안전교육 이수 완료, 응급처치 자격증 보유"
                    )
                    
                    WorkerInfoItem(
                        title = "최근 프로젝트",
                        content = "○○아파트 신축공사 (2024.11 ~ 2024.12)"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 스카웃 제안 섹션
            if (worker.isAvailable) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4B7BFF).copy(alpha = 0.05f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "스카웃 제안",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // 금액 스테퍼 컨트롤
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "제안 일당",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = formatNumberWithCommas(proposedWageAmount) + "원",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF4B7BFF)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // 스테퍼 컨트롤
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // -1만원 버튼
                                    WageStepperButton(
                                        icon = Icons.Default.Remove,
                                        label = "-1만원",
                                        enabled = proposedWageAmount >= 20000,
                                        onClick = { proposedWageAmount = (proposedWageAmount - 10000).coerceAtLeast(10000) }
                                    )
                                    
                                    // -1천원 버튼
                                    WageStepperButton(
                                        icon = Icons.Default.Remove,
                                        label = "-1천원",
                                        enabled = proposedWageAmount > 10000,
                                        onClick = { proposedWageAmount = (proposedWageAmount - 1000).coerceAtLeast(10000) }
                                    )
                                    
                                    // +1천원 버튼
                                    WageStepperButton(
                                        icon = Icons.Default.Add,
                                        label = "+1천원",
                                        enabled = proposedWageAmount < 500000,
                                        onClick = { proposedWageAmount = (proposedWageAmount + 1000).coerceAtMost(500000) }
                                    )
                                    
                                    // +1만원 버튼
                                    WageStepperButton(
                                        icon = Icons.Default.Add,
                                        label = "+1만원",
                                        enabled = proposedWageAmount <= 490000,
                                        onClick = { proposedWageAmount = (proposedWageAmount + 10000).coerceAtMost(500000) }
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = formatWageDisplay(proposedWageAmount),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                showConfirmDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4B7BFF)
                            )
                        ) {
                            Text(
                                text = "스카웃 제안하기",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            } else {
                // 진행중인 경우
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Gray.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "현재 다른 현장에서 작업 중입니다",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // 확인 다이얼로그
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "스카웃 제안 확인",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("다음 조건으로 스카웃 제안을 보내시겠습니까?")
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("• 노동자: ${worker.name}")
                    Text("• 제안 일당: ${formatWageDisplay(proposedWageAmount)}")
                    Text("• 직종: ${worker.jobTypes.joinToString(", ")}")
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        "📝 상세 조건은 나중에 협의할 수 있습니다.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B7BFF)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("취소", color = Color.Gray)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onScoutClick(formatWageDisplay(proposedWageAmount))
                        onDismiss()
                    }
                ) {
                    Text(
                        "스카웃 제안",
                        color = Color(0xFF4B7BFF),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}

@Composable
private fun WageStepperButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (enabled) Color(0xFF4B7BFF).copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (enabled) Color(0xFF4B7BFF) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (enabled) Color.Black else Color.Gray
        )
    }
}

@Composable
private fun WorkerInfoItem(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerDetailBottomSheetPreview() {
    Jikgong1111Theme {
        WorkerDetailBottomSheet(
            worker = Worker(
                id = "1",
                name = "김철수",
                jobTypes = listOf("철근공", "형틀목공"),
                experience = 5,
                distance = 0.8,
                rating = 4.8f,
                introduction = "성실하고 꼼꼼한 작업을 약속드립니다.",
                desiredWage = "일당 18만원",
                isAvailable = true,
                completedProjects = 52,
                certificates = listOf("건설기계조종사", "용접기능사")
            ),
            onDismiss = {},
            onScoutClick = {}
        )
    }
}