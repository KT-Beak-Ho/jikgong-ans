package com.billcorea.jikgong.presentation.company.main.info.components

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
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoUiState
import com.billcorea.jikgong.ui.theme.*

@Composable
fun ContactInfoCard(
  companyInfo: CompanyInfoUiState,
  onPhoneClick: () -> Unit,
  onEmailClick: () -> Unit,
  onAddressClick: () -> Unit,
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
        text = "연락처 정보",
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = AppColors.textPrimary
      )

      Spacer(modifier = Modifier.height(16.dp))

      ContactItem(
        icon = Icons.Default.Person,
        label = "대표자",
        value = companyInfo.ceoName,
        onClick = null
      )
      ContactItem(
        icon = Icons.Default.Phone,
        label = "전화번호",
        value = companyInfo.phoneNumber,
        onClick = onPhoneClick
      )
      ContactItem(
        icon = Icons.Default.Email,
        label = "이메일",
        value = companyInfo.email,
        onClick = onEmailClick
      )
      ContactItem(
        icon = Icons.Default.LocationOn,
        label = "주소",
        value = companyInfo.address,
        onClick = onAddressClick
      )
    }
  }
}

@Composable
private fun ContactItem(
  icon: ImageVector,
  label: String,
  value: String,
  onClick: (() -> Unit)?
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .then(
        if (onClick != null) {
          Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
        } else {
          Modifier
        }
      )
      .padding(vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = AppColors.textSecondary,
      modifier = Modifier.size(20.dp)
    )
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(start = 12.dp)
    ) {
      Text(
        text = label,
        style = AppTypography.labelSmall,
        color = AppColors.textSecondary
      )
      Text(
        text = value,
        style = AppTypography.bodyMedium,
        color = AppColors.textPrimary,
        fontWeight = FontWeight.Medium
      )
    }
    if (onClick != null) {
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        tint = AppColors.textSecondary,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}
