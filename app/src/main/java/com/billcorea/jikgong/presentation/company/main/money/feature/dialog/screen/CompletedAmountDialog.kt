package com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen

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
fun CompletedAmountDialog(
    completedProjects: List<ProjectPaymentData>,
    monthlySavings: Long,
    onDismiss: () -> Unit
) {
    val totalCompletedAmount = completedProjects.sumOf { it.paidAmount }
    
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "직직직 혜택 상세",
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = "인건비 절약 현황",
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

                // 총액 및 절약액 표시
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "총 인건비 절약액",
                            style = AppTypography.titleMedium,
                            color = Color(0xFF6B7280)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(totalCompletedAmount * 0.15)}원",
                            style = AppTypography.displaySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF4CAF50)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "기존 중개업체 수수료 대비 15% 절약\n이번 달 추가 절약: +${NumberFormat.getNumberInstance(Locale.KOREA).format(monthlySavings)}원",
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 완료된 프로젝트 목록
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = completedProjects,
                        key = { it.id }
                    ) { project ->
                        CompletedProjectItem(project = project)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun CompletedProjectItem(
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
            Text(
                text = project.projectTitle,
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1A1D29)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "절약액: ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.paidAmount * 0.15)}원",
                        style = AppTypography.bodyMedium,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        text = "지급액: ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.paidAmount)}원 • ${project.workers.size}명",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "완료",
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF4CAF50)
                    )
                    if (project.completedAt != null) {
                        Text(
                            text = "${project.completedAt.monthValue}/${project.completedAt.dayOfMonth}",
                            style = AppTypography.bodySmall,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun CompletedAmountDialogPreview() {
    val mockCompletedProjects = CompanyMockDataFactory.getProjectPayments()
        .filter { it.status == ProjectPaymentStatus.COMPLETED }
    
    Jikgong1111Theme {
        CompletedAmountDialog(
            completedProjects = mockCompletedProjects,
            monthlySavings = 850000L,
            onDismiss = {}
        )
    }
}