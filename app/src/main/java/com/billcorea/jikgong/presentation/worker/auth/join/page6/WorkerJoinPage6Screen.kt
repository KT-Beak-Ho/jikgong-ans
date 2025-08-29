package com.billcorea.jikgong.presentation.worker.auth.join.page6

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.network.WorkExperience
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.LabelText
import com.billcorea.jikgong.presentation.destinations.JikgongAppDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage5ScreenDestination
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonBottomSheetInput
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonWorkerTopBar
import com.billcorea.jikgong.presentation.worker.auth.common.constants.WorkerJoinConstants
import com.billcorea.jikgong.presentation.worker.auth.join.page6.components.JobSelectList
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
fun WorkerJoinPage6Screen(
  workerJoinViewModel: WorkerJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by workerJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateHome by workerJoinViewModel.shouldNavigateHome.collectAsStateWithLifecycle()
  val shouldNavigateBack by workerJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

  var showBottomSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
  val focusManager = LocalFocusManager.current
  val config = LocalConfiguration.current
  val screenWidth = config.screenWidthDp

  var showRegistrationDialog by remember { mutableStateOf(false) }

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin6Flow)
  }

  // 네비게이션 이벤트 처리 - 홈으로
  LaunchedEffect(shouldNavigateHome) {
    if (shouldNavigateHome) {
      navigator.navigate(JikgongAppDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(WorkerJoinPage5ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

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
      //  회원가입 완료 버튼
      CommonButton(
        text = stringResource(R.string.joinMember),
        onClick = {
          showRegistrationDialog = true
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(WindowInsets.navigationBars.asPaddingValues())
          .padding(horizontal = 16.dp, vertical = 8.dp)
      )
    }
  ) { innerPadding ->
    //  메인화면 시작
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      // 헤더
      item {
        Text(
          text = "직종 및 경력 정보를 입력해주세요",
          color = appColorScheme.primary,
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
        )
      }

      // 직종 선택
      item {
        LabelText(
          mainText = "직종",
          appendText = "",
          appendTextColor = appColorScheme.error
        )

        Spacer(modifier = Modifier.padding(5.dp))

        CommonBottomSheetInput(
          value = uiState.selectedJobName,
          placeholderText = stringResource(R.string.msgSelectJob),
          errorKey = "jobName",
          validationErrors = uiState.validationErrors,
          iconPainter = painterResource(R.drawable.ic_keyboard_arrow_down_24dp),
          onClick = {
            showBottomSheet = true
          }
        )
      }

      // 경력 입력
      item {
        LabelText(
          mainText = "경력",
          appendText = "",
          appendTextColor = appColorScheme.error
        )

        Text(
          text = "해당 직종의 경력을 입력해주세요 (개월 단위)",
          color = appColorScheme.onSurfaceVariant,
          style = AppTypography.bodySmall
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          CommonTextInput(
            value = uiState.yearInput,
            labelMainText = "년",
            labelAppendText = "",
            labelAppendTextColor = colorResource(R.color.secondary_B),
            placeholder = "",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
              .width((screenWidth * .44).dp),
              // .padding(WindowInsets.navigationBars.asPaddingValues()),
            onChange = { newValue ->
              if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateYearInput(newValue))
              }
            }
          )

          CommonTextInput(
            value = uiState.monthInput,
            labelMainText = "월",
            placeholder = "",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
              .width((screenWidth * .44).dp),
              // .padding(WindowInsets.navigationBars.asPaddingValues()),
            onChange = { newValue ->
              if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateMonthInput(newValue))
              }
            }
          )
        }
      }

      // 추가하기 버튼
      item {
        CommonButton(
          text = "경력 추가하기",
          onClick = {
            val totalMonths = (uiState.yearInput.toIntOrNull() ?: 0) * 12 + (uiState.monthInput.toIntOrNull() ?: 0)
            if (totalMonths > 0) {
              workerJoinViewModel.onEvent(
                WorkerJoinSharedEvent.AddWorkExperience(
                  WorkExperience(
                    tech = uiState.selectedJobCode,
                    experienceMonths = totalMonths
                  )
                )
              )
              // 입력 필드 초기화
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ClearJobInput)
              focusManager.clearFocus()
            }
          },
          enabled = uiState.selectedJobName.isNotEmpty() &&
            uiState.yearInput.isNotEmpty() &&
            uiState.monthInput.isNotEmpty(),
          modifier = Modifier.fillMaxWidth()
        )
      }

      // 추가된 경력 리스트
      items(uiState.workExperienceList) { experience ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
          Row(
            modifier = Modifier
              .padding(16.dp)
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "직종: ${experience.tech}",
              style = AppTypography.bodyMedium
            )
            Text(
              text = "경력: ${experience.experienceMonths}개월",
              style = AppTypography.bodyMedium
            )
            TextButton(
              onClick = {
                workerJoinViewModel.onEvent(
                  WorkerJoinSharedEvent.RemoveWorkExperience(experience)
                )
              }
            ) {
              Text("삭제", color = appColorScheme.error)
            }
          }
        }
      }
    }

    // 직종 선택 바텀시트
    if (showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = { showBottomSheet = false },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight()
      ) {
        JobSelectList(
          doJobSelect = { jobCode ->
            Log.e("", "jobCode = $jobCode")
            val jobName = workerJoinViewModel.getJobNameByCode(jobCode)
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateSelectedJob(jobCode, jobName))
            showBottomSheet = false
          },
          doClose = { showBottomSheet = false }
        )
      }
    }

    // 회원가입 확인 다이얼로그
    if (showRegistrationDialog) {
      AlertDialog(
        onDismissRequest = { showRegistrationDialog = false },
        title = { Text("회원가입 완료") },
        text = { Text("입력하신 정보로 회원가입을 진행하시겠습니까?") },
        confirmButton = {
          TextButton(
            onClick = {
              showRegistrationDialog = false
              // 회원가입 API 호출
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.SubmitRegistration)
            }
          ) {
            Text("확인")
          }
        },
        dismissButton = {
          TextButton(
            onClick = { showRegistrationDialog = false }
          ) {
            Text("취소")
          }
        }
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WorkerJoinPage6ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    WorkerJoinPage6Screen(
      workerJoinViewModel = WorkerJoinSharedViewModel(),
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}