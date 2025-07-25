package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

@Composable
fun SavingsIndicator(
    originalAmount: Long,
    savedAmount: Long,
    modifier: Modifier = Modifier,
    showPercentage: Boolean = true
) {
    val savingsPercentage = if (originalAmount > 0) {
        (savedAmount.toFloat() / originalAmount.toFloat()) * 100
    } else 0f

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF4CAF50).copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.TrendingDown,
                contentDescription = "절감",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = "수수료 50% 절감",
                style = AppTypography.labelSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF4CAF50)
            )

            Text(
                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(savedAmount)}원 절약",
                style = AppTypography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun DetailedSavingsCard(
    originalServiceFee: Long,
    currentServiceFee: Long,
    totalSavings: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50).copy(alpha = 0.05f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "직직직 수수료 혜택",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF4CAF50)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF4CAF50)
                ) {
                    Text(
                        text = "50% 절감",
                        style = AppTypography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 기존 vs 현재 비교
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "기존 수수료 (10%)",
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(originalServiceFee)}원",
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = appColorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "직직직 수수료 (5%)",
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(currentServiceFee)}원",
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                color = appColorScheme.outline.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "총 절감액",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )

                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(totalSavings)}원",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SavingsIndicatorPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SavingsIndicator(
                originalAmount = 51000L,
                savedAmount = 25500L
            )

            DetailedSavingsCard(
                originalServiceFee = 51000L,
                currentServiceFee = 25500L,
                totalSavings = 25500L
            )
        }
    }
}