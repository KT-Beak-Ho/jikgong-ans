package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.scout.components.ScoutTabBar
import com.billcorea.jikgong.presentation.company.main.scout.data.Worker
import com.billcorea.jikgong.presentation.company.main.scout.pages.WorkerListPage
import com.billcorea.jikgong.presentation.company.main.scout.pages.ProposalListPage
import com.billcorea.jikgong.presentation.company.main.scout.pages.LocationSettingPage
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun CompanyScoutScreen(
  modifier: Modifier = Modifier,
  navigator: DestinationsNavigator,
  navController: NavController,
  viewModel: CompanyScoutViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val pagerState = rememberPagerState(pageCount = { 3 }) // 3개 탭으로 변경
  val coroutineScope = rememberCoroutineScope()

  Scaffold(
    modifier = modifier,
    topBar = {
      Column {
        // 토스 스타일 헤더
        TossStyleHeader(
          currentLocation = uiState.currentLocation
        )

        // 탭 바 (인력 목록, 제안 목록, 위치 설정)
        ScoutTabBarExtended(
          selectedTab = pagerState.currentPage,
          onTabSelected = { index ->
            coroutineScope.launch {
              pagerState.animateScrollToPage(index)
            }
          }
        )
      }
    },
    bottomBar = {
      // company/main/common의 CompanyBottomBar 사용
      CompanyBottomBar(
        navController = navController,
        currentRoute = "company/scout"
      )
    },
    containerColor = Color(0xFFF7F8FA) // 토스 배경색
  ) { paddingValues ->
    HorizontalPager(
      state = pagerState,
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) { page ->
      when (page) {
        0 -> WorkerListPage(
          workers = uiState.workers,
          isLoading = uiState.isLoading,
          onWorkerClick = { worker ->
            viewModel.showWorkerDetail(worker)
          },
          onScoutClick = { worker ->
            viewModel.sendScoutProposal(worker)
          },
          onRefresh = {
            viewModel.refreshWorkers()
          }
        )
        1 -> ProposalListPage(
          proposals = uiState.proposals,
          isLoading = uiState.isLoading,
          onProposalClick = { proposal ->
            viewModel.showProposalDetail(proposal)
          },
          onRefresh = {
            viewModel.refreshProposals()
          }
        )
        2 -> LocationSettingPage(
          currentLocation = uiState.currentLocation,
          searchRadius = uiState.searchRadius,
          onLocationChange = { location ->
            viewModel.updateLocation(location)
          },
          onRadiusChange = { radius ->
            viewModel.updateSearchRadius(radius)
          },
          onCurrentLocationClick = {
            viewModel.getCurrentLocation()
          }
        )
      }
    }
  }

  // 노동자 상세 정보 바텀 시트
  if (uiState.selectedWorker != null) {
    WorkerDetailBottomSheet(
      worker = uiState.selectedWorker!!,
      onDismiss = { viewModel.dismissWorkerDetail() },
      onScoutConfirm = { wage, message ->
        viewModel.confirmScoutProposal(
          worker = uiState.selectedWorker!!,
          wage = wage,
          message = message
        )
      }
    )
  }
}

