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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
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
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CompletedPaymentDetailDialog(
    project: ProjectPaymentData,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 600.dp),
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
                // 헤더
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
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "완료",
                                tint = Color(0xFF22C55E),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "입금 완료",
                                style = AppTypography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF1A1D29)
                            )
                        }
                        Text(
                            text = "모든 작업자에게 지급이 완료되었습니다",
                            style = AppTypography.bodyMedium,
                            color = Color(0xFF6B7280)
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

                // 프로젝트 정보 요약
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F9FF)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF0EA5E9).copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = project.projectTitle,
                            style = AppTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "총 지급 금액",
                                    style = AppTypography.bodyMedium,
                                    color = Color(0xFF6B7280)
                                )
                                Text(
                                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원",
                                    style = AppTypography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF0EA5E9)
                                )
                            }
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "지급 완료일",
                                    style = AppTypography.bodyMedium,
                                    color = Color(0xFF6B7280)
                                )
                                Text(
                                    text = project.completedAt?.format(
                                        DateTimeFormatter.ofPattern("MM월 dd일")
                                    ) ?: "오늘",
                                    style = AppTypography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF1A1D29)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 지급 완료된 작업자 목록
                Text(
                    text = "💼 지급 완료된 작업자 (${project.workers.size}명)",
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
                        CompletedWorkerPaymentInfo(worker = worker)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 보관함 이동 안내
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF8DC)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "시간",
                            tint = Color(0xFFB8860B),
                            modifier = Modifier.size(20.dp)
                        )
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "보관함 이동 안내",
                                style = AppTypography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFFB8860B)
                            )
                            Text(
                                text = "이 프로젝트는 일주일 후에 보관함으로 자동 이동됩니다",
                                style = AppTypography.bodySmall,
                                color = Color(0xFF8B7355)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 확인 버튼
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22C55E)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "확인",
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

@Composable
private fun CompletedWorkerPaymentInfo(
    worker: ProjectPaymentData.WorkerPaymentInfo
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0FDF4)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = BorderStroke(1.dp, Color(0xFF22C55E).copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 완료 상태 및 워커 기본 정보
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
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "완료",
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "사람",
                        tint = Color(0xFF4B7BFF),
                        modifier = Modifier.size(18.dp)
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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 지급 완료된 계좌 정보
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(1.dp, Color(0xFF22C55E).copy(alpha = 0.3f))
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
                        tint = Color(0xFF22C55E),
                        modifier = Modifier.size(18.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${worker.bankName} ${worker.accountNumber}",
                            style = AppTypography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF065F46)
                        )
                        Text(
                            text = "지급 완료",
                            style = AppTypography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF22C55E)
                        )
                    }
                    
                    // 지급 완료 시간
                    worker.paidAt?.let { paidAt ->
                        Text(
                            text = paidAt.format(
                                DateTimeFormatter.ofPattern("HH:mm")
                            ),
                            style = AppTypography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun CompletedPaymentDetailDialogPreview() {
    val mockProject = CompanyMockDataFactory.getProjectPayments()
        .first { it.status == ProjectPaymentStatus.COMPLETED }
    
    Jikgong1111Theme {
        CompletedPaymentDetailDialog(
            project = mockProject,
            onDismiss = {}
        )
    }
}