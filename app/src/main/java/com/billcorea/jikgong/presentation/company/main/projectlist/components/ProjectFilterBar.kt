package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectFilterBar(
  selectedStatus: ProjectStatus?,
  onStatusSelected: (ProjectStatus?) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    Text(
      text = "필터",
      style = AppTypography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
      color = appColorScheme.onSurface,
      modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
    )

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
      // 전체 필터
      item {
        FilterChip(
          onClick = { onStatusSelected(null) },
          label = { Text("전체") },
          selected = selectedStatus == null,
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = appColorScheme.primary,
            selectedLabelColor = appColorScheme.onPrimary
          )
        )
      }

      // 상태별 필터
      items(ProjectStatus.values()) { status ->
        FilterChip(
          onClick = { onStatusSelected(status) },
          label = { Text(status.displayName) },
          selected = selectedStatus == status,
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = status.color,
            selectedLabelColor = appColorScheme.onPrimary
          )
        )
      }
    }
  }
}
