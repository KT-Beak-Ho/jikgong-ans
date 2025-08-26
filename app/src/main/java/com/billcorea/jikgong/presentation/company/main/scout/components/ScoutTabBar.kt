package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun ScoutTabBar(
  modifier: Modifier = Modifier,
  selectedTab: Int,
  onTabSelected: (Int) -> Unit
) {
  val tabs = listOf("인력 목록", "제안 목록")

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      tabs.forEachIndexed { index, title ->
        TabItem(
          title = title,
          isSelected = selectedTab == index,
          onClick = { onTabSelected(index) },
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun TabItem(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val backgroundColor = if (isSelected) Color(0xFF4B7BFF) else Color(0xFFF5F5F5)
  val textColor = if (isSelected) Color.White else Color.Gray

  Surface(
    modifier = modifier
      .height(40.dp)
      .clickable { onClick() },
    shape = RoundedCornerShape(20.dp),
    color = backgroundColor
  ) {
    Box(
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        ),
        color = textColor
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ScoutTabBarPreview() {
  Jikgong1111Theme {
    Column {
      ScoutTabBar(
        selectedTab = 0,
        onTabSelected = {}
      )
      Spacer(modifier = Modifier.height(8.dp))
      ScoutTabBar(
        selectedTab = 1,
        onTabSelected = {}
      )
    }
  }
}