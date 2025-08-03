package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun ProjectFilterDialog(
  selectedStatus: ProjectStatus?,
  onStatusSelected: (ProjectStatus?) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "프로젝트 상태 필터",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold
        ),
        fontSize = 18.sp
      )
    },
    text = {
      Column {
        // 전체 옵션
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              onStatusSelected(null)
              onDismiss()
            }
            .padding(vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          RadioButton(
            selected = selectedStatus == null,
            onClick = null,
            colors = RadioButtonDefaults.colors(
              selectedColor = MaterialTheme.colorScheme.primary
            )
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "전체",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp
          )
        }

        // 상태별 옵션
        ProjectStatus.values().forEach { status ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onStatusSelected(status)
                onDismiss()
              }
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = selectedStatus == status,
              onClick = null,
              colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
              )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = status.displayName,
              style = MaterialTheme.typography.bodyMedium,
              fontSize = 14.sp
            )
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("닫기")
      }
    }
  )
}
