package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSampleData
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSummary
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

@Composable
fun ProjectPaymentSummaryCard(
    summary: ProjectPaymentSummary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 헤더 - 제목과 이번 달 총액
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "임금 관리 현황",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "이번 달 지급액",
                        style = AppTypography.bodyMedium,
                        color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(summary.totalAmount)}원",
                    style = AppTypography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 절감 혜택 강조 섹션 (service fee benefits removed from data structure)

            Spacer(modifier = Modifier.height(20.dp))

            // 통계 그리드
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 지급 대기
                SummaryStatItem(
                    title = "지급 대기",
                    count = summary.pendingPayments,
                    amount = summary.pendingAmount,
                    icon = Icons.Default.Pending,
                    color = Color(0xFFFFA726),
                    modifier = Modifier.weight(1f)
                )

                // 지급 완료
                SummaryStatItem(
                    title = "지급 완료",
                    count = summary.completedPayments,
                    amount = summary.paidAmount,
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF66BB6A),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 전체 현장
                SummaryStatItem(
                    title = "전체 현장",
                    count = summary.totalProjects,
                    amount = summary.totalAmount,
                    icon = Icons.Default.Assignment,
                    color = appColorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                // 지급 완료액
                SummaryStatItem(
                    title = "지급 완료액",
                    count = null,
                    amount = summary.paidAmount,
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SavingsBenefitSection(
    totalSavings: Long,
    averageSavingsPerProject: Long,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF4CAF50).copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 아이콘
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF4CAF50)
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "절감",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(6.dp)
                )
            }

            // 절감 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "직직직 수수료 혜택",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF4CAF50)
                )

                Text(
                    text = "기존 대비 50% 절감",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }

            // 절감액
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "총 절감액",
                    style = AppTypography.labelSmall,
                    color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )

                Text(
                    text = "+${NumberFormat.getNumberInstance(Locale.KOREA).format(totalSavings)}원",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
private fun SummaryStatItem(
    title: String,
    count: Int?,
    amount: Long,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = appColorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = AppTypography.labelMedium,
                color = appColorScheme.onSurfaceVariant
            )

            if (count != null) {
                Text(
                    text = "${count}건",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )
            }

            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원",
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectPaymentSummaryCardPreview() {
    Jikgong1111Theme {
        ProjectPaymentSummaryCard(
            summary = ProjectPaymentSampleData.getSampleProjectPaymentSummary(),
            modifier = Modifier.padding(16.dp)
        )
    }
}