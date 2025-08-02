package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSummary
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

/**
 * 프로젝트 요약 카드 - 직직직 스타일
 */
@Composable
fun ProjectSummaryCard(
  summary: ProjectSummary,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.primary
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 8.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      // 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "프로젝트 현황",
            style = AppTypography.titleLarge.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color.White
          )
          Text(
            text = "실시간 업데이트",
            style = AppTypography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
          )
        }

        // 새로고침 아이콘
        Surface(
          modifier = Modifier.size(40.dp),
          shape = RoundedCornerShape(20.dp),
          color = Color.White.copy(alpha = 0.2f)
        ) {
          Icon(
            imageVector = Icons.Default.Analytics,
            contentDescription = "통계",
            modifier = Modifier
              .size(24.dp)
              .padding(8.dp),
            tint = Color.White
          )
        }
      }

      // 메인 통계 - 2x2 그리드
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // 총 프로젝트
        SummaryStatCard(
          icon = Icons.Default.Assignment,
          title = "총 프로젝트",
          value = "${summary.totalProjects}",
          subtitle = "개",
          backgroundColor = Color.White.copy(alpha = 0.15f),
          modifier = Modifier.weight(1f)
        )

        // 모집 중
        SummaryStatCard(
          icon = Icons.Default.Group,
          title = "모집 중",
          value = "${summary.recruitingProjects}",
          subtitle = "개",
          backgroundColor = Color.White.copy(alpha = 0.15f),
          modifier = Modifier.weight(1f)
        )
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // 진행 중
        SummaryStatCard(
          icon = Icons.Default.Construction,
          title = "진행 중",
          value = "${summary.inProgressProjects}",
          subtitle = "개",
          backgroundColor = Color.White.copy(alpha = 0.15f),
          modifier = Modifier.weight(1f)
        )

        // 긴급 모집
        SummaryStatCard(
          icon = Icons.Default.Warning,
          title = "긴급 모집",
          value = "${summary.urgentProjects}",
          subtitle = "개",
          backgroundColor = if (summary.urgentProjects > 0) {
            appColorScheme.error.copy(alpha = 0.3f)
          } else {
            Color.White.copy(alpha = 0.15f)
          },
          modifier = Modifier.weight(1f)
        )
      }

      // 구분선
      Divider(
        color = Color.White.copy(alpha = 0.3f),
        thickness = 1.dp
      )

      // 추가 정보
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 평균 일당
        Column(
          horizontalAlignment = Alignment.Start
        ) {
          Text(
            text = "평균 일당",
            style = AppTypography.labelMedium,
            color = Color.White.copy(alpha = 0.8f)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(summary.averageDailyWage)}원",
            style = AppTypography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color.White
          )
        }

        // 총 모집 인원
        Column(
          horizontalAlignment = Alignment.End
        ) {
          Text(
            text = "총 모집 인원",
            style = AppTypography.labelMedium,
            color = Color.White.copy(alpha = 0.8f)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "${summary.totalWorkers}명",
            style = AppTypography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color.White
          )
        }
      }

      // 완료율 프로그래스
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "프로젝트 완료율",
            style = AppTypography.labelMedium,
            color = Color.White.copy(alpha = 0.8f)
          )
          Text(
            text = "${(summary.completionRate * 100).toInt()}%",
            style = AppTypography.labelMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color.White
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
          progress = summary.completionRate,
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = Color.White,
          trackColor = Color.White.copy(alpha = 0.3f)
        )
      }
    }
  }
}

/**
 * 요약 통계 카드
 */
@Composable
private fun SummaryStatCard(
  icon: ImageVector,
  title: String,
  value: String,
  subtitle: String,
  backgroundColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    color = backgroundColor
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // 아이콘
      Icon(
        imageVector = icon,
        contentDescription = title,
        modifier = Modifier.size(24.dp),
        tint = Color.White
      )

      // 제목
      Text(
        text = title,
        style = AppTypography.labelSmall,
        color = Color.White.copy(alpha = 0.9f),
        textAlign = TextAlign.Center,
        maxLines = 1
      )

      // 값
      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = value,
          style = AppTypography.titleLarge.copy(
            fontWeight = FontWeight.Bold
          ),
          color = Color.White
        )
        Text(
          text = subtitle,
          style = AppTypography.labelSmall,
          color = Color.White.copy(alpha = 0.8f),
          modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
        )
      }
    }
  }
}
