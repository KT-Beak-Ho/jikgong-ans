package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.billcorea.jikgong.ui.theme.*

@Composable
fun ActionButtonsSection(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // 새 프로젝트 등록 버튼
    Button(
      onClick = { navController.navigate("company/main/projectcreate") },
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = AppColors.primary
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Add,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "새 프로젝트 등록",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
      )
    }

    // 인력 찾기 버튼
    OutlinedButton(
      onClick = { navController.navigate("company/main/scout") },
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp),
      border = BorderStroke(2.dp, AppColors.primary),
      shape = RoundedCornerShape(12.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = null,
        tint = AppColors.primary,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "인력 찾기",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = AppColors.primary
      )
    }

    // 고객센터 버튼
    TextButton(
      onClick = { navController.navigate("company/main/support") },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = "고객센터 문의",
        style = AppTypography.bodyMedium,
        color = AppColors.textSecondary
      )
    }
  }
}

// ============ Theme Configuration ============
