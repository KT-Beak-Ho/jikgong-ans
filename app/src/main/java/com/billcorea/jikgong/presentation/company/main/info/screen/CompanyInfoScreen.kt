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
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyStats
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyType
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyStatus
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.NotificationInfo
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.StatItem
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.common.InfoTopBar
import com.billcorea.jikgong.presentation.company.main.info.components.HeaderSection
import com.billcorea.jikgong.presentation.company.main.info.components.SavingsCard
import com.billcorea.jikgong.presentation.company.main.info.components.StatsGrid
import com.billcorea.jikgong.presentation.company.main.info.components.QuickMenu
import com.billcorea.jikgong.presentation.company.main.info.components.SettingsMenu
import com.billcorea.jikgong.presentation.company.main.info.components.PremiumBanner
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoViewModel
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoUiState
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.billcorea.jikgong.presentation.destinations.NotificationSettingsScreenDestination
import com.billcorea.jikgong.presentation.destinations.AnnouncementScreenDestination
import com.billcorea.jikgong.presentation.destinations.CustomerServiceScreenDestination
import com.billcorea.jikgong.presentation.destinations.TermsAndPoliciesScreenDestination
import com.billcorea.jikgong.presentation.destinations.MyInfoScreenDestination
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

// ==================== 내부 유틸리티 함수 ====================
/**
 * CompanyInfo 화면 내부에서 사용하는 통화 포맷팅 유틸리티
 */
internal object CompanyInfoFormatter {
  /**
   * 금액을 한국 원화 형식으로 포맷팅
   * @param amount 포맷팅할 금액
   * @return 포맷팅된 문자열 (예: "₩1,234,567")
   */
  fun formatCurrency(amount: Long): String {
    return "₩${String.format(Locale.KOREA, "%,d", amount)}"
  }
}

