package com.billcorea.jikgong.presentation.company.auth.main.scout.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProposalListPage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Assignment,
                contentDescription = "제안 목록",
                modifier = Modifier.size(64.dp),
                tint = appColorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "제안한 내역이 없습니다",
                style = AppTypography.titleMedium,
                color = appColorScheme.onSurfaceVariant
            )

            Text(
                text = "인력에게 제안을 보내보세요",
                style = AppTypography.bodyMedium,
                color = appColorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProposalListPagePreview() {
    ProposalListPage()
}