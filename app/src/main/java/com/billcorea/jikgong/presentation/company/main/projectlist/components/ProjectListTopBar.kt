package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListTopBar(
  onSearchClick: () -> Unit,
  onSortClick: () -> Unit,
  onFilterClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = appColorScheme.surface,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "직직직",
          style = AppTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
          color = appColorScheme.primary
        )
        Text(
          text = "프로젝트 관리",
          style = AppTypography.bodyMedium,
          color = appColorScheme.onSurfaceVariant
        )
      }

      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onSearchClick) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "검색",
            tint = appColorScheme.onSurface
          )
        }

        IconButton(onClick = onSortClick) {
          Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = "정렬",
            tint = appColorScheme.onSurface
          )
        }

        IconButton(onClick = onFilterClick) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = "필터",
            tint = appColorScheme.onSurface
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun ProjectListTopBarPreview() {
  Jikgong1111Theme {
    ProjectListTopBar(
      onSearchClick = {},
      onSortClick = {},
      onFilterClick = {}
    )
  }
}