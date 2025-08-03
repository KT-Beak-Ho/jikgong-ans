package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun ProjectEmptyState(
  isSearching: Boolean,
  isFiltered: Boolean,
  onCreateProject: () -> Unit,
  onClearFilters: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(32.dp)
    ) {
      // 아이콘
      Icon(
        imageVector = if (isSearching) Icons.Default.Search else Icons.Default.Assignment,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = Color(0xFFE0E0E0)
      )

      Spacer(modifier = Modifier.height(24.dp))

      // 제목
      Text(
        text = when {
          isSearching -> "검색 결과가 없습니다"
          isFiltered -> "조건에 맞는 프로젝트가 없습니다"
          else -> "등록된 프로젝트가 없습니다"
        },
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold
        ),
        color = Color(0xFF333333),
        fontSize = 18.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      // 설명
      Text(
        text = when {
          isSearching -> "다른 검색어로 다시 시도해보세요"
          isFiltered -> "필터를 조정하거나 초기화해보세요"
          else -> "새로운 프로젝트를 등록해보세요"
        },
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF666666),
        fontSize = 14.sp,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(32.dp))

      // 액션 버튼
      when {
        isFiltered -> {
          OutlinedButton(
            onClick = onClearFilters,
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = MaterialTheme.colorScheme.primary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "필터 초기화",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
        !isSearching -> {
          Button(
            onClick = onCreateProject,
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = null,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "프로젝트 등록",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  }
}

@Composable
fun ProjectLoadingState(
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 4.dp
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "프로젝트를 불러오는 중...",
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF666666),
        fontSize = 14.sp
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectEmptyStatePreview() {
  Jikgong1111Theme {
    Column {
      ProjectEmptyState(
        isSearching = false,
        isFiltered = false,
        onCreateProject = {},
        onClearFilters = {}
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectLoadingStatePreview() {
  Jikgong1111Theme {
    ProjectLoadingState()
  }
}