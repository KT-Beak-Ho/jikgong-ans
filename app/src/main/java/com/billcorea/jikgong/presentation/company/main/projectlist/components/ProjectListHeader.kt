package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectSummary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProjectListHeader(
  summary: ProjectSummary,
  onSearchClick: () -> Unit,
  onFilterClick: () -> Unit,
  onSortClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth()
  ) {
    // 상단 타이틀 및 액션 버튼
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "프로젝트 관리",
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold
        ),
        color = Color(0xFF1A1A1A),
        fontSize = 24.sp
      )

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        IconButton(onClick = onSearchClick) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "검색",
            tint = Color(0xFF666666)
          )
        }
        IconButton(onClick = onFilterClick) {
          Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = "필터",
            tint = Color(0xFF666666)
          )
        }
        IconButton(onClick = onSortClick) {
          Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = "정렬",
            tint = Color(0xFF666666)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 요약 통계 카드
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        SummaryItem(
          title = "전체",
          value = summary.totalProjects.toString(),
          color = Color(0xFF333333)
        )
        SummaryItem(
          title = "모집중",
          value = summary.recruitingProjects.toString(),
          color = MaterialTheme.colorScheme.primary
        )
        SummaryItem(
          title = "진행중",
          value = summary.inProgressProjects.toString(),
          color = Color(0xFF1565C0)
        )
        SummaryItem(
          title = "평균임금",
          value = "${NumberFormat.getNumberInstance(Locale.KOREA).format(summary.averageDailyWage)}",
          color = Color(0xFF2E7D32)
        )
      }
    }
  }
}

@Composable
private fun SummaryItem(
  title: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold
      ),
      color = color,
      fontSize = 18.sp
    )
    Text(
      text = title,
      style = MaterialTheme.typography.bodySmall,
      color = Color(0xFF666666),
      fontSize = 12.sp
    )
  }
}
