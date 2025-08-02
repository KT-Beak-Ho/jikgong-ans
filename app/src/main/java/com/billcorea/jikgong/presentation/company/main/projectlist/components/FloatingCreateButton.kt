package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun FloatingCreateButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  ExtendedFloatingActionButton(
    onClick = onClick,
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    containerColor = appColorScheme.primary,
    contentColor = appColorScheme.onPrimary
  ) {
    Icon(
      imageVector = Icons.Default.Add,
      contentDescription = "프로젝트 추가"
    )
    Text(
      text = "프로젝트 등록",
      style = AppTypography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
      modifier = Modifier.padding(start = 8.dp)
    )
  }
}
