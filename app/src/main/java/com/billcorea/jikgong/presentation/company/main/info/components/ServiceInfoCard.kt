package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoUiState
import com.billcorea.jikgong.ui.theme.*

@Composable
fun CompanyProfileCard(
  companyInfo: CompanyInfoUiState,
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
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // 회사 로고
      Box(
        modifier = Modifier
          .size(100.dp)
          .clip(CircleShape)
          .background(
            color = AppColors.primary
          ),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "직",
          fontSize = 48.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 회사명
      Text(
        text = companyInfo.companyName,
        style = AppTypography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = AppColors.textPrimary
      )

      // 설명
      Text(
        text = companyInfo.description,
        style = AppTypography.bodyMedium,
        color = AppColors.textSecondary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 8.dp)
      )

      // 평점
      Row(
        modifier = Modifier.padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        repeat(5) { index ->
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = if (index < companyInfo.rating.toInt())
              Color(0xFFFFA726) else Color(0xFFE0E0E0),
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "${companyInfo.rating}",
          style = AppTypography.bodyLarge,
          fontWeight = FontWeight.Bold,
          color = AppColors.textPrimary
        )
        Text(
          text = " (리뷰 ${companyInfo.reviewCount}개)",
          style = AppTypography.bodyMedium,
          color = AppColors.textSecondary
        )
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 20.dp),
        color = AppColors.divider
      )

      // 기본 정보
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        InfoItem(
          icon = Icons.Default.CalendarToday,
          label = "설립일",
          value = companyInfo.establishDate
        )
        InfoItem(
          icon = Icons.Default.People,
          label = "직원수",
          value = "${companyInfo.employeeCount}명"
        )
        InfoItem(
          icon = Icons.Default.Badge,
          label = "사업자번호",
          value = companyInfo.businessNumber.substring(0, 6) + "..."
        )
      }
    }
  }
}

@Composable
private fun InfoItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  label: String,
  value: String
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = AppColors.primary,
      modifier = Modifier.size(24.dp)
    )
    Text(
      text = label,
      style = AppTypography.labelSmall,
      color = AppColors.textSecondary,
      modifier = Modifier.padding(top = 4.dp)
    )
    Text(
      text = value,
      style = AppTypography.bodyMedium,
      fontWeight = FontWeight.Medium,
      color = AppColors.textPrimary
    )
  }
}
