package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.network.models.ProjectPaymentStatus
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
    val enabled: Boolean,
    val showAmount: Boolean = true
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
                subText = "2일 이내 지급 완료 보장",
                backgroundColor = Color(0xFF2E3A59),
                contentColor = Color.White,
                icon = Icons.Default.AccountBalance,
                enabled = true,
                showAmount = true
            )
        }
        ProjectPaymentStatus.PROCESSING -> {
            ButtonConfig(
                text = "입금 처리중",
                subText = "처리 완료까지 약 1-2시간 소요",
                backgroundColor = appColorScheme.primary.copy(alpha = 0.7f),
                contentColor = Color.White,
                icon = Icons.Default.Schedule,
                enabled = false,
                showAmount = true
            )
        }
        ProjectPaymentStatus.COMPLETED -> {
            ButtonConfig(
                text = "입금 완료",
                subText = "모든 작업자에게 지급이 완료되었습니다",
                backgroundColor = appColorScheme.surfaceVariant,
                contentColor = appColorScheme.onSurfaceVariant,
                icon = Icons.Default.Check,
                enabled = false,
                showAmount = false
            )
        }
        ProjectPaymentStatus.FAILED -> {
            ButtonConfig(
                text = "입금 재시도",
                subText = "입금 실패 - 다시 시도해주세요",
                backgroundColor = Color(0xFFEF5350),
                contentColor = Color.White,
                icon = Icons.Default.Refresh,
                enabled = true,
                showAmount = true
            )
        }
        ProjectPaymentStatus.OVERDUE -> {
            ButtonConfig(
                text = "연체 임금 지급",
                subText = "지급 기한이 지났습니다 - 즉시 처리 필요",
                backgroundColor = Color(0xFFD32F2F),
                contentColor = Color.White,
                icon = Icons.Default.Warning,
                enabled = true,
                showAmount = true
            )
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = buttonConfig.backgroundColor,
            contentColor = buttonConfig.contentColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (status == ProjectPaymentStatus.COMPLETED) 1.dp else 4.dp
        )
    ) {
        Button(
            onClick = onDepositClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled && buttonConfig.enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = buttonConfig.contentColor,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = buttonConfig.contentColor.copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 아이콘
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = buttonConfig.contentColor.copy(alpha = 0.15f)
                ) {
                    Icon(
                        imageVector = buttonConfig.icon,
                        contentDescription = buttonConfig.text,
                        tint = buttonConfig.contentColor,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp)
                    )
                }

                // 텍스트 정보
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = buttonConfig.text,
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = buttonConfig.contentColor
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = buttonConfig.subText,
                        style = AppTypography.bodySmall,
                        color = buttonConfig.contentColor.copy(alpha = 0.8f)
                    )
                }

                // 금액 표시 (조건부)
                if (buttonConfig.showAmount) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "지급 금액",
                            style = AppTypography.labelSmall,
                            color = buttonConfig.contentColor.copy(alpha = 0.7f)
                        )

                        Text(
                            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(totalAmount)}원",
                            style = AppTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = buttonConfig.contentColor
                        )
                    }
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
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (isProcessing) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "처리중...",
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "즉시 입금",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "즉시 입금",
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

// 대량 입금 버튼 (여러 프로젝트 선택 시)
@Composable
fun BulkDepositButton(
    selectedCount: Int,
    totalAmount: Long,
    onBulkDepositClick: () -> Unit,
    modifier: Modifier = Modifier,
    isProcessing: Boolean = false
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1976D2)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Button(
            onClick = onBulkDepositClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isProcessing,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            if (isProcessing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "일괄 입금 처리중...",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "일괄 입금",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "선택한 ${selectedCount}건 일괄 입금",
                                style = AppTypography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "모든 현장에 동시 지급",
                                style = AppTypography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "총 금액",
                            style = AppTypography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
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

            BulkDepositButton(
                selectedCount = 3,
                totalAmount = 1200000L,
                onBulkDepositClick = {}
            )
        }
    }
}