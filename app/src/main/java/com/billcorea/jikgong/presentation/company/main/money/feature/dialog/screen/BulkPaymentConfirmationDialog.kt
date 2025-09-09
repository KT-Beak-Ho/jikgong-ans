package com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.text.NumberFormat
import java.util.*

@Composable
fun BulkPaymentConfirmationDialog(
    projects: List<ProjectPaymentData>,
    onDismiss: () -> Unit,
    onConfirmBulkPayment: (List<ProjectPaymentData>) -> Unit
) {
    val totalAmount = projects.sumOf { it.totalAmount }
    val totalWorkers = projects.sumOf { it.workers.size }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 헤더
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "전체 지급 확인",
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = "아래 모든 계좌로 입금을 완료하시고 지급 완료 버튼을 눌러주세요",
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

                // 전체 지급 요약
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4B7BFF).copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "총 지급 예정액",
                            style = AppTypography.titleMedium,
                            color = Color(0xFF6B7280)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(totalAmount)}원",
                            style = AppTypography.displaySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF4B7BFF)
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "${projects.size}개 프로젝트 • 총 ${totalWorkers}명",
                            style = AppTypography.bodyMedium,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 프로젝트별 상세 내역
                Text(
                    text = "지급 상세 내역",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 프로젝트 목록
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = projects,
                        key = { it.id }
                    ) { project ->
                        BulkPaymentProjectItem(project = project)
                    }
                }

                // 액션 버튼들
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
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
                        onClick = { onConfirmBulkPayment(projects) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B7BFF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "지급 완료",
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

@Composable
private fun BulkPaymentProjectItem(
    project: ProjectPaymentData
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFBFCFF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 프로젝트 제목
            Text(
                text = project.projectTitle,
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1A1D29)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 프로젝트 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.company,
                        style = AppTypography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                    Text(
                        text = project.location,
                        style = AppTypography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF4B7BFF)
                    )
                    Text(
                        text = "${project.workers.size}명",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 작업자 계좌 정보 (상세 표시)
            if (project.workers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "입금 계좌 정보:",
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF374151)
                )
                
                Spacer(modifier = Modifier.height(6.dp))
                
                project.workers.forEach { worker ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = worker.workerName,
                                style = AppTypography.bodySmall.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "${worker.bankName} ${worker.accountNumber}",
                                style = AppTypography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                        }
                        
                        Text(
                            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(worker.totalAmount)}원",
                            style = AppTypography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF059669)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun BulkPaymentConfirmationDialogPreview() {
    val mockProjects = CompanyMockDataFactory.getProjectPayments()
        .filter { it.status == ProjectPaymentStatus.PENDING }
    
    Jikgong1111Theme {
        BulkPaymentConfirmationDialog(
            projects = mockProjects,
            onDismiss = {},
            onConfirmBulkPayment = {}
        )
    }
}