package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectListHeader(
  totalCount: Int,
  isSearching: Boolean,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = if (isSearching) "검색 결과" else "프로젝트 목록",
        style = AppTypography.headlineSmall.copy(
          fontWeight = FontWeight.Bold
        ),
        color = appColorScheme.onSurface
      )
      Text(
        text = "총 ${totalCount}개",
        style = AppTypography.bodyMedium,
        color = appColorScheme.onSurfaceVariant
      )
    }

    IconButton(
      onClick = onSearchClick,
      colors = IconButtonDefaults.iconButtonColors(
        containerColor = appColorScheme.primaryContainer,
        contentColor = appColorScheme.onPrimaryContainer
      )
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "검색"
      )
    }
  }
}