package com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.AppTypography
import java.text.NumberFormat
import java.util.*

@Composable
fun PaymentConfirmationDialog(
    project: ProjectPaymentData,
    onDismiss: () -> Unit,
    onConfirmPayment: (ProjectPaymentData) -> Unit
) {
    var showConfirmationStep by remember { mutableStateOf(false) }
    
    // 연체 상태 확인
    val isOverdue = project.status == com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus.OVERDUE
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 700.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // 헤더 - 상태에 따른 색상 및 아이콘 변경
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isOverdue) Icons.Default.Warning else Icons.Default.AccountBalance,
                                contentDescription = if (isOverdue) "경고" else "입금",
                                tint = if (isOverdue) Color(0xFFD32F2F) else Color(0xFF4B7BFF),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = if (!showConfirmationStep) {
                                    if (isOverdue) "연체 임금 지급하기" else "💰 임금 지급하기"
                                } else "입금 완료 확인",
                                style = AppTypography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isOverdue) Color(0xFFD32F2F) else Color(0xFF1A1D29)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (!showConfirmationStep) {
                                if (isOverdue) 
                                    "지급 기한이 경과하여 즉시 처리가 필요합니다. 서비스 신뢰도에 영향이 갈 수 있습니다." 
                                else 
                                    "아래 계좌 정보를 확인하고 입금을 진행해주세요"
                            } else "입금이 완료되었는지 확인해주세요",
                            style = AppTypography.bodyMedium,
                            color = if (isOverdue) Color(0xFFD32F2F).copy(alpha = 0.8f) else Color(0xFF6B7280)
                        )
                    }
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (!showConfirmationStep) {
                    // 프로젝트 정보 - 상태에 따른 색상 변경
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isOverdue) 
                                Color(0xFFFFF5F5) 
                            else 
                                Color(0xFFF0F9FF)
                        ),
                        border = BorderStroke(
                            1.dp, 
                            if (isOverdue) 
                                Color(0xFFD32F2F).copy(alpha = 0.2f) 
                            else 
                                Color(0xFF0EA5E9).copy(alpha = 0.2f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Work,
                                    contentDescription = "프로젝트",
                                    tint = if (isOverdue) Color(0xFFD32F2F) else Color(0xFF0EA5E9),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = project.projectTitle,
                                    style = AppTypography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF1A1D29)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "총 지급 금액",
                                        style = AppTypography.bodyLarge,
                                        color = Color(0xFF6B7280)
                                    )
                                    Text(
                                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원",
                                        style = AppTypography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = if (isOverdue) Color(0xFFD32F2F) else Color(0xFF0EA5E9)
                                    )
                                }
                                
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "지급 대상자",
                                        style = AppTypography.bodyLarge,
                                        color = Color(0xFF6B7280)
                                    )
                                    Text(
                                        text = "${project.workers.size}명",
                                        style = AppTypography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color(0xFF1A1D29)
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // 현장 위치 정보 추가
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "위치",
                                    tint = Color(0xFF6B7280),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = project.location,
                                    style = AppTypography.bodyMedium,
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 노동자 목록
                    Text(
                        text = "💼 지급 대상자 상세 정보",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        project.workers.forEach { worker ->
                            EnhancedWorkerPaymentInfo(worker = worker)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 액션 버튼들
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF6B7280)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "취소",
                                style = AppTypography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Button(
                            onClick = { showConfirmationStep = true },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOverdue) Color(0xFFD32F2F) else Color(0xFF4B7BFF)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isOverdue) Icons.Default.Warning else Icons.Default.AccountBalance,
                                    contentDescription = "입금",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isOverdue) "긴급 입금" else "입금하기",
                                    style = AppTypography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // 입금 완료 확인 단계 - 상태에 따른 색상 변경
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isOverdue) Color(0xFFFEF2F2) else Color(0xFFFFF8DC)
                        ),
                        border = BorderStroke(1.dp, if (isOverdue) Color(0xFFD32F2F) else Color(0xFFFFD700))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "확인",
                                tint = if (isOverdue) Color(0xFFD32F2F) else Color(0xFFB8860B),
                                modifier = Modifier.size(32.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = if (isOverdue) "🚨 긴급 입금 완료 확인" else "⚠️ 입금 완료 확인",
                                style = AppTypography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isOverdue) Color(0xFFD32F2F) else Color(0xFFB8860B),
                                textAlign = TextAlign.Center
                            )
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text(
                                text = if (isOverdue) {
                                    "정말로 ${project.workers.size}명의 노동자에게\n총 ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원을\n긴급 입금 완료하셨습니까?"
                                } else {
                                    "정말로 ${project.workers.size}명의 노동자에게\n총 ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원을\n입금 완료하셨습니까?"
                                },
                                style = AppTypography.bodyLarge,
                                color = if (isOverdue) Color(0xFFB91C1C) else Color(0xFF8B7355),
                                textAlign = TextAlign.Center,
                                lineHeight = AppTypography.bodyLarge.lineHeight * 1.4
                            )
                            
                            if (isOverdue) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "⚡ 연체 처리로 서비스 신뢰도 복구가 중요합니다",
                                    style = AppTypography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color(0xFFD32F2F),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 확인 단계 액션 버튼들
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showConfirmationStep = false },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF6B7280)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "아니요",
                                style = AppTypography.titleMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Button(
                            onClick = { onConfirmPayment(project) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOverdue) Color(0xFFD32F2F) else Color(0xFF22C55E)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "완료",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isOverdue) "긴급 처리 완료" else "예, 완료했습니다",
                                    style = AppTypography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedWorkerPaymentInfo(
    worker: ProjectPaymentData.WorkerPaymentInfo
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 워커 기본 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "사람",
                        tint = Color(0xFF4B7BFF),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = worker.workerName,
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )
                }
                
                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(worker.totalAmount)}원",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF22C55E)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 직업 정보
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "직업",
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = worker.jobType,
                    style = AppTypography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "• ${worker.hoursWorked}시간 작업",
                    style = AppTypography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 계좌 정보 (중요 정보이므로 강조)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F9FF)
                ),
                border = BorderStroke(1.dp, Color(0xFF0EA5E9).copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "은행",
                        tint = Color(0xFF0EA5E9),
                        modifier = Modifier.size(18.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = worker.bankName,
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color(0xFF0C4A6E)
                        )
                        Text(
                            text = worker.accountNumber,
                            style = AppTypography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF0C4A6E)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkerPaymentInfo(
    worker: ProjectPaymentData.WorkerPaymentInfo
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = worker.workerName,
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${worker.bankName} ${worker.accountNumber}",
                    style = AppTypography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }
            
            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(worker.totalAmount)}원",
                style = AppTypography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun PaymentConfirmationDialogPreview() {
    val mockProject = CompanyMockDataFactory.getProjectPayments()
        .first { it.status == ProjectPaymentStatus.PENDING }
    
    Jikgong1111Theme {
        PaymentConfirmationDialog(
            project = mockProject,
            onDismiss = {},
            onConfirmPayment = {}
        )
    }
}