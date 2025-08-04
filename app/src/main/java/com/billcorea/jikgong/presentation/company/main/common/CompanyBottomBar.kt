package com.billcorea.jikgong.presentation.company.main.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

enum class CompanyBottomNavItem(
  val route: String,
  val title: String,
  val icon: ImageVector,
  val selectedIcon: ImageVector
) {
  PROJECT_LIST(
    route = "company/projectlist",
    title = "프로젝트",
    icon = Icons.Outlined.Assignment,
    selectedIcon = Icons.Filled.Assignment
  ),
  SCOUT(
    route = "company/scout",
    title = "인력 스카우트",
    icon = Icons.Outlined.PersonSearch,
    selectedIcon = Icons.Filled.PersonSearch
  ),
  MONEY(
    route = "company/money",
    title = "급여 관리",
    icon = Icons.Outlined.Payments,
    selectedIcon = Icons.Filled.Payments
  ),
  INFO(
    route = "company/info",
    title = "내 기업",
    icon = Icons.Outlined.Business,
    selectedIcon = Icons.Filled.Business
  )
}

@Composable
fun CompanyBottomBar(
  navController: NavHostController,
  modifier: Modifier = Modifier
) {
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = 16.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        clip = false
      ),
    color = Color.White,
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .navigationBarsPadding()
        .padding(top = 8.dp, bottom = 8.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.Top
    ) {
      CompanyBottomNavItem.values().forEach { item ->
        CompanyBottomBarItem(
          item = item,
          isSelected = currentRoute == item.route,
          onClick = {
            if (currentRoute != item.route) {
              navController.navigate(item.route) {
                popUpTo(navController.graph.startDestinationId) {
                  saveState = true
                }
                launchSingleTop = true
                restoreState = true
              }
            }
          }
        )
      }
    }
  }
}

@Composable
private fun CompanyBottomBarItem(
  item: CompanyBottomNavItem,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val animatedScale by animateFloatAsState(
    targetValue = if (isSelected) 1.1f else 1f,
    animationSpec = spring(dampingRatio = 0.7f),
    label = "scale"
  )

  val animatedColor by animateColorAsState(
    targetValue = if (isSelected) Color(0xFF1E88E5) else Color(0xFF9E9E9E),
    label = "color"
  )

  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier = Modifier
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 12.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(28.dp)
        .scale(animatedScale),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = if (isSelected) item.selectedIcon else item.icon,
        contentDescription = item.title,
        tint = animatedColor,
        modifier = Modifier.size(24.dp)
      )
    }

    Text(
      text = item.title,
      color = animatedColor,
      fontSize = 11.sp,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
      maxLines = 1
    )

    // 선택된 아이템 인디케이터
    if (isSelected) {
      Box(
        modifier = Modifier
          .width(4.dp)
          .height(4.dp)
          .background(
            color = Color(0xFF1E88E5),
            shape = RoundedCornerShape(2.dp)
          )
      )
    }
  }
}