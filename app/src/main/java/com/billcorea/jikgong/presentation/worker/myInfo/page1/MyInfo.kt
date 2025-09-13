package com.billcorea.jikgong.presentation.worker.myInfo.page1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.worker.projectList.page1.WorkerProjectList
import com.billcorea.jikgong.presentation.worker.common.WorkerBottomNav
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun MyInfo(
  viewModel : MainViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  var isNightWorkEnabled by remember { mutableStateOf(true) }
  val config = LocalConfiguration.current
  val screenHeight = config.screenHeightDp

  Scaffold(
    bottomBar = {
      WorkerBottomNav(
        modifier = Modifier
          .fillMaxWidth()
          .height((screenHeight * .10).dp)
          .padding(5.dp),
        navigator = navigator,
        doWorkerProjectList = {},
        doWorkerMyjob = {},
        doWorkerEarning = {},
        doWorkerProfile = {}
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(innerPadding)
        .padding(16.dp)
    ) {
    // Header with profile
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 20.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Profile Image
      Box(
        modifier = Modifier
          .size(60.dp)
          .clip(CircleShape)
          .background(Color.Gray),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Person,
          contentDescription = "Profile",
          tint = Color.Black,
          modifier = Modifier.size(30.dp)
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      Column {
        Text(
          text = "김석공",
          color = Color.Black,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "정보 수정 >",
          color = Color.Gray,
          fontSize = 14.sp
        )
      }
    }

    // Night work notification badge
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A3E)),
      shape = RoundedCornerShape(12.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "밤근 일자리 제안",
          color = Color.Black,
          fontSize = 16.sp
        )
        Box(
          modifier = Modifier
            .background(
              Color(0xFF6C63FF),
              RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "2",
            color = Color.Black,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Menu Items
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      // Night work notification toggle
      SettingItemWithSwitch(
        icon = Icons.Default.Notifications,
        title = "일자리 제안 받기",
        isChecked = isNightWorkEnabled,
        onCheckedChange = { isNightWorkEnabled = it }
      )

      SettingItem(
        icon = Icons.Default.Person,
        title = "맞춤 정보 설정"
      )

      SettingItem(
        icon = Icons.Default.Bookmark,
        title = "스크랩한 일자리"
      )

      SettingItem(
        icon = Icons.Default.Notifications,
        title = "알림 설정"
      )

      SettingItem(
        icon = Icons.Default.Event,
        title = "이벤트"
      )

      SettingItem(
        icon = Icons.Default.Public,
        title = "공지사항"
      )

      SettingItem(
        icon = Icons.Default.Support,
        title = "고객센터"
      )

      SettingItem(
        icon = Icons.Default.School,
        title = "학습 및 정책"
      )

      SettingItem(
        icon = Icons.Default.Info,
        title = "앱 버전",
        subtitle = "현재 0.1.1/빌드 0.1.7"
      )
    }

    Spacer(modifier = Modifier.weight(1f))

    // Logout button
    Button(
      onClick = { /* Handle logout */ },
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF3A3A4E)
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Text(
        text = "로그아웃",
        color = Color.Black,
        fontSize = 16.sp
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Bottom links
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Center
    ) {
      Text(
        text = "로그아웃",
        color = Color.Gray,
        fontSize = 14.sp
      )

      Spacer(modifier = Modifier.width(20.dp))

      Text(
        text = "회원탈퇴",
        color = Color.Gray,
        fontSize = 14.sp
      )
    }
    }
  }
}

@Composable
fun SettingItem(
  icon: ImageVector,
  title: String,
  subtitle: String? = null,
  onClick: () -> Unit = {}
) {
  Card(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.Transparent
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = Color.Black,
        modifier = Modifier.size(24.dp)
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = title,
          color = Color.Black,
          fontSize = 16.sp
        )
        subtitle?.let {
          Text(
            text = it,
            color = Color.Gray,
            fontSize = 12.sp
          )
        }
      }

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "Navigate",
        tint = Color.Gray,
        modifier = Modifier.size(20.dp)
      )
    }
  }
}

@Composable
fun SettingItemWithSwitch(
  icon: ImageVector,
  title: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.Transparent
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = Color.Black,
        modifier = Modifier.size(24.dp)
      )

      Spacer(modifier = Modifier.width(16.dp))

      Text(
        text = title,
        color = Color.Black,
        fontSize = 16.sp,
        modifier = Modifier.weight(1f)
      )

      Switch(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
          checkedThumbColor = Color.Black,
          checkedTrackColor = Color(0xFF6C63FF),
          uncheckedThumbColor = Color.Gray,
          uncheckedTrackColor = Color(0xFF3A3A4E)
        )
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MyInfoPreview() {
  val viewModel = MainViewModel()
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    MyInfo(viewModel, navigator, modifier = Modifier.padding(3.dp))
  }
}
