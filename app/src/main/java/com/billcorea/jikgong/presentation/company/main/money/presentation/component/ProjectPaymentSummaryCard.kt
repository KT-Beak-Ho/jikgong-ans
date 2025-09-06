package com.billcorea.jikgong.presentation.company.main.money.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentSummary
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

// 금액 포맷팅 함수
private fun formatAmount(amount: Long): String {
    return when {
        amount >= 100000000 -> "${String.format("%.1f", amount / 100000000.0)}억원"
        amount >= 10000 -> "${String.format("%.0f", amount / 10000.0)}만원"
        else -> "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원"
    }
}

@Composable
fun ProjectPaymentSummaryCard(
    summary: ProjectPaymentSummary,
    onPendingPaymentsClick: () -> Unit = {},
    onCompletedPaymentsClick: () -> Unit = {},
    onAllProjectsClick: () -> Unit = {},
    onCompletedAmountClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color(0xFFFBFCFF)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬 추가
        ) {
            // 헤더 - 제목만
            Text(
                text = "이때까지 절약한 총 금액",
                style = AppTypography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = androidx.compose.ui.graphics.Color(0xFF1A1D29)
            )

            Spacer(modifier = Modifier.height(16.dp))
            
            // 총 절약 금액을 메인으로 크게 표시
            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(summary.totalSavingsAmount)}원",
                style = AppTypography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFF4CAF50)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 절약 설명
            Text(
                text = "기존 중개업체 수수료 대비 절약",
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = androidx.compose.ui.graphics.Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 통계 그리드
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 바로지급하기 버튼
                ClickableSummaryStatItem(
                    title = "바로지급하기",
                    count = summary.pendingPayments,
                    amount = summary.pendingAmount,
                    icon = Icons.Default.Pending,
                    color = Color(0xFFFF5722), // 더 눈에 띄는 주황색
                    onClick = onPendingPaymentsClick,
                    modifier = Modifier.weight(1f),
                    isActionButton = true
                )

                // 지급 완료
                ClickableSummaryStatItem(
                    title = "지급 완료",
                    count = summary.completedPayments,
                    amount = summary.paidAmount,
                    icon = Icons.Default.CheckCircle,
                    color = Color(0xFF66BB6A),
                    onClick = onCompletedPaymentsClick,
                    modifier = Modifier.weight(1f),
                    isActionButton = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 전체 현장
                ClickableSummaryStatItem(
                    title = "전체 현장",
                    count = summary.totalProjects,
                    amount = summary.totalAmount,
                    icon = Icons.Default.Assignment,
                    color = appColorScheme.primary,
                    onClick = onAllProjectsClick,
                    modifier = Modifier.weight(1f),
                    isActionButton = true
                )

                // 이번달 절약한 금액
                ClickableSummaryStatItem(
                    title = "이번달 절약금액",
                    count = null,
                    amount = summary.monthlySavingsAmount,
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF4CAF50),
                    onClick = onCompletedAmountClick,
                    modifier = Modifier.weight(1f),
                    isActionButton = true
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
private fun TossStyleStatItem(
    title: String,
    count: Int?,
    amount: Long,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp), // 토스 스타일 높이
        shape = RoundedCornerShape(16.dp),
        color = androidx.compose.ui.graphics.Color.White,
        shadowElevation = 0.5.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = 0.5.dp,
            color = androidx.compose.ui.graphics.Color(0xFFE5E7EB)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 상단: 제목
            Text(
                text = title,
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = (-0.2).sp
                ),
                color = androidx.compose.ui.graphics.Color(0xFF6B7280),
                maxLines = 1
            )
            
            // 하단: 수치 정보
            Column {
                if (count != null) {
                    Text(
                        text = "${count}건",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.3).sp
                        ),
                        color = androidx.compose.ui.graphics.Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                
                Text(
                    text = if (amount >= 10000000) {
                        "${String.format("%.1f", amount / 10000000.0)}억원"
                    } else if (amount >= 10000) {
                        "${String.format("%.0f", amount / 10000.0)}만원"
                    } else {
                        "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원"
                    },
                    style = AppTypography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp
                    ),
                    color = color,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun SummaryStatItemWithSavings(
    title: String,
    amount: Long,
    monthlySavings: Long,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(120.dp), // 고정 높이로 모든 상자 크기 동일하게
        shape = RoundedCornerShape(12.dp),
        color = androidx.compose.ui.graphics.Color(0xFFF1F3F9),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), // 패딩 약간 축소
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly // 수직 간격 균등 배치
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )

            Text(
                text = title,
                style = AppTypography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF374151)
            )

            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원",
                style = AppTypography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = color
            )
            
            // 총 절약 금액 표시
            Text(
                text = "총 절약 +${NumberFormat.getNumberInstance(Locale.KOREA).format(monthlySavings)}원",
                style = AppTypography.labelSmall,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
private fun ClickableSummaryStatItem(
    title: String,
    count: Int?,
    amount: Long,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActionButton: Boolean = false
) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() }, // 클릭 가능
        shape = RoundedCornerShape(12.dp),
        color = if (isActionButton) Color.White else Color.White,
        shadowElevation = if (isActionButton) 6.dp else 4.dp,
        border = if (isActionButton) BorderStroke(
            width = 2.dp,
            color = color
        ) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = title,
                style = AppTypography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF374151),
                maxLines = 1
            )

            if (count != null) {
                // 건수와 금액을 한 줄에 표시
                Text(
                    text = "${count}건 ${formatAmount(amount)}",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = color,
                    maxLines = 1
                )
            } else {
                Text(
                    text = formatAmount(amount),
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = color,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ClickableSummaryStatItemWithSavings(
    title: String,
    amount: Long,
    monthlySavings: Long,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(
            width = 2.dp,
            color = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )

            Text(
                text = title,
                style = AppTypography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF374151)
            )

            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원",
                style = AppTypography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = color
            )
            
            // 총 절약 금액 표시
            Text(
                text = "총 절약 +${NumberFormat.getNumberInstance(Locale.KOREA).format(monthlySavings)}원",
                style = AppTypography.labelSmall,
                color = Color(0xFF4CAF50)
            )
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
        modifier = modifier.height(120.dp), // 고정 높이로 모든 상자 크기 동일하게
        shape = RoundedCornerShape(12.dp),
        color = androidx.compose.ui.graphics.Color(0xFFF1F3F9),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), // 패딩 약간 축소
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly // 수직 간격 균등 배치
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(28.dp)
            )

            Text(
                text = title,
                style = AppTypography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF374151)
            )

            if (count != null) {
                Text(
                    text = "${count}건",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1F2937)
                )
            }

            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원",
                style = AppTypography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
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
            summary = CompanyMockDataFactory.getProjectPaymentSummary(),
            modifier = Modifier.padding(16.dp)
        )
    }
}