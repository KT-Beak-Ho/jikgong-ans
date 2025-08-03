package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFilterChips(
  selectedFilter: ProjectStatus?,
  onFilterSelected: (ProjectStatus?) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    // 전체 칩
    FilterChip(
      onClick = { onFilterSelected(null) },
      label = {
        Text(
          text = "전체",
          style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
      },
      selected = selectedFilter == null,
      colors = FilterChipDefaults.filterChipColors(
        selectedContainerColor = appColorScheme.primary,
        selectedLabelColor = Color.White,
        containerColor = appColorScheme.surface,
        labelColor = appColorScheme.onSurface
      ),
      border = if (selectedFilter == null) null else FilterChipDefaults.filterChipBorder(
        borderColor = appColorScheme.outline.copy(alpha = 0.5f)
      ),
      shape = RoundedCornerShape(20.dp)
    )

    // 상태별 칩들
    ProjectStatus.values().forEach { status ->
      FilterChip(
        onClick = {
          onFilterSelected(if (selectedFilter == status) null else status)
        },
        label = {
          Text(
            text = status.displayName,
            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Medium)
          )
        },
        selected = selectedFilter == status,
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = status.color,
          selectedLabelColor = Color.White,
          containerColor = appColorScheme.surface,
          labelColor = appColorScheme.onSurface
        ),
        border = if (selectedFilter == status) null else FilterChipDefaults.filterChipBorder(
          borderColor = appColorScheme.outline.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(20.dp)
      )
    }
  }
}

// ProjectStatus 확장 속성
private val ProjectStatus.displayName: String
  get() = when (this) {
    ProjectStatus.RECRUITING -> "모집중"
    ProjectStatus.IN_PROGRESS -> "진행중"
    ProjectStatus.COMPLETED -> "완료"
    ProjectStatus.PAUSED -> "일시중단"
    ProjectStatus.CANCELLED -> "취소"
  }

private val ProjectStatus.color: Color
  get() = when (this) {
    ProjectStatus.RECRUITING -> Color(0xFF4CAF50)
    ProjectStatus.IN_PROGRESS -> Color(0xFF2196F3)
    ProjectStatus.COMPLETED -> Color(0xFF757575)
    ProjectStatus.PAUSED -> Color(0xFFFFC107)
    ProjectStatus.CANCELLED -> Color(0xFFF44336)
  }

@Preview
@Composable
fun ProjectFilterChipsPreview() {
  Jikgong1111Theme {
    ProjectFilterChips(
      selectedFilter = ProjectStatus.RECRUITING,
      onFilterSelected = {},
      modifier = Modifier.padding(16.dp)
    )
  }
}