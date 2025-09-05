package com.billcorea.jikgong.presentation.company.main.money.popup

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
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // 헤더
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "지급 확인",
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = "아래 계좌로 입금을 완료하시고 완료 버튼을 눌러주세요",
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

                // 프로젝트 정보
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = project.projectTitle,
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "지급 예정 금액: ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원",
                            style = AppTypography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color(0xFF4B7BFF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 노동자 목록
                Text(
                    text = "지급 대상자 (${project.workers.size}명)",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = project.workers,
                        key = { it.workerId }
                    ) { worker ->
                        WorkerPaymentInfo(worker = worker)
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
                        onClick = { onConfirmPayment(project) },
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