package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
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

@Composable
fun DepositButton(
    status: ProjectPaymentStatus,
    totalAmount: Long,
    onDepositClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val (buttonText, buttonColor, textColor, icon) = when (status) {
        ProjectPaymentStatus.PENDING -> {
            Pair(
                Pair("입금", Color(0xFF2E3A59)),
                Pair(Color.White, Icons.Default.AccountBalance)
            )
        }
        ProjectPaymentStatus.PROCESSING -> {
            Pair(
                Pair("처리중", appColorScheme.primary.copy(alpha = 0.6f)),
                Pair(Color.White, Icons.Default.AccountBalance)
            )
        }
        ProjectPaymentStatus.COMPLETED -> {
            Pair(
                Pair("입금 완료", appColorScheme.surfaceVariant),
                Pair(appColorScheme.onSurfaceVariant, Icons.Default.Check)
            )
        }
        ProjectPaymentStatus.FAILED -> {
            Pair(
                Pair("재시도", Color(0xFFEF5350)),
                Pair(Color.White, Icons.Default.AccountBalance)
            )
        }
    }

    Button(
        onClick = onDepositClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled && status != ProjectPaymentStatus.COMPLETED,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor.first,
            contentColor = buttonColor.second,
            disabledContainerColor = appColorScheme.surfaceVariant,
            disabledContentColor = appColorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (status == ProjectPaymentStatus.COMPLETED) 0.dp else 2.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                imageVector = icon.second,
                contentDescription = buttonText.first,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = buttonText.first,
                style = AppTypography.labelLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
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

            QuickDepositButton(
                amount = 123500L,
                onDepositClick = {}
            )
        }
    }
}