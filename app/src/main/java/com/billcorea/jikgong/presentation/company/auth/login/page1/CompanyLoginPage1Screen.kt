package com.billcorea.jikgong.presentation.company.auth.login.page1

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.network.common.ApiResult
import com.billcorea.jikgong.network.auth.LoginResponse
import com.billcorea.jikgong.api.repository.login.LoginRepository
import com.billcorea.jikgong.presentation.common.components.KeyboardConstants
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedEvent
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.ProjectListScreenDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyLoginPage1Screen(
  companyLoginViewModel: CompanyLoginSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by companyLoginViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateToNextPage by companyLoginViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
  val shouldNavigateBack by companyLoginViewModel.shouldNavigateBack.collectAsStateWithLifecycle()
  val shouldNavigateToProjectList by companyLoginViewModel.shouldNavigateToProjectList.collectAsStateWithLifecycle()

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    //  로컬 변수 초기화
    companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ResetLogin1Flow)
    //  에러 변수 초기화
    companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ClearError)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(CompanyJoinPage2ScreenDestination)
      companyLoginViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      companyLoginViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - ProjectListScreen으로 이동
  LaunchedEffect(shouldNavigateToProjectList) {
    if (shouldNavigateToProjectList) {
      navigator.navigate(ProjectListScreenDestination)
      companyLoginViewModel.clearNavigationEvents()
    }
  }

  /** 에러 다이얼로그 처리 */
  uiState.errorMessage?.let { message ->
    AlertDialog(
      onDismissRequest = {
        companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ClearError)
      },
      title = { Text("알림") },
      text = { Text(message) },
      confirmButton = {
        TextButton(
          onClick = {
            companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ClearError)
          }
        ) {
          Text("확인")
        }
      }
    )
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .padding(top = 20.dp)
      .drawBehind {
        val strokeWidth = 1.dp.toPx()
        val y = size.height - strokeWidth / 2
        drawLine(
          color = appColorScheme.outlineVariant,
          start = Offset(0f, y),
          end = Offset(size.width, y),
          strokeWidth = strokeWidth
        )
      },
    //  상단바
    topBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = {
          Log.e("", "backArrow")
          navigator.navigateUp()
        }) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Arrow Back"
          )
        }
        Text(
          text = stringResource(R.string.login),
          color = appColorScheme.onPrimaryContainer,
          style = AppTypography.titleMedium,
        )
      }
    }
  ) {
    /** 중앙 (메인) */
      innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      Spacer(modifier = Modifier.padding(5.dp))
      /** ID 입력 */
      CommonTextInput(
        value = uiState.id,
        placeholder = stringResource(R.string.loginIdOrPhone),
        validationError = uiState.validationErrors["id"],
        keyboardOptions = KeyboardConstants.Options.DEFAULT,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        onChange = {
          companyLoginViewModel.onEvent(CompanyLoginSharedEvent.UpdateId(it))
        }
      )
      Spacer(modifier = Modifier.padding(4.dp))
      /** Password 입력 */
      CommonTextInput(
        value = uiState.password,
        placeholder = stringResource(R.string.password),
        validationError = uiState.validationErrors["password"],
        keyboardOptions = KeyboardConstants.Options.DEFAULT,
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        onChange = {
          companyLoginViewModel.onEvent(CompanyLoginSharedEvent.UpdatePassword(it))
        }
      )
      Spacer(modifier = Modifier.padding(10.dp))

      /** 로그인 버튼 */
      CommonButton(
        text = stringResource(R.string.login),
        onClick = {
          companyLoginViewModel.onEvent(CompanyLoginSharedEvent.RequestLogin)
        },
        enabled =  uiState.canLogin,
        isLoading = uiState.isLoading,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),  // 전체 너비 사용
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun LoginPage1ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  // 빈 Repository로 ViewModel 생성 (네트워크 호출 무시)
  val emptyRepository = object : LoginRepository {
    override suspend fun requestLogin(
      loginIdOrPhone: String,
      password: String,
      deviceToken: String
    ): ApiResult<LoginResponse> =
      ApiResult.Error(Exception("Preview mode"))
  }

  Jikgong1111Theme {
    CompanyLoginPage1Screen(
      companyLoginViewModel = CompanyLoginSharedViewModel(emptyRepository), // ViewModel 직접 생성
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}