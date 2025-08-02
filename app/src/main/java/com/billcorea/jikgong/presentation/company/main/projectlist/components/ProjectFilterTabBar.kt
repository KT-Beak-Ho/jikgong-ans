package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ProjectFilterTabBar(
  selectedStatus: ProjectStatus?,
  projectCounts: Map<ProjectStatus, Int>,
  onStatusSelected: (ProjectStatus?) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyRow(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(horizontal = 4.dp)
  ) {
    // 전체 탭
    item {
      FilterChip(
        selected = selectedStatus == null,
        onClick = { onStatusSelected(null) },
        label = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = "전체",
              style = AppTypography.labelMedium.copy(
                fontWeight = if (selectedStatus == null) FontWeight.Bold else FontWeight.Normal
              )
            )
            Text(
              text = "(${projectCounts.values.sum()})",
              style = AppTypography.labelSmall,
              color = appColorScheme.primary
            )
          }
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = appColorScheme.primary,
          selectedLabelColor = appColorScheme.onPrimary,
          containerColor = appColorScheme.surface,
          labelColor = appColorScheme.onSurfaceVariant
        )
      )
    }

    // 상태별 탭
    items(ProjectStatus.values()) { status ->
      FilterChip(
        selected = selectedStatus == status,
        onClick = { onStatusSelected(status) },
        label = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Text(
              text = status.displayName,
              style = AppTypography.labelMedium.copy(
                fontWeight = if (selectedStatus == status) FontWeight.Bold else FontWeight.Normal
              )
            )
            Text(
              text = "(${projectCounts[status] ?: 0})",
              style = AppTypography.labelSmall,
              color = if (selectedStatus == status) appColorScheme.onPrimary else status.color
            )
          }
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = status.color,
          selectedLabelColor = appColorScheme.onPrimary,
          containerColor = appColorScheme.surface,
          labelColor = appColorScheme.onSurfaceVariant
        )
      )
    }
  }
}
