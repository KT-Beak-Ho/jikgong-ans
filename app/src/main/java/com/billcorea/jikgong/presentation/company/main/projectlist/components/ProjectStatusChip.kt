// ========================================
// 📄 components/ProjectStatusChip.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography

/**
 * 프로젝트 상태 칩 컴포넌트
 */
@Composable
fun ProjectStatusChip(
  status: ProjectStatus,
  modifier: Modifier = Modifier
) {
  AssistChip(
    onClick = { },
    label = {
      Text(
        text = status.displayName,
        style = AppTypography.labelSmall
      )
    },
    leadingIcon = {
      Icon(
        imageVector = status.icon,
        contentDescription = status.displayName,
        modifier = Modifier.size(14.dp)
      )
    },
    colors = AssistChipDefaults.assistChipColors(
      containerColor = status.color.copy(alpha = 0.1f),
      labelColor = status.color,
      leadingIconContentColor = status.color
    ),
    modifier = modifier.height(28.dp)
  )
}