@Composable
private fun TossStyleHeader(
  currentLocation: String
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(horizontal = 20.dp, vertical = 20.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "인력 스카웃",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color.Black
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "주변의 우수한 인력을 찾아보세요",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
          )
        }

        // 현재 위치 표시
        Surface(
          shape = MaterialTheme.shapes.small,
          color = Color(0xFFF0F0F0)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "위치",
              modifier = Modifier.size(16.dp),
              tint = Color(0xFF4B7BFF)
            )
            Text(
              text = currentLocation,
              style = MaterialTheme.typography.bodySmall,
              color = Color.Black
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ScoutTabBarExtended(
  selectedTab: Int,
  onTabSelected: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  val tabs = listOf("인력 목록", "제안 목록", "위치 설정")

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
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
  modifier: Modifier = Modifier,
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val backgroundColor = if (isSelected) Color(0xFF4B7BFF) else Color(0xFFF5F5F5)
  val textColor = if (isSelected) Color.White else Color.Gray

  Surface(
    modifier = modifier
      .height(36.dp),
    onClick = onClick,
    shape = MaterialTheme.shapes.small,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkerDetailBottomSheet(
  worker: Worker,
  onDismiss: () -> Unit,
  onScoutConfirm: (wage: String, message: String) -> Unit
) {
  var wage by remember { mutableStateOf("") }
  var message by remember { mutableStateOf("") }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    containerColor = Color.White,
    contentColor = Color.Black
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      // 노동자 정보 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = worker.name,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold
            )
          )
          Text(
            text = "${worker.distance}km · ${worker.jobTypes.joinToString(", ")}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
          )
        }

        // 평점
        Surface(
          shape = MaterialTheme.shapes.small,
          color = Color(0xFFF0F0F0)
        ) {
          Text(
            text = "⭐ ${worker.rating}",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 경력 정보
      InfoSection(
        title = "경력",
        content = "${worker.experience}년"
      )

      InfoSection(
        title = "희망 일당",
        content = worker.desiredWage ?: "협의 가능"
      )

      InfoSection(
        title = "자기소개",
        content = worker.introduction ?: "등록된 소개가 없습니다."
      )

      Spacer(modifier = Modifier.height(24.dp))

      // 임금 입력
      OutlinedTextField(
        value = wage,
        onValueChange = { wage = it },
        label = { Text("제안 일당") },
        placeholder = { Text("예: 150,000원") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color(0xFF4B7BFF),
          unfocusedBorderColor = Color(0xFFE5E8EB)
        )
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 메시지 입력
      OutlinedTextField(
        value = message,
        onValueChange = { message = it },
        label = { Text("스카웃 메시지") },
        placeholder = { Text("노동자에게 전달할 메시지를 입력하세요") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color(0xFF4B7BFF),
          unfocusedBorderColor = Color(0xFFE5E8EB)
        )
      )

      Spacer(modifier = Modifier.height(24.dp))

      // 스카웃 제안 버튼
      Button(
        onClick = { onScoutConfirm(wage, message) },
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF4B7BFF)
        ),
        enabled = wage.isNotEmpty() && message.isNotEmpty()
      ) {
        Text(
          text = "스카웃 제안하기",
          style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold
          )
        )
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun InfoSection(
  title: String,
  content: String
) {
  Column(modifier = Modifier.padding(vertical = 8.dp)) {
    Text(
      text = title,
      style = MaterialTheme.typography.bodySmall,
      color = Color.Gray
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = content,
      style = MaterialTheme.typography.bodyLarge
    )
  }
}

@Preview(showBackground = true)
@Composable
fun CompanyScoutScreenPreview() {
  Jikgong1111Theme {
    val mockNavController = rememberNavController()
    val mockViewModel = CompanyScoutViewModel()
    val uiState by mockViewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 3 })

    Scaffold(
      topBar = {
        Column {
          TossStyleHeader(
            currentLocation = "서울특별시 강남구"
          )
          ScoutTabBarExtended(
            selectedTab = pagerState.currentPage,
            onTabSelected = { }
          )
        }
      },
      bottomBar = {
        CompanyBottomBar(
          navController = mockNavController,
          currentRoute = "company/scout"
        )
      },
      containerColor = Color(0xFFF7F8FA)
    ) { paddingValues ->
      HorizontalPager(
        state = pagerState,
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
      ) { page ->
        when (page) {
          0 -> WorkerListPage(
            workers = uiState.workers,
            isLoading = false,
            onWorkerClick = { },
            onScoutClick = { },
            onRefresh = { }
          )
          1 -> ProposalListPage(
            proposals = uiState.proposals,
            isLoading = false,
            onProposalClick = { },
            onRefresh = { }
          )
          2 -> LocationSettingPage(
            currentLocation = "서울특별시 강남구",
            searchRadius = 10,
            onLocationChange = { },
            onRadiusChange = { },
            onCurrentLocationClick = { }
          )
        }
      }
    }
  }
}