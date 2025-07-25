package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentData
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSampleData
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectPaymentCard(
    projectPayment: ProjectPaymentData,
    onPaymentAction: (ProjectPaymentData, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 헤더: 프로젝트 정보와 확장/축소 아이콘
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = projectPayment.projectTitle,
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = projectPayment.projectLocation,
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "접기" else "펼치기",
                        tint = appColorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 총 결제금액 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "총 결제금액",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )

                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(projectPayment.totalAmount)}원",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.primary
                )
            }

            // 작업 완료 메시지
            if (projectPayment.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = projectPayment.notes,
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 수수료 절감 표시 (하이라이트 처리)
            SavingsHighlightCard(
                originalServiceFee = projectPayment.originalServiceFee,
                currentServiceFee = projectPayment.currentServiceFee,
                totalSavings = projectPayment.totalSavings,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 입금 버튼
            DepositButton(
                status = projectPayment.status,
                totalAmount = projectPayment.totalAmount,
                onDepositClick = { onPaymentAction(projectPayment, "deposit") },
                modifier = Modifier.fillMaxWidth()
            )

            // 확장된 상세 정보
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))

                    // 구분선
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = appColorScheme.outline.copy(alpha = 0.2f)
                    )

                    // 상세 수수료 절감 정보
                    DetailedSavingsCard(
                        originalServiceFee = projectPayment.originalServiceFee,
                        currentServiceFee = projectPayment.currentServiceFee,
                        totalSavings = projectPayment.totalSavings
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 작업자 목록 헤더
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "작업자 목록",
                            style = AppTypography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface
                        )

                        Text(
                            text = "${projectPayment.completedWorkers}/${projectPayment.totalWorkers} 지급완료",
                            style = AppTypography.bodySmall,
                            color = appColorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 작업자 목록
                    projectPayment.workers.forEach { worker ->
                        WorkerPaymentItem(
                            worker = worker,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkerPaymentItem(
    worker: com.billcorea.jikgong.presentation.company.main.money.data.WorkerPaymentInfo,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = appColorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = worker.workerName,
                    style = AppTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = appColorScheme.onSurface
                )
                Text(
                    text = "${worker.jobType} • ${worker.workHours}시간",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(worker.finalAmount)}원",
                    style = AppTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = appColorScheme.onSurface
                )

                // 개별 상태 표시
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (worker.status) {
                        ProjectPaymentStatus.PENDING -> appColorScheme.secondary.copy(alpha = 0.1f)
                        ProjectPaymentStatus.PROCESSING -> appColorScheme.primary.copy(alpha = 0.1f)
                        ProjectPaymentStatus.COMPLETED -> appColorScheme.tertiary.copy(alpha = 0.1f)
                        ProjectPaymentStatus.FAILED -> appColorScheme.error.copy(alpha = 0.1f)
                    }
                ) {
                    Text(
                        text = when (worker.status) {
                            ProjectPaymentStatus.PENDING -> "대기"
                            ProjectPaymentStatus.PROCESSING -> "처리중"
                            ProjectPaymentStatus.COMPLETED -> "완료"
                            ProjectPaymentStatus.FAILED -> "실패"
                        },
                        style = AppTypography.labelSmall,
                        color = when (worker.status) {
                            ProjectPaymentStatus.PENDING -> appColorScheme.secondary
                            ProjectPaymentStatus.PROCESSING -> appColorScheme.primary
                            ProjectPaymentStatus.COMPLETED -> appColorScheme.tertiary
                            ProjectPaymentStatus.FAILED -> appColorScheme.error
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectPaymentCardPreview() {
    Jikgong1111Theme {
        ProjectPaymentCard(
            projectPayment = ProjectPaymentSampleData.getSampleProjectPayments().first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}