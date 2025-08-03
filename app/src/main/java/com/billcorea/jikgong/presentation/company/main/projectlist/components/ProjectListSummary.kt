package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSummary
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectListSummary(
  summary: ProjectSummary,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.primaryContainer.copy(alpha = 0.1f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "프로젝트 현황",
          style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = appColorScheme.onSurface
        )

        Icon(
          imageVector = Icons.Default.Assessment,
          contentDescription = null,
          tint = appColorScheme.primary,
          modifier = Modifier.size(24.dp)
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        SummaryItem(
          icon = Icons.Default.Assignment,
          label = "전체",
          value = summary.totalProjects.toString(),
          color = appColorScheme.primary,
          modifier = Modifier.weight(1f)
        )

        SummaryItem(
          icon = Icons.Default.HowToReg,
          label = "모집중",
          value = summary.recruitingProjects.toString(),
          color = Color(0xFF4CAF50),
          modifier = Modifier.weight(1f)
        )

        SummaryItem(
          icon = Icons.Default.Engineering,
          label = "진행중",
          value = summary.inProgressProjects.toString(),
          color = Color(0xFF2196F3),
          modifier = Modifier.weight(1f)
        )

        SummaryItem(
          icon = Icons.Default.CheckCircle,
          label = "완료",
          value = summary.completedProjects.toString(),
          color = Color(0xFF757575),
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun SummaryItem(
  icon: ImageVector,
  label: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = color,
      modifier = Modifier.size(20.dp)
    )

    Text(
      text = value,
      style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = appColorScheme.onSurface
    )

    Text(
      text = label,
      style = AppTypography.labelSmall,
      color = appColorScheme.onSurfaceVariant
    )
  }
}

@Preview
@Composable
fun ProjectListSummaryPreview() {
  Jikgong1111Theme {
    ProjectListSummary(
      summary = ProjectSummary(
        totalProjects = 24,
        recruitingProjects = 8,
        inProgressProjects = 12,
        completedProjects = 4
      ),
      modifier = Modifier.padding(16.dp)
    )
  }
}