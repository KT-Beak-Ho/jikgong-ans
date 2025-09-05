package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFFFBFCFF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // 헤더: 프로젝트 정보와 확장/축소 아이콘
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = projectPayment.projectTitle,
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = AppTypography.titleLarge.lineHeight * 1.1
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = projectPayment.location,
                        style = AppTypography.titleMedium,
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
                        tint = appColorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 총 결제금액 및 작업자 수 표시
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "총 결제금액",
                        style = AppTypography.bodyLarge,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(projectPayment.totalAmount)}원",
                        style = AppTypography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.primary
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "작업자",
                        style = AppTypography.bodyLarge,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${projectPayment.completedWorkers}/${projectPayment.totalWorkers}명",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface
                    )
                }
            }

            // 작업 완료 메시지 (notes field removed from data structure)

            Spacer(modifier = Modifier.height(16.dp))

            // 직직직 혜택 강조 카드 (service fee benefits removed from data structure)

            Spacer(modifier = Modifier.height(20.dp))

            // 입금 버튼
            DepositButton(
                status = projectPayment.status,
                totalAmount = projectPayment.totalAmount,
                onDepositClick = { 
                    if (projectPayment.status == com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus.COMPLETED) {
                        onPaymentAction(projectPayment, "view_completed")
                    } else {
                        onPaymentAction(projectPayment, "deposit")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // 입금 완료 상태일 때 보관함 안내문구 표시
            if (projectPayment.status == com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus.COMPLETED) {
                Spacer(modifier = Modifier.height(12.dp))
                
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = androidx.compose.ui.graphics.Color(0xFFF0F9FF)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "시간",
                            tint = androidx.compose.ui.graphics.Color(0xFF0EA5E9),
                            modifier = Modifier.size(16.dp)
                        )
                        
                        Text(
                            text = "일주일 후에 보관함으로 이동됩니다",
                            style = AppTypography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = androidx.compose.ui.graphics.Color(0xFF0C4A6E)
                        )
                    }
                }
            }

            // 확장된 상세 정보
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(20.dp))

                    // 구분선
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = appColorScheme.outline.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )

                    // 상세 혜택 정보 (service fee benefits removed from data structure)

                    Spacer(modifier = Modifier.height(20.dp))

                    // 작업자 목록 헤더
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "작업자별 상세 내역",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = appColorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${projectPayment.completedWorkers}/${projectPayment.totalWorkers} 지급완료",
                                style = AppTypography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = appColorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 작업자 목록
                    projectPayment.workers.forEach { worker ->
                        WorkerPaymentItem(
                            worker = worker,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JikgongBenefitHighlightCard(
    originalServiceFee: Long,
    currentServiceFee: Long,
    totalSavings: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50).copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 혜택 아이콘
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
            ) {
                Text(
                    text = "직직직",
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            // 혜택 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "수수료 50% 절감 혜택",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                )

                Text(
                    text = "기존 ${NumberFormat.getNumberInstance(Locale.KOREA).format(originalServiceFee)}원 → ${NumberFormat.getNumberInstance(Locale.KOREA).format(currentServiceFee)}원",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            // 절감액
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "절감액",
                    style = AppTypography.labelSmall,
                    color = appColorScheme.onSurfaceVariant
                )

                Text(
                    text = "+${NumberFormat.getNumberInstance(Locale.KOREA).format(totalSavings)}원",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
private fun DetailedBenefitCard(
    originalServiceFee: Long,
    currentServiceFee: Long,
    totalSavings: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFF4CAF50).copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "직직직 혜택 상세 내역",
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 기존 vs 현재 비교
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ComparisonItem(
                    title = "기존 인력사무소",
                    subtitle = "수수료 10%",
                    amount = originalServiceFee,
                    isOld = true
                )

                ComparisonItem(
                    title = "직직직 플랫폼",
                    subtitle = "수수료 5%",
                    amount = currentServiceFee,
                    isOld = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 절감 혜택 요약
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = androidx.compose.ui.graphics.Color(0xFF4CAF50).copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "이번 현장 총 절감액",
                            style = AppTypography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface
                        )

                        Text(
                            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(totalSavings)}원",
                            style = AppTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = androidx.compose.ui.graphics.Color(0xFF4CAF50)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "월 10개 현장 기준 약 ${NumberFormat.getNumberInstance(Locale.KOREA).format(totalSavings * 10)}원 절감 효과",
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonItem(
    title: String,
    subtitle: String,
    amount: Long,
    isOld: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = AppTypography.bodyMedium,
            color = appColorScheme.onSurfaceVariant
        )

        Text(
            text = subtitle,
            style = AppTypography.labelSmall,
            color = appColorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원",
            style = AppTypography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = if (isOld) appColorScheme.error else androidx.compose.ui.graphics.Color(0xFF4CAF50)
        )
    }
}

@Composable
private fun WorkerPaymentItem(
    worker: ProjectPaymentData.WorkerPaymentInfo,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = appColorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = worker.workerName,
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${worker.jobType} • ${worker.hoursWorked}시간 근무",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(worker.totalAmount)}원",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 개별 상태 표시
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (worker.isPaid) {
                        androidx.compose.ui.graphics.Color(0xFF4CAF50).copy(alpha = 0.15f)
                    } else {
                        androidx.compose.ui.graphics.Color(0xFFFFA726).copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = if (worker.isPaid) "완료" else "대기",
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = if (worker.isPaid) {
                            androidx.compose.ui.graphics.Color(0xFF4CAF50)
                        } else {
                            androidx.compose.ui.graphics.Color(0xFFFFA726)
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
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
            projectPayment = CompanyMockDataFactory.getProjectPayments().first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}