// ==================== Main Screen ====================
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CompanyInfoScreen(
  navigator: DestinationsNavigator,
  navController: NavController,
  viewModel: CompanyInfoViewModel = koinViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val companyData by viewModel.companyData.collectAsStateWithLifecycle()
  val totalSavingsAmount by viewModel.totalSavingsAmount.collectAsStateWithLifecycle()
  val dynamicStats by viewModel.projectStats.collectAsStateWithLifecycle()
  val scoutProposalCount by viewModel.scoutProposalCount.collectAsStateWithLifecycle()

  // dynamicStats를 CompanyStats 형태로 변환
  val updatedStats = if (dynamicStats.isNotEmpty()) {
    CompanyStats(
      automatedDocs = companyData.stats.automatedDocs,  // 기존값 유지
      matchedWorkers = dynamicStats.getOrNull(0) ?: companyData.stats.matchedWorkers,  // 매칭 인력
      completedProjects = dynamicStats.getOrNull(1) ?: companyData.stats.completedProjects,  // 완료 프로젝트
      activeConstructionSites = dynamicStats.getOrNull(2) ?: companyData.stats.activeConstructionSites  // 시공 현장
    )
  } else {
    companyData.stats
  }

  CompanyInfoContent(
    navigator = navigator,
    navController = navController,
    uiState = uiState,
    companyData = companyData.copy(stats = updatedStats),
    totalSavingsAmount = totalSavingsAmount,
    scoutProposalCount = scoutProposalCount,
    onRefresh = viewModel::refresh,
    onClearNotifications = viewModel::clearNotifications,
    formatCurrency = viewModel::formatCurrency,
    showBottomBar = true
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CompanyInfoContent(
  navigator: DestinationsNavigator,
  navController: NavController,
  uiState: CompanyInfoUiState,
  companyData: CompanyData,
  totalSavingsAmount: Long,
  scoutProposalCount: Int,
  onRefresh: () -> Unit,
  onClearNotifications: () -> Unit,
  formatCurrency: (Long) -> String,
  showBottomBar: Boolean = true
) {
  val scrollState = rememberLazyListState()
  val pullToRefreshState = rememberPullToRefreshState()

  Scaffold(
    topBar = {
      InfoTopBar(
        title = companyData.name,
        notificationCount = companyData.notifications.unreadCount,
        onNotificationClick = onClearNotifications,
        onSettingsClick = {
          // TODO: 설정 화면으로 이동
        }
      )
    },
    bottomBar = {
      if (showBottomBar) {
        CompanyBottomBar(
          navController = navController,
          currentRoute = "company_info_screen"
        )
      }
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
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(bottom = 16.dp)  // 컨텐츠 하단 패딩
      ) {
        // 절약 카드
        item {
          Spacer(modifier = Modifier.height(20.dp))
          SavingsCard(
            companyData = companyData,
            formatCurrency = formatCurrency,
            totalSavingsAmount = totalSavingsAmount
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
            scoutProposalCount = scoutProposalCount,
            onAutoDocsClick = { /* Navigate */ },
            onSavedWorkersClick = { /* Navigate */ },
            onScoutClick = {
              // CompanyScoutScreen으로 이동
              navController.navigate("company_scout_main")
            }
          )
        }

        // 설정 메뉴
        item {
          Spacer(modifier = Modifier.height(20.dp))
          SettingsMenu(
            notifications = companyData.notifications,
            onNotificationClick = { 
              navigator.navigate(NotificationSettingsScreenDestination)
            },
            onAnnouncementClick = { 
              navigator.navigate(AnnouncementScreenDestination)
            },
            onCustomerServiceClick = { 
              navigator.navigate(CustomerServiceScreenDestination)
            },
            onTermsClick = { 
              navigator.navigate(TermsAndPoliciesScreenDestination)
            },
            onMyInfoClick = { 
              navigator.navigate(MyInfoScreenDestination)
            }
          )
        }

        // 프리미엄 배너
        item {
          Spacer(modifier = Modifier.height(20.dp))
          PremiumBanner(onClick = { /* Navigate */ })
        }

        // 푸터
        item {
          Spacer(modifier = Modifier.height(20.dp))
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
              color = Color.Gray,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}

// ==================== Mock Data Helper ====================
private fun createMockCompanyData(
  notificationCount: Int = 3,
  savedWorkersCount: Int = 32
): CompanyData {
  return CompanyData(
    id = "company_001",
    name = "김직공건설",
    type = CompanyType.PREMIUM,
    status = CompanyStatus.ACTIVE,
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
}

// ==================== Preview Functions ====================
@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "Company Info Screen - Full View with BottomBar",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  device = "spec:width=411dp,height=891dp,dpi=420",
  showSystemUi = true  // 시스템 UI 포함하여 전체 화면 보기
)
@Composable
fun CompanyInfoScreenFullPreview() {
  Jikgong1111Theme {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    CompanyInfoContent(
      navigator = navigator,
      navController = navController,
      uiState = CompanyInfoUiState(
        isRefreshing = false,
        error = null
      ),
      companyData = createMockCompanyData(),
      totalSavingsAmount = 5420000L,
      scoutProposalCount = 5,
      onRefresh = {},
      onClearNotifications = {},
      formatCurrency = CompanyInfoFormatter::formatCurrency,
      showBottomBar = true  // BottomBar 표시
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "Company Info Screen - Default",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  heightDp = 800,  // 높이를 지정하여 전체 내용 확인
  widthDp = 360
)
@Composable
fun CompanyInfoScreenPreview() {
  Jikgong1111Theme {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    CompanyInfoContent(
      navigator = navigator,
      navController = navController,
      uiState = CompanyInfoUiState(
        isRefreshing = false,
        error = null
      ),
      companyData = createMockCompanyData(),
      totalSavingsAmount = 5420000L,
      scoutProposalCount = 5,
      onRefresh = {},
      onClearNotifications = {},
      formatCurrency = CompanyInfoFormatter::formatCurrency,
      showBottomBar = true
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "Company Info Screen - Loading",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  heightDp = 800,
  widthDp = 360
)
@Composable
fun CompanyInfoScreenLoadingPreview() {
  Jikgong1111Theme {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    CompanyInfoContent(
      navigator = navigator,
      navController = navController,
      uiState = CompanyInfoUiState(
        isRefreshing = true,
        error = null
      ),
      companyData = createMockCompanyData(),
      totalSavingsAmount = 5420000L,
      scoutProposalCount = 5,
      onRefresh = {},
      onClearNotifications = {},
      formatCurrency = CompanyInfoFormatter::formatCurrency,
      showBottomBar = true
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  name = "Company Info Screen - Many Notifications",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  heightDp = 800,
  widthDp = 360
)
@Composable
fun CompanyInfoScreenWithNotificationsPreview() {
  Jikgong1111Theme {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    CompanyInfoContent(
      navigator = navigator,
      navController = navController,
      uiState = CompanyInfoUiState(
        isRefreshing = false,
        error = null
      ),
      companyData = createMockCompanyData(
        notificationCount = 10,
        savedWorkersCount = 156
      ),
      totalSavingsAmount = 5420000L,
      scoutProposalCount = 12,
      onRefresh = {},
      onClearNotifications = {},
      formatCurrency = CompanyInfoFormatter::formatCurrency,
      showBottomBar = true
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
        formatCurrency = CompanyInfoFormatter::formatCurrency,
        totalSavingsAmount = 5420000L
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
  name = "Settings Menu",
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

// ==================== Bottom Bar Preview ====================
@Preview(
  name = "Company Bottom Bar",
  showBackground = true,
  backgroundColor = 0xFFFFFFFF,
  widthDp = 360
)
@Composable
fun CompanyBottomBarPreview() {
  Jikgong1111Theme {
    CompanyBottomBar(
      navController = rememberNavController(),
      currentRoute = "company_info_screen"
    )
  }
}