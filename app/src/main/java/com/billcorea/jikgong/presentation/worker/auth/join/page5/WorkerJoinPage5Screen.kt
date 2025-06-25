package com.billcorea.jikgong.presentation.worker.auth.join.page5

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.FeatureThatRequiresCameraPermission
import com.billcorea.jikgong.presentation.GlideImage
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage4ScreenDestination
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

@Destination
@Composable
fun WorkerJoinPage5Screen(
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
  val context = LocalContext.current

  // 추가된 상태 변수들
  var isGrantCamera by remember { mutableStateOf(false) }
  var _safeManagerCardUri by remember { mutableStateOf<String?>(null) }
  var _workerCardUri by remember { mutableStateOf<String?>(null) }
  var isUsePic by remember { mutableStateOf(false) }
  var isUseCamera by remember { mutableStateOf(false) }
  var isSecretOk by remember { mutableStateOf(false) }
  var takePicTy by remember { mutableStateOf("") }
  var safeCardNumber by remember { mutableStateOf("") }
  var showDialog by remember { mutableStateOf(false) }
  var showLaterDialog by remember { mutableStateOf(false) }
  val screenWidth = config.screenWidthDp

  // FocusRequester들 생성
  /**
   * 이거 테스트 해봐야하는데 컴으로 하면 엔터누르면 담으로 넘어가서 폰으로 해봐야할거같은데
   * 어케하누 ?
   */
//  val focusRequesters = remember {
//    List(7) { FocusRequester() }
//    listOf(
//      FocusRequester(), // name
//      FocusRequester(), // id
//      FocusRequester(), // password
//      FocusRequester(), // email
//      FocusRequester(), // businessNumber
//      FocusRequester(), // companyName
//      FocusRequester()  // inquiry
//    )
//  }

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin5Flow)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      // navigator.navigate(WorkerJoinPage6ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(WorkerJoinPage4ScreenDestination)
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
      //  버튼 두개
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        TextButton(
          onClick = {
            showLaterDialog = true
          },
          modifier = Modifier
            .width((screenWidth * .45).dp)
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(appColorScheme.onPrimary)
        ) {
          Text(
            text = stringResource(R.string.toLater),
            color = appColorScheme.primary,
            lineHeight = 1.25.em,
            style = AppTypography.labelMedium,
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentWidth(Alignment.CenterHorizontally)
          )
        }
        Spacer(modifier = Modifier.padding(5.dp))
        TextButton(
          onClick = {
            // TODO: SharedPreferences 처리 로직 추가 필요
            if(isSecretOk) {
              // 카드 URI 저장 로직
              // navigator.navigate(JoinPage6Destination) // 목적지 수정 필요
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.NextPage)
            }
          },
          modifier = Modifier
            .width((screenWidth * .45).dp)
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(if (isSecretOk) appColorScheme.primary else appColorScheme.inversePrimary)
        ) {
          Text(
            text = stringResource(R.string.completed),
            color = if (isSecretOk) appColorScheme.onPrimary else appColorScheme.surfaceDim,
            lineHeight = 1.25.em,
            style = AppTypography.labelMedium,
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentWidth(Alignment.CenterHorizontally)
          )
        }
      }
    }
  ) { innerPadding ->
    //  메인화면 시작
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp),  //  각 item별 공통으로 간격 적용
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    )
    {
      // 내용 추가
      item {
        Text(
          text = stringResource(R.string.lastTwoRegister),
          color = appColorScheme.primary,
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Image(
          painter = painterResource(id = R.drawable.joinpage5_guide_v1),
          contentDescription = null,
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
          text = stringResource(R.string.safeManagerCard),
          color = appColorScheme.primary,
          lineHeight = 1.25.em,
          style = AppTypography.bodyMedium,
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        Spacer(modifier = Modifier.padding(5.dp))
        FeatureThatRequiresCameraPermission(doResult = { isGrantCamera = it })
        if (isGrantCamera) {

          if (_safeManagerCardUri != null) {
            Card(
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary),
            ) {
              GlideImage(_safeManagerCardUri.toString(), doRefresh = {
                _safeManagerCardUri = null
                isUsePic = false
                isUseCamera = false
                if (_workerCardUri == null) {
                  isSecretOk = false
                }
              })
            }

          } else {
            IconButton(
              onClick = {
                takePicTy = "safeManagerCard"
                showBottomSheet = true
              },
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary),
            ) {
              Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_camera_v1),
                  contentDescription = null,
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                  text = stringResource(R.string.msgImageSet),
                  style = AppTypography.bodyMedium.copy(
                    appColorScheme.primary
                  )
                )
              }
            }
          }
        }
      }

      if (_safeManagerCardUri != null) {
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            OutlinedTextField(
              value = safeCardNumber,
              onValueChange = {
                safeCardNumber = it
              },
              placeholder = {
                Text(text = stringResource(R.string.enterForNumberOnly))
              },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
              keyboardActions = KeyboardActions(onDone = {

              }),
              maxLines = 1,
              modifier = Modifier
                .width((screenWidth * .67).dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->

                }
            )
            Spacer(modifier = Modifier.padding(2.dp))
            TextButton( onClick = {
              showDialog = true
            }, modifier = Modifier
              .width((screenWidth * .3).dp)
              .background(appColorScheme.primary)) {
              Text(
                text = stringResource(R.string.OK),
                color = appColorScheme.onPrimary,
                lineHeight = 1.25.em,
                style = AppTypography.labelMedium,
                modifier = Modifier
                  .fillMaxWidth()
                  .wrapContentWidth(Alignment.CenterHorizontally)
              )
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
          text = stringResource(R.string.workerCard),
          color = appColorScheme.primary,
          lineHeight = 1.25.em,
          style = AppTypography.bodyMedium,
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        Spacer(modifier = Modifier.padding(5.dp))
        FeatureThatRequiresCameraPermission(doResult = { isGrantCamera = it })
        if (isGrantCamera) {
          if (_workerCardUri != null) {
            Card(
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary),
            ) {
              GlideImage(_workerCardUri.toString(), doRefresh = {
                _workerCardUri = null
                isUsePic = false
                isUseCamera = false
                if (_safeManagerCardUri == null) {
                  isSecretOk = false
                }
              })
            }
          } else {
            IconButton(
              onClick = {
                takePicTy = "workerCard"
                showBottomSheet = true
              },
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary),
            ) {
              Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_camera_v1),
                  contentDescription = null,
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                  text = stringResource(R.string.msgImageSet),
                  style = AppTypography.bodyMedium.copy(
                    appColorScheme.primary
                  )
                )
              }
            }
          }
        }
      }
    }

    // AlertDialog들
    if (showDialog) {
      AlertDialog(
        onDismissRequest = { showDialog = false },
        title = {
          Text(text = stringResource(R.string.msgCertified))
        },
        confirmButton = {
          TextButton(
            onClick = { showDialog = false }
          ) {
            Text(stringResource(R.string.OK))
          }
        }
      )
    }

    if (showLaterDialog) {
      AlertDialog(
        onDismissRequest = { showLaterDialog = false },
        title = {
          Text(text = stringResource(R.string.msgToLater))
        },
        confirmButton = {
          TextButton(
            onClick = {
              showLaterDialog = false
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.NextPage)
            }
          ) {
            Text(stringResource(R.string.OK))
          }
        },
        dismissButton = {
          TextButton(
            onClick = { showLaterDialog = false }
          ) {
            Text(stringResource(android.R.string.cancel))
          }
        }
      )
    }
  }
}


@Preview(showBackground = true)
@Composable
fun JoinPage5ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    WorkerJoinPage5Screen(
      workerJoinViewModel = WorkerJoinSharedViewModel(), // ViewModel 직접 생성
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}