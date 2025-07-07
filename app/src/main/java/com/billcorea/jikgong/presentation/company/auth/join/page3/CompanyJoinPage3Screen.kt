package com.billcorea.jikgong.presentation.company.auth.join.page3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.api.models.response.ApiResult
import com.billcorea.jikgong.api.models.response.EmailValidationResponse
import com.billcorea.jikgong.api.models.response.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.response.PhoneValidationResponse
import com.billcorea.jikgong.api.models.response.SmsVerificationResponse
import com.billcorea.jikgong.api.repository.JoinRepository
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.page1.CompanyJoinPage1Screen
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.JikgongAppDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyJoinPage3Screen(
  companyJoinViewModel: CompanyJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by companyJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateHome by companyJoinViewModel.shouldNavigateHome.collectAsStateWithLifecycle()
  val shouldNavigateBack by companyJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    //  로컬 변수 초기화
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ResetJoin3Flow)
    //  에러 변수 초기화
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(CompanyJoinPage2ScreenDestination)
      companyJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 홈으로 돌아가기
  LaunchedEffect(shouldNavigateHome) {
    if (shouldNavigateHome) {
      navigator.navigateUp()
      navigator.navigate(JikgongAppDestination)
      companyJoinViewModel.clearNavigationEvents()
    }
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .padding(top = 20.dp),
    //  상단바
    topBar = {
      CommonTopBar(
        currentPage = uiState.currentPage,
        totalPages = JoinConstants.TOTAL_PAGES,
        onBackClick = {
          companyJoinViewModel.onEvent(CompanyJoinSharedEvent.PreviousPage)
        }
      )
    },
    //  하단바
    bottomBar = {
      //  메인화면으로 돌아가기
      CommonButton(
        text = stringResource(R.string.go_to_home),
        onClick = {
          companyJoinViewModel.onEvent(CompanyJoinSharedEvent.HomePage)
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(WindowInsets.navigationBars.asPaddingValues())
          .padding(horizontal = 16.dp, vertical = 8.dp)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(60.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stringResource(R.string.business_partnership_application_done),
          color = appColorScheme.primary,
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          textAlign = TextAlign.Center
        )
      }
      Spacer(modifier = Modifier.padding(8.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(60.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stringResource(R.string.business_partnership_application_done_recall_msg),
          color = colorResource(R.color.black),
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          textAlign = TextAlign.Center
        )
      }
      Spacer(modifier = Modifier.padding(8.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(60.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stringResource(R.string.business_partnership_application_done_msg1),
          color = colorResource(R.color.black),
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          textAlign = TextAlign.Center
        )
      }
      Spacer(modifier = Modifier.padding(8.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(60.dp),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = stringResource(R.string.business_partnership_application_done_msg2),
          color = colorResource(R.color.black),
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun JoinPage2ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  // 빈 Repository로 ViewModel 생성 (네트워크 호출 무시)
  val emptyRepository = object : JoinRepository {
    override suspend fun sendSmsVerification(phoneNumber: String): ApiResult<SmsVerificationResponse> =
      ApiResult.Error(Exception("Preview mode"))
    override suspend fun validatePhone(phoneNumber: String): ApiResult<PhoneValidationResponse> =
      ApiResult.Error(Exception("Preview mode"))
    override suspend fun validateLoginId(loginId: String): ApiResult<LoginIdValidationResponse> =
      ApiResult.Error(Exception("Preview mode"))
    override suspend fun validateEmail(email: String): ApiResult<EmailValidationResponse> =
      ApiResult.Error(Exception("Preview mode"))
  }

  Jikgong1111Theme {
    CompanyJoinPage3Screen(
      companyJoinViewModel = CompanyJoinSharedViewModel(emptyRepository), // ViewModel 직접 생성
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}