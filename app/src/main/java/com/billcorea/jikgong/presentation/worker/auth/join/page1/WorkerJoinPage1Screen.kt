package com.billcorea.jikgong.presentation.worker.auth.join.page1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.common.components.KeyboardConstants
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonWorkerTopBar
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedEvent
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun WorkerJoinPage1Screen(
  workerJoinViewModel: WorkerJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by workerJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateToNextPage by workerJoinViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
  val shouldNavigateBack by workerJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()


  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin1Flow)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(WorkerJoinPage2ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 🚨 에러 다이얼로그 처리
  uiState.errorMessage?.let { message ->
    AlertDialog(
      onDismissRequest = {
        workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ClearError)
      },
      title = { Text("알림") },
      text = { Text(message) },
      confirmButton = {
        TextButton(
          onClick = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ClearError)
          }
        ) {
          Text("확인")
        }
      }
    )
  }

  //  화면 시작
  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .padding(top = 20.dp),
    //  상단바
    topBar = {
      CommonWorkerTopBar(
        currentPage = uiState.currentPage,
        totalPages = JoinConstants.TOTAL_PAGES,
        onBackClick = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.PreviousPage)
        }
      )
    },
    //  하단바
    bottomBar = {
      //  다음 버튼
      CommonButton(
        text = stringResource(R.string.next),
        onClick = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.NextPage)
        },
        enabled = uiState.isPage1Complete,
        modifier =
          Modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(horizontal = 16.dp, vertical = 8.dp)
      )
    }
  ) {
    //  중앙 (메인)
      innerPadding ->
    //  전화 번호 입력 섹션
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      //  전화 번호 입력 헤더 메시지
      Text(
        text = stringResource(R.string.enterPhoneNumber),
        color = appColorScheme.primary,
        lineHeight = 1.33.em,
        style = AppTypography.titleLarge,
        modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight(align = Alignment.CenterVertically)
      )
      Spacer(modifier = Modifier.padding(8.dp))

      // 전화 번호 입력 받는 섹션
      CommonTextInput(
        value = uiState.phoneNumber,
        labelMainText = stringResource(R.string.telnumber),
        placeholder = stringResource(R.string.enterForNumberOnly),
        validationError = uiState.validationErrors["phoneNumber"],
        keyboardOptions = KeyboardConstants.Options.PHONE,
        modifier = Modifier.fillMaxWidth(),
        onChange = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdatePhoneNumber(it))
        },
      )
      Spacer(modifier = Modifier.padding(5.dp))

      //  인증번호 받기 버튼
      CommonButton(
        text = stringResource(R.string.getSecretCode),
        onClick = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.RequestVerificationCode)
        },
        enabled = uiState.phoneNumber.isNotEmpty() && uiState.isValidPhoneNumber,
        isLoading = uiState.isWaiting,
        modifier = Modifier.fillMaxWidth()  // 전체 너비 사용
      )


      //  인증번호 입력 섹션 (조건부 표시)
      if (uiState.isSecurityStepActive) {
        CommonTextInput(
          value = uiState.verificationCode,
          labelMainText = stringResource(R.string.secretCode),
          placeholder = stringResource(R.string.enterSecretCode),
          validationError = uiState.validationErrors["verificationCode"],
          keyboardOptions = KeyboardConstants.Options.NUMBER,
          keyboardActions = KeyboardConstants.Actions.doneAndHide(),
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateVerificationCode(it))
          },
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun JoinPage1ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    WorkerJoinPage1Screen(
      workerJoinViewModel = WorkerJoinSharedViewModel(), // ViewModel 직접 생성
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}