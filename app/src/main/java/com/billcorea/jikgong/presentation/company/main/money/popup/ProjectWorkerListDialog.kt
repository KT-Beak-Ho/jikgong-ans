package com.billcorea.jikgong.presentation.company.main.money.popup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
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
fun ProjectWorkerListDialog(
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
                .fillMaxHeight(0.8f),
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
                            text = "프로젝트 상세",
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = project.projectTitle,
                            style = AppTypography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
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

                // 프로젝트 정보 카드
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "위치",
                                    style = AppTypography.labelMedium,
                                    color = Color(0xFF6B7280)
                                )
                                Text(
                                    text = project.location,
                                    style = AppTypography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color(0xFF1A1D29)
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "총 인건비",
                                    style = AppTypography.labelMedium,
                                    color = Color(0xFF6B7280)
                                )
                                Text(
                                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원",
                                    style = AppTypography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF4B7BFF)
                                )
                            }
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "상태",
                                    style = AppTypography.labelMedium,
                                    color = Color(0xFF6B7280)
                                )
                                val statusText = when (project.status) {
                                    ProjectPaymentStatus.PENDING -> "지급 대기"
                                    ProjectPaymentStatus.PROCESSING -> "처리중"
                                    ProjectPaymentStatus.COMPLETED -> "지급 완료"
                                    ProjectPaymentStatus.FAILED -> "지급 실패"
                                    ProjectPaymentStatus.OVERDUE -> "연체"
                                }
                                val statusColor = when (project.status) {
                                    ProjectPaymentStatus.PENDING -> Color(0xFFFF9800)
                                    ProjectPaymentStatus.PROCESSING -> Color(0xFF2196F3)
                                    ProjectPaymentStatus.COMPLETED -> Color(0xFF4CAF50)
                                    ProjectPaymentStatus.FAILED -> Color(0xFFF44336)
                                    ProjectPaymentStatus.OVERDUE -> Color(0xFFE91E63)
                                }
                                Text(
                                    text = statusText,
                                    style = AppTypography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = statusColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 노동자 목록 헤더
                Text(
                    text = "참여 노동자 (${project.workers.size}명)",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 노동자 목록
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = project.workers,
                        key = { it.workerId }
                    ) { worker ->
                        WorkerListItem(worker = worker, projectStatus = project.status)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun WorkerListItem(
    worker: ProjectPaymentData.WorkerPaymentInfo,
    projectStatus: ProjectPaymentStatus
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 아이콘
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF4B7BFF).copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "노동자",
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier
                        .size(24.dp)
                        .padding(8.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = worker.workerName,
                    style = AppTypography.titleMedium.copy(
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
                
                if (projectStatus == ProjectPaymentStatus.COMPLETED && worker.paidAt != null) {
                    Text(
                        text = "지급일: ${worker.paidAt!!.monthValue}월 ${worker.paidAt!!.dayOfMonth}일",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(worker.totalAmount)}원",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = when (projectStatus) {
                        ProjectPaymentStatus.COMPLETED -> Color(0xFF4CAF50)
                        ProjectPaymentStatus.PENDING -> Color(0xFFFF9800)
                        else -> Color(0xFF6B7280)
                    }
                )
                
                val statusText = when (projectStatus) {
                    ProjectPaymentStatus.COMPLETED -> "지급 완료"
                    ProjectPaymentStatus.PENDING -> "지급 대기"
                    ProjectPaymentStatus.PROCESSING -> "처리중"
                    ProjectPaymentStatus.FAILED -> "지급 실패"
                    ProjectPaymentStatus.OVERDUE -> "연체"
                }
                
                Text(
                    text = statusText,
                    style = AppTypography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = when (projectStatus) {
                        ProjectPaymentStatus.COMPLETED -> Color(0xFF4CAF50)
                        ProjectPaymentStatus.PENDING -> Color(0xFFFF9800)
                        ProjectPaymentStatus.PROCESSING -> Color(0xFF2196F3)
                        ProjectPaymentStatus.FAILED -> Color(0xFFF44336)
                        ProjectPaymentStatus.OVERDUE -> Color(0xFFE91E63)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ProjectWorkerListDialogPreview() {
    val mockProject = CompanyMockDataFactory.getProjectPayments().first()
    
    Jikgong1111Theme {
        ProjectWorkerListDialog(
            project = mockProject,
            onDismiss = {}
        )
    }
}