package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

data class QuickFilter(
  val id: String,
  val label: String,
  val icon: androidx.compose.ui.graphics.vector.ImageVector,
  val count: Int = 0
)

@Composable
fun ProjectQuickFilters(
  filters: List<QuickFilter>,
  selectedFilterId: String?,
  onFilterSelected: (String?) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surfaceVariant.copy(alpha = 0.5f)
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text(
        text = "빠른 필터",
        style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = appColorScheme.onSurface
      )

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // 전체 보기
        item {
          QuickFilterChip(
            label = "전체",
            icon = Icons.Default.Apps,
            count = filters.sumOf { it.count },
            isSelected = selectedFilterId == null,
            onClick = { onFilterSelected(null) }
          )
        }

        // 개별 필터들
        items(filters) { filter ->
          QuickFilterChip(
            label = filter.label,
            icon = filter.icon,
            count = filter.count,
            isSelected = selectedFilterId == filter.id,
            onClick = { onFilterSelected(filter.id) }
          )
        }
      }
    }
  }
}

@Composable
private fun QuickFilterChip(
  label: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  count: Int,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  FilterChip(
    onClick = onClick,
    label = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          modifier = Modifier.size(16.dp)
        )
        Text(text = label)
        if (count > 0) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (isSelected)
              appColorScheme.onPrimary.copy(alpha = 0.2f)
            else
              appColorScheme.primary.copy(alpha = 0.2f)
          ) {
            Text(
              text = count.toString(),
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
              style = AppTypography.labelSmall.copy(fontWeight = FontWeight.Bold),
              color = if (isSelected) appColorScheme.onPrimary else appColorScheme.primary
            )
          }
        }
      }
    },
    selected = isSelected,
    modifier = modifier,
    colors = FilterChipDefaults.filterChipColors(
      selectedContainerColor = appColorScheme.primary,
      selectedLabelColor = appColorScheme.onPrimary
    )
  )
}