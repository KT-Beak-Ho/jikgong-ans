package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.money.data.*
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentCard(
    payment: PaymentData,
    isSelected: Boolean = false,
    isMultiSelectMode: Boolean = false,
    onPaymentClick: (PaymentData) -> Unit,
    onToggleSelection: (String) -> Unit = {},
    onProcessPayment: (String) -> Unit = {},
    onMarkUrgent: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (isMultiSelectMode) {
                    onToggleSelection(payment.id)
                } else {
                    onPaymentClick(payment)
                }
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) appColorScheme.primaryContainer else appColorScheme.surface
        ),
        border = if (payment.isUrgent) {
            androidx.compose.foundation.BorderStroke(2.dp, Color.Red)
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 상단: 근로자 정보와 상태
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = payment.worker.name,
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${payment.worker.jobType} • ${payment.project.name}",
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 멀티 선택 체크박스
                    if (isMultiSelectMode) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onToggleSelection(payment.id) }
                        )
                    }

                    // 상태 칩
                    PaymentStatusChip(
                        status = payment.status,
                        isUrgent = payment.isUrgent
                    )

                    // 더보기 메뉴
                    if (!isMultiSelectMode) {
                        PaymentCardMenu(
                            payment = payment,
                            onProcessPayment = onProcessPayment,
                            onMarkUrgent = onMarkUrgent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 작업 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "작업일",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = payment.workDate.format(DateTimeFormatter.ofPattern("MM/dd")),
                        style = AppTypography.bodyMedium,
                        color = appColorScheme.onSurface
                    )
                }

                Column {
                    Text(
                        text = "근무시간",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${payment.workHours}시간" +
                                if (payment.overtimeHours > 0) " (+${payment.overtimeHours})" else "",
                        style = AppTypography.bodyMedium,
                        color = appColorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "지급예정일",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = payment.dueDate.format(DateTimeFormatter.ofPattern("MM/dd")),
                        style = AppTypography.bodyMedium,
                        color = if (payment.isUrgent) Color.Red else appColorScheme.onSurface,
                        fontWeight = if (payment.isUrgent) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 금액 정보
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = appColorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "기본급",
                            style = AppTypography.bodySmall,
                            color = appColorScheme.onSurfaceVariant
                        )
                        Text(
                            text = NumberFormat.getNumberInstance(Locale.KOREA).format(payment.basicWage) + "원",
                            style = AppTypography.bodySmall,
                            color = appColorScheme.onSurface
                        )
                    }

                    if (payment.overtimePay > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "야근비",
                                style = AppTypography.bodySmall,
                                color = appColorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "+${NumberFormat.getNumberInstance(Locale.KOREA).format(payment.overtimePay)}원",
                                style = AppTypography.bodySmall,
                                color = appColorScheme.onSurface
                            )
                        }
                    }

                    if (payment.allowances > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "수당",
                                style = AppTypography.bodySmall,
                                color = appColorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "+${NumberFormat.getNumberInstance(Locale.KOREA).format(payment.allowances)}원",
                                style = AppTypography.bodySmall,
                                color = appColorScheme.onSurface
                            )
                        }
                    }

                    if (payment.deductions > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "공제",
                                style = AppTypography.bodySmall,
                                color = appColorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "-${NumberFormat.getNumberInstance(Locale.KOREA).format(payment.deductions)}원",
                                style = AppTypography.bodySmall,
                                color = Color.Red
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = appColorScheme.outline.copy(alpha = 0.3f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "총 지급액",
                            style = AppTypography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface
                        )
                        Text(
                            text = NumberFormat.getNumberInstance(Locale.KOREA).format(payment.totalAmount) + "원",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.primary
                        )
                    }
                }
            }

            // 메모가 있는 경우 표시
            if (payment.notes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Note,
                        contentDescription = "메모",
                        modifier = Modifier.size(16.dp),
                        tint = appColorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = payment.notes,
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentStatusChip(
    status: PaymentStatus,
    isUrgent: Boolean
) {
    val (backgroundColor, textColor) = when {
        isUrgent -> Pair(Color.Red.copy(alpha = 0.1f), Color.Red)
        status == PaymentStatus.COMPLETED -> Pair(Color.Green.copy(alpha = 0.1f), Color.Green)
        status == PaymentStatus.PROCESSING -> Pair(Color.Blue.copy(alpha = 0.1f), Color.Blue)
        status == PaymentStatus.FAILED -> Pair(Color.Red.copy(alpha = 0.1f), Color.Red)
        else -> Pair(Color.Gray.copy(alpha = 0.1f), Color.Gray)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Text(
            text = if (isUrgent) "긴급" else status.name,
            style = AppTypography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun PaymentCardMenu(
    payment: PaymentData,
    onProcessPayment: (String) -> Unit,
    onMarkUrgent: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "더보기",
                tint = appColorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (payment.status == PaymentStatus.PENDING || payment.status == PaymentStatus.URGENT) {
                DropdownMenuItem(
                    text = { Text("지급 처리") },
                    onClick = {
                        onProcessPayment(payment.id)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = "지급 처리"
                        )
                    }
                )
            }

            if (!payment.isUrgent && payment.status != PaymentStatus.COMPLETED) {
                DropdownMenuItem(
                    text = { Text("긴급 표시") },
                    onClick = {
                        onMarkUrgent(payment.id)
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.PriorityHigh,
                            contentDescription = "긴급 표시"
                        )
                    }
                )
            }

            DropdownMenuItem(
                text = { Text("상세 보기") },
                onClick = {
                    // 상세 보기 로직
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "상세 보기"
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentCardPreview() {
    val samplePayment = PaymentSampleData.getSamplePayments().first()

    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PaymentCard(
                payment = samplePayment,
                onPaymentClick = {},
                onProcessPayment = {},
                onMarkUrgent = {}
            )

            PaymentCard(
                payment = samplePayment.copy(status = PaymentStatus.URGENT),
                isSelected = true,
                isMultiSelectMode = true,
                onPaymentClick = {},
                onToggleSelection = {},
                onProcessPayment = {},
                onMarkUrgent = {}
            )
        }
    }
}