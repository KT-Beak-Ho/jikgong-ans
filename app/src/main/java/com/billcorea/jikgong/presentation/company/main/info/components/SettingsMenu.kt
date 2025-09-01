// File: .../presentation/company/main/info/components/SettingsMenu.kt
package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.NotificationInfo

@Composable
fun SettingsMenu(
  notifications: NotificationInfo,
  onNotificationClick: () -> Unit,
  onAnnouncementClick: () -> Unit,
  onCustomerServiceClick: () -> Unit,
  onTermsClick: () -> Unit,
  onMyInfoClick: () -> Unit,
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
        text = "설정 및 지원",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111827),
        modifier = Modifier.padding(bottom = 12.dp)
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        SettingItem(
          icon = Icons.Default.Notifications,
          title = "알림 설정",
          badgeCount = notifications.unreadCount,
          onClick = onNotificationClick
        )
        SettingItem(
          icon = Icons.Default.Campaign,
          title = "공지사항",
          onClick = onAnnouncementClick
        )
        SettingItem(
          icon = Icons.Default.HelpOutline,
          title = "고객센터",
          onClick = onCustomerServiceClick
        )
        SettingItem(
          icon = Icons.Default.Description,
          title = "약관 및 정책",
          onClick = onTermsClick
        )
        SettingItem(
          icon = Icons.Default.Person,
          title = "내 정보",
          subtitle = "프로필 및 계정 관리",
          onClick = onMyInfoClick
        )
      }
    }
  }
}

@Composable
private fun SettingItem(
  icon: ImageVector,
  title: String,
  subtitle: String? = null,
  badgeCount: Int = 0,
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
      pressedElevation = 1.dp  // 클릭 시 약간의 elevation 효과
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = Color(0xFF6B7280),
        modifier = Modifier.size(20.dp)
      )

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          color = Color(0xFF111827)
        )
        subtitle?.let {
          Text(
            text = it,
            fontSize = 12.sp,
            color = Color(0xFF6B7280)
          )
        }
      }

      if (badgeCount > 0) {
        Surface(
          shape = CircleShape,
          color = Color(0xFFEF4444),
          modifier = Modifier.padding(end = 8.dp)
        ) {
          Text(
            text = badgeCount.toString(),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
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