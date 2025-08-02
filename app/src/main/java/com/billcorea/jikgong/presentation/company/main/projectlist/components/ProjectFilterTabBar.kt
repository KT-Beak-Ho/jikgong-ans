// ========================================
// 📄 components/ProjectFilterTabBar.kt
// ========================================
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

/**
 * 프로젝트 필터 탭바 - 이미지 UI 스타일
 */
@Composable
fun ProjectFilterTabBar(
  selectedStatus: ProjectStatus?,
  projectCounts: Map<ProjectStatus, Int>,
  onStatusSelected: (ProjectStatus?) -> Unit,
  modifier: Modifier = Modifier
) {
  val tabs = listOf(
    FilterTab("전체", null, projectCounts.values.sum()),
    FilterTab("모집중", ProjectStatus.RECRUITING, projectCounts[ProjectStatus.RECRUITING] ?: 0),
    FilterTab("진행중", ProjectStatus.IN_PROGRESS, projectCounts[ProjectStatus.IN_PROGRESS] ?: 0),
    FilterTab("완료", ProjectStatus.COMPLETED, projectCounts[ProjectStatus.COMPLETED] ?: 0)
  )

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      tabs.forEach { tab ->
        FilterTabItem(
          tab = tab,
          isSelected = selectedStatus == tab.status,
          onClick = { onStatusSelected(tab.status) },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun FilterTabItem(
  tab: FilterTab,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  FilledTonalButton(
    onClick = onClick,
    modifier = modifier.height(40.dp),
    colors = ButtonDefaults.filledTonalButtonColors(
      containerColor = if (isSelected) {
        appColorScheme.primary
      } else {
        appColorScheme.surface
      },
      contentColor = if (isSelected) {
        appColorScheme.onPrimary
      } else {
        appColorScheme.onSurface
      }
    )
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = tab.name,
        style = AppTypography.labelMedium.copy(
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
      )
      if (tab.count > 0) {
        Text(
          text = "(${tab.count})",
          style = AppTypography.labelSmall
        )
      }
    }
  }
}

private data class FilterTab(
  val name: String,
  val status: ProjectStatus?,
  val count: Int
)