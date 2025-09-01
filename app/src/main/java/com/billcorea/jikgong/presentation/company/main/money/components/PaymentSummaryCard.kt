package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentSummary
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

@Composable
fun PaymentSummaryCard(
    summary: PaymentSummary,
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
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "임금 관리 현황",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onPrimaryContainer
                )

                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "임금 관리",
                    tint = appColorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 통계 그리드
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 대기중인 지급
                SummaryItem(
                    title = "지급 대기",
                    count = summary.pendingCount,
                    amount = summary.pendingAmount,
                    icon = Icons.Default.Pending,
                    color = Color(0xFFFFA726),
                    modifier = Modifier.weight(1f)
                )

                // 완료된 지급
                SummaryItem(
                    title = "지급 완료",
                    count = summary.completedCount,
                    amount = summary.completedAmount,
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
                // 전체 건수
                SummaryItem(
                    title = "전체 건수",
                    count = summary.totalPayments,
                    amount = summary.totalAmount,
                    icon = Icons.Default.Assignment,
                    color = appColorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                // 이번 주 지급액
                SummaryItem(
                    title = "주간 지급액",
                    count = null,
                    amount = summary.weeklyTotal,
                    icon = Icons.Default.AttachMoney,
                    color = Color(0xFF42A5F5),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
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
fun PaymentSummaryCardPreview() {
    Jikgong1111Theme {
        PaymentSummaryCard(
            summary = CompanyMockDataFactory.getSamplePaymentSummary(),
            modifier = Modifier.padding(16.dp)
        )
    }
}