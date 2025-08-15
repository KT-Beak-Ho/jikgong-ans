// File: .../presentation/company/main/info/screen/CompanyInfoScreen.kt
package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.network.CompanyData
import com.billcorea.jikgong.network.CompanyStats
import com.billcorea.jikgong.network.CompanyType
import com.billcorea.jikgong.network.NotificationInfo
import com.billcorea.jikgong.network.StatItem
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.info.components.*
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoViewModel
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoUiState
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CompanyInfoScreen(
  navigator: DestinationsNavigator,
  viewModel: CompanyInfoViewModel = koinViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val companyData by viewModel.companyData.collectAsStateWithLifecycle()

  CompanyInfoContent(
    navigator = navigator,
    uiState = uiState,
    companyData = companyData,
    onRefresh = viewModel::refresh,
    onClearNotifications = viewModel::clearNotifications,
    formatCurrency = viewModel::formatCurrency
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CompanyInfoContent(
  navigator: DestinationsNavigator,
  uiState: CompanyInfoUiState,
  companyData: CompanyData,
  onRefresh: () -> Unit,
  onClearNotifications: () -> Unit,
  formatCurrency: (Long) -> String
) {
  val scrollState = rememberLazyListState()
  val pullToRefreshState = rememberPullToRefreshState()

  Scaffold(
    bottomBar = {
      CompanyBottomBar(
        navController = navigator as NavController,
        currentRoute = "company/info"
      )
    },
    containerColor = Color.White
  ) { paddingValues ->
    PullToRefreshBox(
      isRefreshing = uiState.isRefreshing,
      onRefresh = onRefresh,
      state = pullToRefreshState,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      LazyColumn(
        state = scrollState,
        modifier = Modifier
          .fillMaxSize()
          .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(0.dp)
      ) {
        // 헤더
        item {
          HeaderSection(companyData = companyData)
        }

        // 절약 카드
        item {
          Spacer(modifier = Modifier.height(20.dp))
          SavingsCard(
            companyData = companyData,
            formatCurrency = formatCurrency
          )
        }

        // 통계 그리드
        item {
          Spacer(modifier = Modifier.height(20.dp))
          StatsGrid(stats = companyData.stats)
        }

        // 빠른 메뉴
        item {
          Spacer(modifier = Modifier.height(20.dp))
          QuickMenu(
            savedWorkersCount = companyData.savedWorkersCount,
            onAutoDocsClick = { /* Navigate */ },
            onSavedWorkersClick = { /* Navigate */ }
          )
        }

        // 설정 메뉴
        item {
          Spacer(modifier = Modifier.height(20.dp))
          SettingsMenu(
            notifications = companyData.notifications,
            onNotificationClick = onClearNotifications,
            onAnnouncementClick = { /* Navigate */ },
            onCustomerServiceClick = { /* Navigate */ },
            onTermsClick = { /* Navigate */ },
            onMyInfoClick = { /* Navigate */ }
          )
        }

        // 프리미엄 배너
        item {
          Spacer(modifier = Modifier.height(20.dp))
          PremiumBanner(onClick = { /* Navigate */ })
        }

        // 푸터
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            HorizontalDivider(
              thickness = 1.dp,
              color = Color(0xFFF3F4F6)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "앱 버전 1.2.0 | 최신 버전",
              fontSize = 12.sp,
              color = Color(0xFF9CA3AF),
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}

// ==================== Preview용 Mock 데이터 생성 함수 ====================
private fun createMockCompanyData(
  notificationCount: Int = 3,
  savedWorkersCount: Int = 32
) = CompanyData(
  id = "company_001",
  name = "김직공건설",
  type = CompanyType.PREMIUM,
  statusText = "기업회원 • 활성 사용자",
  monthlySavings = 3540000L,
  previousMonthGrowth = 28,
  targetAchievementRate = 112,
  savedWorkersCount = savedWorkersCount,
  notifications = NotificationInfo(
    unreadCount = notificationCount,
    totalCount = 15
  ),
  stats = CompanyStats(
    automatedDocs = StatItem(
      icon = "📄",
      label = "서류 자동화",
      value = 312,
      unit = "건",
      trendText = "100%"
    ),
    matchedWorkers = StatItem(
      icon = "👷",
      label = "매칭 인력",
      value = 156,
      unit = "명",
      trendText = "98.5%"
    ),
    completedProjects = StatItem(
      icon = "✅",
      label = "완료 프로젝트",
      value = 23,
      unit = "개",
      trendText = "100%"
    ),
    activeConstructionSites = StatItem(
      icon = "🏗️",
      label = "시공 현장",
      value = 8,
      unit = "곳",
      isActive = true,
      trendText = "활성"
    )
  )
)

// ==================== Full Screen Previews ====================
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "Company Info Screen - Default",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  showSystemUi = true
)
@Composable
fun CompanyInfoScreenPreview() {
  Jikgong1111Theme {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    CompanyInfoContent(
      navigator = navigator,
      uiState = CompanyInfoUiState(
        isRefreshing = false,
        error = null
      ),
      companyData = createMockCompanyData(),
      onRefresh = {},
      onClearNotifications = {},
      formatCurrency = { amount -> "₩${String.format("%,d", amount)}" }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "Company Info Screen - Loading",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF
)
@Composable
fun CompanyInfoScreenLoadingPreview() {
  Jikgong1111Theme {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    CompanyInfoContent(
      navigator = navigator,
      uiState = CompanyInfoUiState(
        isRefreshing = true,
        error = null
      ),
      companyData = createMockCompanyData(),
      onRefresh = {},
      onClearNotifications = {},
      formatCurrency = { amount -> "₩${String.format("%,d", amount)}" }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "Company Info Screen - Many Notifications",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF
)
@Composable
fun CompanyInfoScreenWithNotificationsPreview() {
  Jikgong1111Theme {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    CompanyInfoContent(
      navigator = navigator,
      uiState = CompanyInfoUiState(
        isRefreshing = false,
        error = null
      ),
      companyData = createMockCompanyData(
        notificationCount = 10,
        savedWorkersCount = 156
      ),
      onRefresh = {},
      onClearNotifications = {},
      formatCurrency = { amount -> "₩${String.format("%,d", amount)}" }
    )
  }
}

// ==================== Individual Component Previews ====================
@Preview(
  name = "Header Section",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  widthDp = 360
)
@Composable
fun HeaderSectionPreview() {
  Jikgong1111Theme {
    HeaderSection(
      companyData = createMockCompanyData()
    )
  }
}

@Preview(
  name = "Savings Card",
  showBackground = true,
  backgroundColor = 0xFFF5F5F5,
  widthDp = 360
)
@Composable
fun SavingsCardPreview() {
  Jikgong1111Theme {
    Box(modifier = Modifier.padding(16.dp)) {
      SavingsCard(
        companyData = createMockCompanyData(),
        formatCurrency = { amount -> "₩${String.format("%,d", amount)}" }
      )
    }
  }
}

@Preview(
  name = "Stats Grid",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  widthDp = 360
)
@Composable
fun StatsGridPreview() {
  Jikgong1111Theme {
    StatsGrid(
      stats = createMockCompanyData().stats
    )
  }
}

@Preview(
  name = "Quick Menu",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  widthDp = 360
)
@Composable
fun QuickMenuPreview() {
  Jikgong1111Theme {
    QuickMenu(
      savedWorkersCount = 32,
      onAutoDocsClick = {},
      onSavedWorkersClick = {}
    )
  }
}

@Preview(
  name = "Settings Menu - With Notifications",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  widthDp = 360
)
@Composable
fun SettingsMenuPreview() {
  Jikgong1111Theme {
    SettingsMenu(
      notifications = NotificationInfo(
        unreadCount = 3,
        totalCount = 15
      ),
      onNotificationClick = {},
      onAnnouncementClick = {},
      onCustomerServiceClick = {},
      onTermsClick = {},
      onMyInfoClick = {}
    )
  }
}

@Preview(
  name = "Settings Menu - No Notifications",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  widthDp = 360
)
@Composable
fun SettingsMenuNoNotificationPreview() {
  Jikgong1111Theme {
    SettingsMenu(
      notifications = NotificationInfo(
        unreadCount = 0,
        totalCount = 0
      ),
      onNotificationClick = {},
      onAnnouncementClick = {},
      onCustomerServiceClick = {},
      onTermsClick = {},
      onMyInfoClick = {}
    )
  }
}

@Preview(
  name = "Premium Banner",
  showBackground = true,
  backgroundColor = 0xFFF5F5F5,
  widthDp = 360
)
@Composable
fun PremiumBannerPreview() {
  Jikgong1111Theme {
    Box(modifier = Modifier.padding(16.dp)) {
      PremiumBanner(
        onClick = {}
      )
    }
  }
}