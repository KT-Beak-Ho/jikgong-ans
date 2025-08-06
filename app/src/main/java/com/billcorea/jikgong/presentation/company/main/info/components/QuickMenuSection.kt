package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import com.billcorea.jikgong.ui.theme.*

@Composable
fun QuickMenuSection(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Text(
        text = "빠른 메뉴",
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = AppColors.textPrimary
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        QuickMenuItem(
          icon = Icons.Default.Assignment,
          label = "프로젝트\n관리",
          onClick = { navController.navigate("company/main/projectlist") }
        )
        QuickMenuItem(
          icon = Icons.Default.PersonSearch,
          label = "인력\n스카우트",
          onClick = { navController.navigate("company/main/scout") }
        )
        QuickMenuItem(
          icon = Icons.Default.Analytics,
          label = "통계\n분석",
          onClick = { navController.navigate("company/main/analytics") }
        )
        QuickMenuItem(
          icon = Icons.Default.Settings,
          label = "설정",
          onClick = { navController.navigate("company/main/settings") }
        )
      }
    }
  }
}

@Composable
private fun QuickMenuItem(
  icon: ImageVector,
  label: String,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .padding(12.dp)
  ) {
    Box(
      modifier = Modifier
        .size(56.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(AppColors.primaryLight),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = AppColors.primary,
        modifier = Modifier.size(28.dp)
      )
    }
    Text(
      text = label,
      style = AppTypography.labelSmall,
      color = AppColors.textPrimary,
      modifier = Modifier.padding(top = 8.dp),
      textAlign = TextAlign.Center
    )
  }
}
