// File: .../presentation/company/main/info/components/QuickMenu.kt
package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.style.TextAlign

@Composable
fun QuickMenu(
  savedWorkersCount: Int,
  scoutProposalCount: Int = 0,
  onAutoDocsClick: () -> Unit,
  onSavedWorkersClick: () -> Unit,
  onScoutClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var showComingSoonDialog by remember { mutableStateOf(false) }
  
  // 추후 추가 예정 다이얼로그
  if (showComingSoonDialog) {
    ComingSoonDialog(
      onDismiss = { showComingSoonDialog = false }
    )
  }
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
          icon = Icons.Default.PersonSearch,
          title = "스카우트 제안",
          subtitle = "총 ${scoutProposalCount}건 제안",
          onClick = onScoutClick
        )

        QuickMenuItem(
          icon = Icons.Default.Star,
          title = "스크랩한 인력",
          subtitle = "${savedWorkersCount}명 저장",
          onClick = { showComingSoonDialog = true }
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
                Color(0xFF4B7BFF),
                Color(0xFF5B87FF)
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

@Composable
private fun ComingSoonDialog(
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      dismissOnBackPress = true,
      dismissOnClickOutside = true
    )
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.85f)
        .wrapContentHeight(),
      shape = RoundedCornerShape(20.dp),
      color = Color.White,
      shadowElevation = 12.dp
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // 아이콘
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
              Brush.linearGradient(
                colors = listOf(
                  Color(0xFF4B7BFF),
                  Color(0xFF6B93FF)
                )
              )
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 제목
        Text(
          text = "🚀 추후 추가 예정",
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold
          ),
          color = Color(0xFF1A1D29),
          textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 설명
        Text(
          text = "이 기능은 현재 개발 중에 있습니다.\n더 나은 서비스로 \n곧 만나벵겠습니다! 🚀",
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF6B7280),
          textAlign = TextAlign.Center,
          lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 확인 버튼
        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4B7BFF)
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = "확인",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color.White
          )
        }
      }
    }
  }
}