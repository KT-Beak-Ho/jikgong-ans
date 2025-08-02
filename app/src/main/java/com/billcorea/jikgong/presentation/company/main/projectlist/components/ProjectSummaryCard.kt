package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSummary
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

@Composable
fun ProjectSummaryCard(
  summary: ProjectSummary,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.primaryContainer.copy(alpha = 0.3f)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 제목
      Text(
        text = "프로젝트 현황",
        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = appColorScheme.onSurface
      )

      // 통계 항목들
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        SummaryItem(
          icon = Icons.Default.Work,
          label = "진행 중",
          value = summary.inProgress.toString(),
          color = appColorScheme.primary
        )
        SummaryItem(
          icon = Icons.Default.Schedule,
          label = "모집 중",
          value = summary.recruiting.toString(),
          color = appColorScheme.secondary
        )
        SummaryItem(
          icon = Icons.Default.CheckCircle,
          label = "완료",
          value = summary.completed.toString(),
          color = appColorScheme.tertiary
        )
      }

      HorizontalDivider(color = appColorScheme.outline.copy(alpha = 0.3f))

      // 추가 정보
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "이번 달 모집",
            style = AppTypography.bodyMedium,
            color = appColorScheme.onSurfaceVariant
          )
          Text(
            text = "${summary.thisMonthRecruits}명",
            style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = appColorScheme.onSurface
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "평균 일당",
            style = AppTypography.bodyMedium,
            color = appColorScheme.onSurfaceVariant
          )
          Text(
            text = NumberFormat.getNumberInstance(Locale.KOREA)
              .format(summary.averageDailyWage) + "원",
            style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = appColorScheme.onSurface
          )
        }
      }
    }
  }
}

@Composable
private fun SummaryItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String,
  color: androidx.compose.ui.graphics.Color,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Surface(
      modifier = Modifier.size(48.dp),
      shape = RoundedCornerShape(24.dp),
      color = color.copy(alpha = 0.2f)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = color,
          modifier = Modifier.size(24.dp)
        )
      }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = value,
      style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = appColorScheme.onSurface
    )
    Text(
      text = label,
      style = AppTypography.bodySmall,
      color = appColorScheme.onSurfaceVariant
    )
  }
}