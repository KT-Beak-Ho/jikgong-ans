package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.network.models.PaymentData
import com.billcorea.jikgong.network.data.CompanyMockDataFactory
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(
    payment: PaymentData,
    onPaymentClick: (PaymentData) -> Unit,
    onPaymentAction: (PaymentData, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onPaymentClick(payment) },
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
            // 헤더: 작업자 정보와 상태
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // 프로필 아이콘
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        color = appColorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "작업자",
                            tint = appColorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = payment.worker.name,
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface
                        )
                        Text(
                            text = payment.worker.jobType,
                            style = AppTypography.bodySmall,
                            color = appColorScheme.onSurfaceVariant
                        )
                    }
                }

                // 상태 태그
                Surface(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                    color = payment.statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = payment.statusDisplayName,
                        style = AppTypography.labelSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = payment.statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 프로젝트 정보
            Text(
                text = payment.projectTitle,
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = appColorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 작업 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 작업 날짜
                InfoItem(
                    icon = Icons.Default.Schedule,
                    text = payment.workDate.format(DateTimeFormatter.ofPattern("MM/dd")),
                    modifier = Modifier.weight(1f)
                )

                // 작업 시간
                InfoItem(
                    icon = Icons.Default.Work,
                    text = payment.workTimeDisplay,
                    modifier = Modifier.weight(2f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 임금 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "총 임금",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(payment.totalWage)}원",
                        style = AppTypography.bodyMedium,
                        color = appColorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "실지급액",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(payment.finalAmount)}원",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.primary
                    )
                }
            }

            // 수수료 정보 (새로운 시스템 강조)
            if (payment.deductions > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "수수료 (5% 절감 적용)",
                        style = AppTypography.labelSmall,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        text = "-${NumberFormat.getNumberInstance(Locale.KOREA).format(payment.deductions)}원",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            // 액션 버튼 (대기중인 경우만)
            if (payment.status == com.billcorea.jikgong.network.models.PaymentStatus.PENDING) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onPaymentAction(payment, "reject") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = appColorScheme.error
                        )
                    ) {
                        Text(
                            text = "반려",
                            style = AppTypography.labelMedium
                        )
                    }

                    Button(
                        onClick = { onPaymentAction(payment, "approve") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = appColorScheme.primary
                        )
                    ) {
                        Text(
                            text = "지급 승인",
                            style = AppTypography.labelMedium
                        )
                    }
                }
            }

            // 메모가 있는 경우 표시
            if (payment.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    color = appColorScheme.surfaceVariant
                ) {
                    Text(
                        text = payment.notes,
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = appColorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = AppTypography.bodySmall,
            color = appColorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentCardPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PaymentCard(
                payment = CompanyMockDataFactory.getSamplePayments().first(),
                onPaymentClick = {}
            )
        }
    }
}