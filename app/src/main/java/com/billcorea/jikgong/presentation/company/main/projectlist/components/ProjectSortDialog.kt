package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectSortBy
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun ProjectSortDialog(
  currentSortBy: ProjectSortBy,
  onSortSelected: (ProjectSortBy) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
      color = Color.White
    ) {
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        // 제목
        Text(
          text = "정렬 방식",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
          ),
          color = Color(0xFF1A1A1A),
          fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 정렬 옵션들
        ProjectSortBy.values().forEach { sortBy ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .selectable(
                selected = currentSortBy == sortBy,
                onClick = {
                  onSortSelected(sortBy)
                  onDismiss()
                }
              )
              .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            RadioButton(
              selected = currentSortBy == sortBy,
              onClick = null,
              colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
              )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
              text = sortBy.displayName,
              style = MaterialTheme.typography.bodyMedium,
              color = if (currentSortBy == sortBy) {
                MaterialTheme.colorScheme.primary
              } else {
                Color(0xFF333333)
              },
              fontWeight = if (currentSortBy == sortBy) {
                FontWeight.Medium
              } else {
                FontWeight.Normal
              },
              fontSize = 14.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 취소 버튼
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(
            onClick = onDismiss,
            colors = ButtonDefaults.textButtonColors(
              contentColor = Color(0xFF666666)
            )
          ) {
            Text(
              text = "취소",
              fontSize = 14.sp
            )
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectSearchBarPreview() {
  Jikgong1111Theme {
    ProjectSearchBar(
      query = "테스트 검색",
      suggestions = listOf("삼성건설", "강남구", "철근공"),
      onQueryChange = {},
      onSuggestionClick = {},
      onCloseSearch = {}
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectFilterDialogPreview() {
  Jikgong1111Theme {
    ProjectFilterDialog(
      selectedStatus = ProjectStatus.RECRUITING,
      onStatusSelected = {},
      onDismiss = {}
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectSortDialogPreview() {
  Jikgong1111Theme {
    ProjectSortDialog(
      currentSortBy = ProjectSortBy.CREATED_DATE_DESC,
      onSortSelected = {},
      onDismiss = {}
    )
  }
}