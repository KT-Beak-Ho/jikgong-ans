package com.billcorea.jikgong.presentation.worker.auth.join.page2

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.datepicker.WheelDatePicker
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.LabelText
import com.billcorea.jikgong.presentation.company.auth.join.page2.components.PhoneNumberDisplay
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage3ScreenDestination
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonBottomSheetInput
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonWorkerTopBar
import com.billcorea.jikgong.presentation.worker.auth.common.constants.WorkerJoinConstants
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedEvent
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Destination
@Composable
fun WorkerJoinPage2Screen(
  workerJoinViewModel: WorkerJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by workerJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateToNextPage by workerJoinViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
  val shouldNavigateBack by workerJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

  var showBottomSheet by remember { mutableStateOf(false) }
  val focusManager = LocalFocusManager.current
  val focusRequester = FocusRequester()
  val config = LocalConfiguration.current
  val screenWeight = config.screenWidthDp
  val screenHeight = config.screenHeightDp

  // FocusRequester들 생성
  /**
   * 이거 테스트 해봐야하는데 컴으로 하면 엔터누르면 담으로 넘어가서 폰으로 해봐야할거같은데
   * 어케하누 ?
   */
  val focusRequesters = remember {
    List(7) { FocusRequester() }
//    listOf(
//      FocusRequester(), // name
//      FocusRequester(), // id
//      FocusRequester(), // password
//      FocusRequester(), // email
//      FocusRequester(), // businessNumber
//      FocusRequester(), // companyName
//      FocusRequester()  // inquiry
//    )
  }

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin2Flow)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(WorkerJoinPage3ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(WorkerJoinPage1ScreenDestination)
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
      //  다음 버튼
      CommonButton(
        text = stringResource(R.string.next),
        onClick = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.NextPage)
        },
        enabled = uiState.isPage2Complete,
        modifier = Modifier
          .fillMaxWidth()
          .padding(WindowInsets.navigationBars.asPaddingValues())
          .padding(horizontal = 16.dp, vertical = 8.dp)
      )
    }
  ) { innerPadding ->
    //  메인화면 시작
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp),  //  각 item별 공통으로 간격 적용
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      //  필수정보 입력 메시지
      item {
        Text(
          text = stringResource(R.string.enterYourInfo),
          color = appColorScheme.primary,
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
        )
      }
      //  page 1에서 등록한 휴대폰 번호 출력
      item {
        PhoneNumberDisplay(
          phoneNumber = uiState.phoneNumber,
        )
      }
      //  이름
      item {
        CommonTextInput(
          value = uiState.name,
          labelMainText = stringResource(R.string.name),
          labelAppendText = "*",
          labelAppendTextColor = colorResource(R.color.secondary_B),
          placeholder = stringResource(R.string.enterName),
          validationError = uiState.validationErrors["name"],
          keyboardActions = KeyboardActions(onNext = {
            focusRequesters[1].requestFocus()
            // keyboardController?.hide()
          }),
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequesters[0]),
          onChange = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateUserName(it))
          },
        )
      }
      //  아이디
      item {
        CommonTextInput(
          value = uiState.id,
          labelMainText = stringResource(R.string.id),
          labelAppendText = "*",
          labelAppendTextColor = colorResource(R.color.secondary_B),
          placeholder = stringResource(R.string.enterId),
          validationError = uiState.validationErrors["id"],
          keyboardActions = KeyboardActions(onNext = {
            focusRequesters[2].requestFocus()
            // keyboardController?.hide()
          }),
          modifier = Modifier.fillMaxWidth().focusRequester(focusRequesters[1]),
          onChange = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateUserId(it))
          }
        )
      }
      //  비밀번호
      item {
        CommonTextInput(
          value = uiState.password,
          labelMainText = stringResource(R.string.password),
          labelAppendText = "*",
          labelAppendTextColor = colorResource(R.color.secondary_B),
          placeholder = stringResource(R.string.enterPassword),
          validationError = uiState.validationErrors["password"],
          keyboardActions = KeyboardActions(onNext = {
            focusRequesters[3].requestFocus()
            // keyboardController?.hide()
          }),
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateUserPassword(it))
          }
        )
      }
      //  메일
      item {
        CommonTextInput(
          value = uiState.email,
          labelMainText = stringResource(R.string.email),
          labelAppendText = "*",
          labelAppendTextColor = colorResource(R.color.secondary_B),
          placeholder = stringResource(R.string.enterEmail),
          validationError = uiState.validationErrors["email"],
          keyboardActions = KeyboardActions(onNext = {
            focusRequesters[4].requestFocus()
            // keyboardController?.hide()
          }),
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateUserMail(it))
          }
        )
      }

      item {
        // 라벨
        LabelText(
          mainText = stringResource(R.string.birthday),
          appendText = "*",
          appendTextColor = colorResource(R.color.secondary_B)
        )

        Spacer(modifier = Modifier.padding(5.dp))

        // 선택 가능한 필드
        CommonBottomSheetInput(
          value = uiState.birthday,
          placeholderText = stringResource(R.string.checkBirthDay),
          errorKey = "birthday",
          validationErrors = uiState.validationErrors,
          iconPainter = painterResource(R.drawable.ic_keyboard_arrow_down_24dp),
          onClick = {
            showBottomSheet = true
          }
        )
      }

      item {
        LabelText(
          mainText = stringResource(R.string.gender),
          appendText = "*",
          appendTextColor = colorResource(R.color.secondary_B)
        )

        Spacer(modifier = Modifier.padding(5.dp))
        
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(
            onClick = {
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateGender("MALE"))
              focusManager.clearFocus()
            },
            modifier = Modifier
              .weight(1f)
              .border(
                width = if (uiState.gender == "MALE") 3.dp else 1.dp,
                color = if (uiState.gender == "MALE") appColorScheme.primary else appColorScheme.outline
              )
              .focusRequester(focusRequester)
          ) {
            Text(
              text = stringResource(R.string.male),
              style = AppTypography.bodyMedium.copy(
                color = if (uiState.gender == "MALE") appColorScheme.primary else appColorScheme.outline
              ),
              modifier = Modifier.padding(6.dp)
            )
          }

          TextButton(
            onClick = {
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateGender("FEMALE"))
              focusManager.clearFocus()
            },
            modifier = Modifier
              .weight(1f)
              .border(
                width = if (uiState.gender == "FEMALE") 3.dp else 1.dp,
                color = if (uiState.gender == "FEMALE") appColorScheme.primary else appColorScheme.outline
              )
          ) {
            Text(
              text = stringResource(R.string.female),
              style = AppTypography.bodyMedium.copy(
                color = if (uiState.gender == "FEMALE") appColorScheme.primary else appColorScheme.outline
              ),
              modifier = Modifier.padding(6.dp)
            )
          }
        }
      }
      item {
        LabelText(
          mainText = stringResource(R.string.nationality),
          appendText = "*",
          appendTextColor = colorResource(R.color.secondary_B)
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(
            onClick = {
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateNationality("KOREAN"))
              focusManager.clearFocus()
            },
            modifier = Modifier
              .weight(1f)
              .border(
                width = if (uiState.nationality == "KOREAN") 3.dp else 1.dp,
                color = if (uiState.nationality == "KOREAN") appColorScheme.primary else appColorScheme.outline
              )
              .focusRequester(focusRequester)
          ) {
            Text(
              text = stringResource(R.string.local),
              style = AppTypography.bodyMedium.copy(
                color = if (uiState.gender == "KOREAN") appColorScheme.primary else appColorScheme.outline
              ),
              modifier = Modifier.padding(6.dp)
            )
          }

          TextButton(
            onClick = {
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateNationality("FOREIGN"))
              focusManager.clearFocus()
            },
            modifier = Modifier
              .weight(1f)
              .border(
                width = if (uiState.nationality == "FOREIGN") 3.dp else 1.dp,
                color = if (uiState.nationality == "FOREIGN") appColorScheme.primary else appColorScheme.outline
              )
          ) {
            Text(
              text = stringResource(R.string.foreigner),
              style = AppTypography.bodyMedium.copy(
                color = if (uiState.nationality == "FOREIGN") appColorScheme.primary else appColorScheme.outline
              ),
              modifier = Modifier.padding(6.dp)
            )
          }
        }
      }
    }
  }
  if (showBottomSheet) {
    BottomSheetDialog(
      onDismissRequest = {
        Log.d("[BottomSheetDialog]", "onDismissRequest")
        showBottomSheet = false
        focusManager.clearFocus()
        // focusRequester.requestFocus()
      },
      properties = BottomSheetDialogProperties(
        behaviorProperties = BottomSheetBehaviorProperties(
        )
      ),
    ) {
      Column( modifier = Modifier.fillMaxWidth()) {
        WheelDatePicker(
          startDate = LocalDate.parse(uiState.birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
          rowCount = 5,
          size = DpSize(400.dp, 300.dp),
          textStyle = AppTypography.bodyMedium,
          selectorProperties = WheelPickerDefaults.selectorProperties(
            enabled = true,
            shape = MaterialTheme.shapes.medium,
            color = appColorScheme.surfaceDim,
            border = BorderStroke(2.dp, appColorScheme.outline)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .background(appColorScheme.secondaryContainer)
        ) { snappedDate ->
          Log.e("", "date ${snappedDate.year}, ${snappedDate.month.value}, ${snappedDate.dayOfMonth}")
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateBirthday(LocalDate.of(snappedDate.year, snappedDate.month.value, snappedDate.dayOfMonth).toString()))
        }
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(
            onClick = {
              showBottomSheet = false
              focusManager.clearFocus()
            },
            modifier = Modifier
              .width((screenWeight * .4).dp)
              .background(appColorScheme.secondary)
          ) {
            Text(
              text = stringResource(R.string.Cancel),
              style = AppTypography.bodyMedium.copy(appColorScheme.onSecondary)
            )
          }
          TextButton(
            onClick = {
              showBottomSheet = false
              focusManager.clearFocus()
              // focusRequester.requestFocus()
            },
            modifier = Modifier
              .width((screenWeight * .6).dp)
              .background(appColorScheme.primary)
          ) {
            Text(
              text = stringResource(R.string.OK),
              style = AppTypography.bodyMedium.copy(appColorScheme.onPrimary)
            )
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun JoinPage2ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    WorkerJoinPage2Screen(
      workerJoinViewModel = WorkerJoinSharedViewModel(), // ViewModel 직접 생성
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}