package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.layout.*
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
fun EmptyState(
  modifier: Modifier = Modifier,
  title: String,
  description: String,
  icon: String,
  actionButton: @Composable (() -> Unit)? = null
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(32.dp)
    ) {
      // 아이콘
      Text(
        text = icon,
        fontSize = 64.sp
      )

      Spacer(modifier = Modifier.height(24.dp))

      // 제목
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold
        ),
        color = Color.Black,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 설명
      Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5
      )

      // 액션 버튼 (있을 경우)
      actionButton?.let {
        Spacer(modifier = Modifier.height(32.dp))
        it()
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun EmptyStatePreview() {
  Jikgong1111Theme {
    Column {
      EmptyState(
        title = "주변에 인력이 없습니다",
        description = "위치 설정을 확인하거나\n검색 반경을 늘려보세요",
        icon = "👷",
        modifier = Modifier.height(400.dp)
      )

      Divider()

      EmptyState(
        title = "아직 제안한 내역이 없습니다",
        description = "인력 목록에서 필요한 인력을\n스카웃해보세요",
        icon = "📋",
        modifier = Modifier.height(400.dp),
        actionButton = {
          Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF4B7BFF)
            )
          ) {
            Text("인력 찾기")
          }
        }
      )
    }
  }
}