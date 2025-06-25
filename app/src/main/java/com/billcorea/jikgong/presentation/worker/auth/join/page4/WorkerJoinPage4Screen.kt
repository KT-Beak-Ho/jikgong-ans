package com.billcorea.jikgong.presentation.worker.auth.join.page4

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.network.AddressFindRoadAddress
import com.billcorea.jikgong.presentation.JoinPage4
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.destinations.KakaoMapViewDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonWorkerTopBar
import com.billcorea.jikgong.presentation.worker.auth.common.constants.WorkerJoinConstants
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedEvent
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun WorkerJoinPage4Screen(
  workerJoinViewModel: WorkerJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by workerJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateToNextPage by workerJoinViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
  val shouldNavigateBack by workerJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

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
    workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin4Flow)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(WorkerJoinPage1ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(WorkerJoinPage3ScreenDestination)
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
        enabled = uiState.isPage4Complete,
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
          text = stringResource(R.string.enterMainLocation),
          color = appColorScheme.primary,
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
        )
      }

      item {
        Row(
          modifier = Modifier.fillMaxWidth().padding(5.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (uiState.address.isNotEmpty()) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, appColorScheme.outline)
                .padding(5.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (uiState.address.isEmpty()) stringResource(R.string.msgSearchLocation) else uiState.address.toString(),
                color = appColorScheme.primary,
                lineHeight = 1.25.em,
                style = AppTypography.bodyMedium,
              )
              IconButton(onClick = {
//                        val intent = Intent(context, AddressFindActivity::class.java)
//                        getPostNo.launch(intent)
                // 초기화
                // viewModel._respAddress.value = ""
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin4Flow)
              }) {
                Image(
                  imageVector = Icons.Default.Edit,
                  contentDescription = "Search Location"
                )
              }
            }
          } else {
            OutlinedTextField(
              value = uiState.addressName,
              onValueChange = {
                //초기화
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateAddressName(it)) // addressName
              },
              placeholder = {
                Text(text = stringResource(R.string.msgSearchLocation))
              },
              keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
              ),
              keyboardActions = KeyboardActions(onNext = {

              }),
              maxLines = 1,
              trailingIcon = {
                IconButton(onClick = {
//                        val intent = Intent(context, AddressFindActivity::class.java)
//                        getPostNo.launch(intent)
                  if (uiState.addressName != "") {
                    try {
                      // 함수
                      workerJoinViewModel.onEvent(WorkerJoinSharedEvent.KakaoGeocoding(uiState.addressName))
                    } catch (e: Exception) {
                      Log.e("JoinPage4", "Geocoding error: ${e.message}")
                      //errorMessage = "주소 검색 중 오류가 발생했습니다."
                      //showErrorDialog = true
                    }
                  }
                }) {
                  Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Location"
                  )
                }
              },
              modifier = Modifier
                .fillMaxWidth()
            )
          }
        }
      }

      item {
        IconButton(
          onClick = {
            navigator.navigate(KakaoMapViewDestination)
          },
          modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(1.dp, appColorScheme.outline)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.MyLocation,
              contentDescription = "My Location"
            )
            Spacer(Modifier.padding(3.dp))
            Text(
              text = stringResource(R.string.selectMylocation),
              style = AppTypography.bodyMedium
            )
          }
        }
      }
      if (uiState.address.isEmpty()) {
        // null 체크를 추가하여 안전하게 처리
        val safeRoadAddress = uiState.roadAddress.filterNotNull()
        itemsIndexed(safeRoadAddress) { index, item ->
          DisplayAddress(item, doSetCenterPosition = {
            // 업데이트 함수
            // uiState.respAddress = item.addressName
            // viewModel._geoCoding.value = "${item.y},${item.x}"
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateAddress(item.addressName))
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateLat((item.x).toDouble()))
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateLon((item.y).toDouble()))
            // Log.e("", "geoCoding=${viewModel._geoCoding.value}")
          })
        }
      }
    }
  }
}

@Composable
fun DisplayAddress(
  item: AddressFindRoadAddress,
  doSetCenterPosition: (item: AddressFindRoadAddress) -> Unit = {}
) {
  TextButton(
    onClick = {
      doSetCenterPosition(item)
    }, modifier = Modifier
      .padding(3.dp)
      .border(1.dp, appColorScheme.outline)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = item.addressName,
        lineHeight = 1.25.em,
        style = AppTypography.titleMedium,
      )
      Row(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = item.roadName,
          style = AppTypography.bodyMedium,
        )
        if (item.buildingName.isNotEmpty()) {
          Spacer(modifier = Modifier.padding(3.dp))
          Text(
            text = String.format("(%s)", item.buildingName),
            style = AppTypography.bodyMedium,
          )
        }
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
          text = item.zoneNo,
          style = AppTypography.bodyLarge,
        )
      }
    }
  }
}

@Preview
@Composable
fun JoinPage4Preview() {
  val fakeViewModel = MainViewModel()
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    JoinPage4(fakeViewModel, navigator, modifier = Modifier.padding(3.dp), mainActivity = null)
  }
}
