package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.info.components.*
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoViewModel
import com.billcorea.jikgong.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyInfoScreen(
  navController: NavController = rememberNavController(),
  viewModel: CompanyInfoViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsState()
  val isLoading by viewModel.isLoading.collectAsState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "기업 정보",
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.navigateUp() }) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "뒤로가기"
            )
          }
        },
        actions = {
          IconButton(onClick = viewModel::navigateToEdit) {
            Icon(
              imageVector = Icons.Default.Edit,
              contentDescription = "편집",
              tint = AppColors.primary
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    },
    bottomBar = {
      CompanyBottomBar(
        navController = navController,
        currentRoute = "company/main/info"
      )
    }
  ) { paddingValues ->
    if (isLoading) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(color = AppColors.primary)
      }
    } else {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .background(AppColors.background)
          .verticalScroll(rememberScrollState())
      ) {
        // 회사 프로필 카드
        CompanyProfileCard(
          companyInfo = uiState,
          modifier = Modifier.padding(16.dp)
        )

        // 사업 현황 통계
        CompanyStatsCard(
          stats = uiState,
          modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 빠른 메뉴
        QuickMenuSection(
          navController = navController,
          modifier = Modifier.padding(16.dp)
        )

        // 제공 서비스
        ServiceInfoCard(
          modifier = Modifier.padding(horizontal = 16.dp)
        )

        // 연락처 정보
        ContactInfoCard(
          companyInfo = uiState,
          onPhoneClick = viewModel::callCompany,
          onEmailClick = viewModel::sendEmail,
          onAddressClick = viewModel::openMap,
          modifier = Modifier.padding(16.dp)
        )

        // 액션 버튼들
        ActionButtonsSection(
          navController = navController,
          modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}