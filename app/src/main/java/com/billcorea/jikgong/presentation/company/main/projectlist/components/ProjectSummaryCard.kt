// ========================================
// 📄 components/ProjectSummaryCard.kt
// ========================================
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

/**
 * 프로젝트 요약 통계 카드
 */
@Composable
fun ProjectSummaryCard(
  summary: ProjectSummary,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.primaryContainer
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      // 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "프로젝트 현황",
          style = AppTypography.titleLarge.copy(
            fontWeight = FontWeight.Bold
          ),
          color = appColorScheme.onPrimaryContainer
        )

        if (summary.urgentProjects > 0) {
          AssistChip(
            onClick = { },
            label = {
              Text(
                text = "긴급 ${summary.urgentProjects}건",
                style = AppTypography.labelMedium
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "긴급",
                modifier = Modifier.size(16.dp)
              )
            },
            colors = AssistChipDefaults.assistChipColors(
              containerColor = appColorScheme.errorContainer,
              labelColor = appColorScheme.onErrorContainer,
              leadingIconContentColor = appColorScheme.onErrorContainer
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 통계 그리드
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // 전체 프로젝트
        SummaryStatItem(
          icon = Icons.Default.Assignment,
          title = "전체",
          value = summary.totalProjects.toString(),
          modifier = Modifier.weight(1f)
        )

        // 모집중 프로젝트
        SummaryStatItem(
          icon = Icons.Default.PersonAdd,
          title = "모집중",
          value = summary.recruitingProjects.toString(),
          iconTint = appColorScheme.primary,
          modifier = Modifier.weight(1f)
        )

        // 진행중 프로젝트
        SummaryStatItem(
          icon = Icons.Default.Build,
          title = "진행중",
          value = summary.inProgressProjects.toString(),
          iconTint = appColorScheme.tertiary,
          modifier = Modifier.weight(1f)
        )

        // 완료 프로젝트
        SummaryStatItem(
          icon = Icons.Default.CheckCircle,
          title = "완료",
          value = summary.completedProjects.toString(),
          iconTint = appColorScheme.outline,
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 추가 정보
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "총 모집 인원",
            style = AppTypography.bodySmall,
            color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
          )
          Text(
            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(summary.totalRequiredWorkers)}명",
            style = AppTypography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = appColorScheme.onPrimaryContainer
          )
        }

        Column {
          Text(
            text = "평균 일당",
            style = AppTypography.bodySmall,
            color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
          )
          Text(
            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(summary.averageDailyWage)}원",
            style = AppTypography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = appColorScheme.onPrimaryContainer
          )
        }
      }
    }
  }
}

@Composable
private fun SummaryStatItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  value: String,
  modifier: Modifier = Modifier,
  iconTint: androidx.compose.ui.graphics.Color = appColorScheme.onPrimaryContainer
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = icon,
      contentDescription = title,
      tint = iconTint,
      modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = value,
      style = AppTypography.titleLarge.copy(
        fontWeight = FontWeight.Bold
      ),
      color = appColorScheme.onPrimaryContainer
    )
    Text(
      text = title,
      style = AppTypography.bodySmall,
      color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
    )
  }
}