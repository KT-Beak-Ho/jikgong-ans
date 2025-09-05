package com.billcorea.jikgong.presentation.company.main.money.popup

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
fun ProjectListDialog(
    title: String,
    projects: List<ProjectPaymentData>,
    onDismiss: () -> Unit,
    actionButtonText: String,
    onProjectAction: (ProjectPaymentData) -> Unit
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
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = title,
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = "총 ${projects.size}건",
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
                        ProjectListItem(
                            project = project,
                            actionButtonText = actionButtonText,
                            onActionClick = { onProjectAction(project) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun ProjectListItem(
    project: ProjectPaymentData,
    actionButtonText: String,
    onActionClick: () -> Unit
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.projectTitle,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "총 금액: ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalAmount)}원",
                    style = AppTypography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
                
                Text(
                    text = "대상자: ${project.workers.size}명 • ${project.location}",
                    style = AppTypography.bodySmall,
                    color = Color(0xFF9CA3AF)
                )
            }
            
            Button(
                onClick = onActionClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B7BFF)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = actionButtonText,
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ProjectListDialogPreview() {
    val mockProjects = CompanyMockDataFactory.getProjectPayments()
        .filter { it.status == ProjectPaymentStatus.PENDING }
    
    Jikgong1111Theme {
        ProjectListDialog(
            title = "전체 현장",
            projects = mockProjects,
            onDismiss = {},
            actionButtonText = "상세보기",
            onProjectAction = {}
        )
    }
}