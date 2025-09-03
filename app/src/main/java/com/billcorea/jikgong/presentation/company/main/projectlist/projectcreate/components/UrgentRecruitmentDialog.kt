package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UrgentRecruitmentDialog(
  onConfirm: () -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        imageVector = Icons.Default.Warning,
        contentDescription = null,
        modifier = Modifier.size(48.dp),
        tint = Color(0xFFFFA726)
      )
    },
    title = {
      Text(
        text = "긴급 모집 알림",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )
    },
    text = {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "긴급 모집은 최대 3개까지만 등록 가능합니다.",
          fontSize = 14.sp,
          textAlign = TextAlign.Center,
          color = Color(0xFF424242)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "긴급 모집으로 등록하시겠습니까?",
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          textAlign = TextAlign.Center
        )
      }
    },
    confirmButton = {
      Button(
        onClick = onConfirm,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF4B7BFF)
        ),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "확인",
          color = Color.White,
          fontWeight = FontWeight.Medium
        )
      }
    },
    dismissButton = {
      OutlinedButton(
        onClick = onDismiss,
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "취소",
          color = Color(0xFF666666)
        )
      }
    },
    containerColor = Color.White,
    shape = RoundedCornerShape(16.dp)
  )
}

@Preview(showBackground = true)
@Composable
fun UrgentRecruitmentDialogPreview() {
  MaterialTheme {
    UrgentRecruitmentDialog(
      onConfirm = { },
      onDismiss = { }
    )
  }
}