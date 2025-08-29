package com.billcorea.jikgong.presentation.worker.auth.join.page3

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.JoinedPage.BankSelectList
import com.billcorea.jikgong.presentation.common.components.KeyboardConstants
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.LabelText
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage4ScreenDestination
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonBottomSheetInput
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonWorkerTopBar
import com.billcorea.jikgong.presentation.worker.auth.common.constants.WorkerJoinConstants
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedEvent
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun WorkerJoinPage3Screen(
  workerJoinViewModel: WorkerJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  var showBottomSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false)
  val uiState by workerJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateToNextPage by workerJoinViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
  val shouldNavigateBack by workerJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()
  val keyboardController = LocalSoftwareKeyboardController.current

  val focusRequesters = remember {
    List(3) { FocusRequester() }
  }


  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin3Flow)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(WorkerJoinPage4ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(WorkerJoinPage2ScreenDestination)
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
        totalPages = WorkerJoinConstants.TOTAL_PAGES,
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
        enabled = uiState.isPage3Complete,
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
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp),  //  각 item별 공통으로 간격 적용
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {

      item {
        Text(
          text = stringResource(R.string.enterBankAccountInfo),
          color = appColorScheme.primary,
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Image(
          painter = painterResource(id = R.drawable.bank_description),
          contentDescription = null,
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        // 예금주
        CommonTextInput(
          value = uiState.accountName,
          labelMainText = stringResource(R.string.accountName),
          placeholder = stringResource(R.string.enterName),
          validationError = uiState.validationErrors["accountName"],
          keyboardOptions = KeyboardConstants.Options.DEFAULT,
          keyboardActions = KeyboardActions(onNext = {
            showBottomSheet = true
            keyboardController?.hide()
          }),
          modifier = Modifier.fillMaxWidth().focusRequester(focusRequesters[0]),
          onChange = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateAccountName(it))
          }
        )
      }

      item {
        // 라벨
        LabelText(
          mainText = stringResource(R.string.bankName),
          appendText = ""
        )

        Spacer(modifier = Modifier.padding(5.dp))
        CommonBottomSheetInput(
          value = uiState.bankName,
          placeholderText = stringResource(R.string.msgSelectBank),
          errorKey = "bankName",
          validationErrors = uiState.validationErrors,
          iconPainter = painterResource(R.drawable.ic_keyboard_arrow_down_24dp),
          onClick = {
            showBottomSheet = true
          }
        )
      }

      item {
        // 계좌번호
        CommonTextInput(
          value = uiState.accountNumber,
          labelMainText = stringResource(R.string.accountNumber),
          placeholder = stringResource(R.string.enterAccountNumber),
          validationError = uiState.validationErrors["accountNumber"],
          keyboardOptions = KeyboardConstants.Options.PHONE,
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequesters[2]),
          onChange = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateAccountNumber(it))
          },
        )
      }
    }
  }
  if (showBottomSheet) {
    ModalBottomSheet(
      onDismissRequest = {
        showBottomSheet = false
      },
      sheetState = sheetState,
      modifier = Modifier
        .fillMaxHeight()
        .focusRequester(focusRequesters[1])
    ) {
      BankSelectList(
        doBankSelect = { bankCode ->
          Log.e(",", "bankCode = $bankCode")
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateBankName(workerJoinViewModel.bankName[workerJoinViewModel.bankCode.indexOf(bankCode)]))
          focusRequesters[2].requestFocus()
          showBottomSheet = false
        },
        doClose = {
          showBottomSheet = false
        }
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun JoinPage3ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    WorkerJoinPage3Screen(
      workerJoinViewModel = WorkerJoinSharedViewModel(), // ViewModel 직접 생성
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}