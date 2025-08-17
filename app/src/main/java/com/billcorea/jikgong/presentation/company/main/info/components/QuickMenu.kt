// File: .../presentation/company/main/info/components/QuickMenu.kt
package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuickMenu(
  savedWorkersCount: Int,
  onAutoDocsClick: () -> Unit,
  onSavedWorkersClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFF9FAFB)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Text(
        text = "빠른 메뉴",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111827),
        modifier = Modifier.padding(bottom = 12.dp)
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        QuickMenuItem(
          icon = Icons.Default.Description,
          title = "자동 서류제작",
          subtitle = "근로계약서, 임금명세서",
          onClick = onAutoDocsClick
        )

        QuickMenuItem(
          icon = Icons.Default.Star,
          title = "스크랩한 인력",
          subtitle = "${savedWorkersCount}명 저장",
          onClick = onSavedWorkersClick
        )
      }
    }
  }
}

@Composable
private fun QuickMenuItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },  // 단순 클릭으로 변경 (ripple 제거)
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 0.dp,
      pressedElevation = 2.dp  // 클릭 시 elevation 효과
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(
            Brush.linearGradient(
              colors = listOf(
                Color(0xFF7C3AED),
                Color(0xFF8B5CF6)
              )
            )
          ),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF111827)
        )
        Text(
          text = subtitle,
          fontSize = 12.sp,
          color = Color(0xFF6B7280)
        )
      }

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        tint = Color(0xFFD1D5DB),
        modifier = Modifier.size(16.dp)
      )
    }
  }
}