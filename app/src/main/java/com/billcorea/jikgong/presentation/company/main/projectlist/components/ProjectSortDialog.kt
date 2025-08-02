package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.window.Dialog
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectSortBy
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectSortDialog(
  currentSortBy: ProjectSortBy,
  onSortSelected: (ProjectSortBy) -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(
        containerColor = appColorScheme.surface
      )
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // 헤더
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "정렬 방식",
            style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = appColorScheme.onSurface
          )

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "닫기"
            )
          }
        }

        HorizontalDivider()

        // 정렬 옵션 목록
        LazyColumn(
          modifier = Modifier.heightIn(max = 400.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          items(ProjectSortBy.values()) { sortOption ->
            SortOptionItem(
              sortBy = sortOption,
              isSelected = currentSortBy == sortOption,
              onClick = {
                onSortSelected(sortOption)
                onDismiss()
              }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SortOptionItem(
  sortBy: ProjectSortBy,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    onClick = onClick,
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    color = if (isSelected)
      appColorScheme.primaryContainer.copy(alpha = 0.5f)
    else
      appColorScheme.surface
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      RadioButton(
        selected = isSelected,
        onClick = onClick,
        colors = RadioButtonDefaults.colors(
          selectedColor = appColorScheme.primary
        )
      )

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = sortBy.displayName,
          style = AppTypography.bodyLarge.copy(
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
          ),
          color = appColorScheme.onSurface
        )
        Text(
          text = sortBy.description,
          style = AppTypography.bodyMedium,
          color = appColorScheme.onSurfaceVariant
        )
      }

      if (isSelected) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = "선택됨",
          tint = appColorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}
