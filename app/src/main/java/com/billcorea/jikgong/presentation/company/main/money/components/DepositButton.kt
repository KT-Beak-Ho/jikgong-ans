package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

// 내부에서만 사용하는 버튼 설정 클래스
private data class ButtonConfig(
    val text: String,
    val subText: String,
    val backgroundColor: Color,
    val contentColor: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val enabled: Boolean
)

@Composable
fun DepositButton(
    status: ProjectPaymentStatus,
    totalAmount: Long,
    onDepositClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val buttonConfig = when (status) {
        ProjectPaymentStatus.PENDING -> {
            ButtonConfig(
                text = "임금 입금하기",
                subText = "2일 이내 지급 완료",
                backgroundColor = Color(0xFF2E3A59),
                contentColor = Color.White,
                icon = Icons.Default.AccountBalance,
                enabled = true
            )
        }
        ProjectPaymentStatus.PROCESSING -> {
            ButtonConfig(
                text = "입금 처리중",
                subText = "처리 완료까지 약 1-2시간",
                backgroundColor = appColorScheme.primary.copy(alpha = 0.6f),
                contentColor = Color.White,
                icon = Icons.Default.Schedule,
                enabled = false
            )
        }
        ProjectPaymentStatus.COMPLETED -> {
            ButtonConfig(
                text = "입금 완료",
                subText = "지급이 완료되었습니다",
                backgroundColor = appColorScheme.surfaceVariant,
                contentColor = appColorScheme.onSurfaceVariant,
                icon = Icons.Default.Check,
                enabled = false
            )
        }
        ProjectPaymentStatus.FAILED -> {
            ButtonConfig(
                text = "입금 재시도",
                subText = "입금에 실패했습니다",
                backgroundColor = Color(0xFFEF5350),
                contentColor = Color.White,
                icon = Icons.Default.Refresh,
                enabled = true
            )
        }
    }

    Button(
        onClick = onDepositClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled && buttonConfig.enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonConfig.backgroundColor,
            contentColor = buttonConfig.contentColor,
            disabledContainerColor = appColorScheme.surfaceVariant,
            disabledContentColor = appColorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (status == ProjectPaymentStatus.COMPLETED) 0.dp else 2.dp
        ),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = buttonConfig.icon,
                contentDescription = buttonConfig.text,
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = buttonConfig.text,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = buttonConfig.subText,
                    style = AppTypography.bodySmall,
                    color = buttonConfig.contentColor.copy(alpha = 0.8f)
                )
            }

            // 금액 표시 (완료 상태가 아닌 경우)
            if (status != ProjectPaymentStatus.COMPLETED) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(totalAmount)}원",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun QuickDepositButton(
    amount: Long,
    onDepositClick: () -> Unit,
    modifier: Modifier = Modifier,
    isProcessing: Boolean = false
) {
    Button(
        onClick = onDepositClick,
        modifier = modifier,
        enabled = !isProcessing,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        if (isProcessing) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "즉시 입금",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "즉시 입금",
                    style = AppTypography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DepositButtonPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DepositButton(
                status = ProjectPaymentStatus.PENDING,
                totalAmount = 510000L,
                onDepositClick = {}
            )

            DepositButton(
                status = ProjectPaymentStatus.PROCESSING,
                totalAmount = 300000L,
                onDepositClick = {}
            )

            DepositButton(
                status = ProjectPaymentStatus.COMPLETED,
                totalAmount = 210000L,
                onDepositClick = {}
            )

            DepositButton(
                status = ProjectPaymentStatus.FAILED,
                totalAmount = 150000L,
                onDepositClick = {}
            )

            QuickDepositButton(
                amount = 123500L,
                onDepositClick = {}
            )
        }
    }
}