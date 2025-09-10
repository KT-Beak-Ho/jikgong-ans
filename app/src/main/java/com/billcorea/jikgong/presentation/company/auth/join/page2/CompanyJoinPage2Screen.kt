package com.billcorea.jikgong.presentation.company.auth.join.page2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.api.models.common.ApiResult
import com.billcorea.jikgong.api.models.auth.EmailValidationResponse
import com.billcorea.jikgong.api.models.auth.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.auth.PhoneValidationResponse
import com.billcorea.jikgong.api.models.auth.SmsVerificationResponse
import com.billcorea.jikgong.api.repository.join.JoinRepository
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.page2.components.PhoneNumberDisplay
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage3ScreenDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyJoinPage2Screen(
  companyJoinViewModel: CompanyJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by companyJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateToNextPage by companyJoinViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
  val shouldNavigateBack by companyJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

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
    //  로컬 변수 초기화
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ResetJoin2Flow)
    //  에러 변수 초기화
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(CompanyJoinPage3ScreenDestination)
      companyJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(CompanyJoinPage1ScreenDestination)
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
      //  다음 버튼
      CommonButton(
        text = stringResource(R.string.business_partnership_application),
        onClick = {
          //  여기에 사용자 ID, Email 등록여부 검증 로직 추가 필요함
          //  NextPage 함수 정의에 들어가서 currentPage=2 인 경우 위 두 함수 실행하는 로직 추가하면될듯
          companyJoinViewModel.onEvent(CompanyJoinSharedEvent.NextPage)
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
          modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequesters[0]),
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserName(it))
          },
        )
      }
      //  아이디
      item {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            CommonTextInput(
              value = uiState.id,
              labelMainText = stringResource(R.string.id),
              labelAppendText = "*",
              labelAppendTextColor = colorResource(R.color.secondary_B),
              placeholder = stringResource(R.string.enterId),
              validationError = uiState.validationErrors["id"],
              modifier = Modifier.weight(1f),
              onChange = {
                companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserId(it))
              }
            )
            Button(
              onClick = {
                companyJoinViewModel.onEvent(CompanyJoinSharedEvent.RequestVerificationID)
              },
              enabled = uiState.id.isNotEmpty() && !uiState.isWaiting,
              modifier = Modifier.align(Alignment.Bottom).padding(bottom = if(uiState.validationErrors["id"] != null) 20.dp else 0.dp)
            ) {
              if (uiState.isWaiting) {
                CircularProgressIndicator(
                  modifier = Modifier.size(16.dp),
                  color = Color.White
                )
              } else {
                Text("중복 확인")
              }
            }
          }
          // ID 중복 확인 메시지 표시
          uiState.idCheckMessage?.let { message ->
            Text(
              text = message,
              color = if (uiState.isIdAvailable) Color(0xFF4CAF50) else Color(0xFFE53935),
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
          }
        }
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
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserPassword(it))
          }
        )
      }
      //  메일
      //  이메일
      item {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            CommonTextInput(
              value = uiState.email,
              labelMainText = stringResource(R.string.email),
              labelAppendText = "*",
              labelAppendTextColor = colorResource(R.color.secondary_B),
              placeholder = stringResource(R.string.enterEmail),
              validationError = uiState.validationErrors["email"],
              modifier = Modifier.weight(1f),
              onChange = {
                companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserMail(it))
              }
            )
            Button(
              onClick = {
                companyJoinViewModel.onEvent(CompanyJoinSharedEvent.RequestVerificationEmail)
              },
              enabled = uiState.email.isNotEmpty(),
              modifier = Modifier.align(Alignment.Bottom).padding(bottom = if(uiState.validationErrors["email"] != null) 20.dp else 0.dp)
            ) {
              Text("형식 확인")
            }
          }
          // Email 형식 확인 메시지 표시
          uiState.emailCheckMessage?.let { message ->
            Text(
              text = message,
              color = if (uiState.isEmailAvailable) Color(0xFF4CAF50) else Color(0xFFE53935),
              style = MaterialTheme.typography.bodySmall,
              modifier = Modifier.padding(top = 4.dp, start = 8.dp)
            )
          }
        }
      }
      //  사업자 등록 번호
      item {
        CommonTextInput(
          value = uiState.businessNumber,
          labelMainText = stringResource(R.string.businessNumber),
          labelAppendText = "*",
          labelAppendTextColor = colorResource(R.color.secondary_B),
          placeholder = stringResource(R.string.enterNusinessNumber),
          validationError = uiState.validationErrors["businessNumber"],
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateBusinessNumber(it))
          }
        )
      }
      //  회사명
      item {
        CommonTextInput(
          value = uiState.companyName,
          labelMainText = stringResource(R.string.companyName),
          labelAppendText = "*",
          labelAppendTextColor = colorResource(R.color.secondary_B),
          placeholder = stringResource(R.string.enterCompanyName),
          validationError = uiState.validationErrors["companyName"],
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateCompanyName(it))
          }
        )
      }
      //  문의 사항
      item {
        CommonTextInput(
          value = uiState.inquiry,
          labelMainText = stringResource(R.string.inquiry),
          placeholder = stringResource(R.string.enterInquiry),
          maxLines = 3,
          validationError = uiState.validationErrors["inquiry"],
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateInquiry(it))
          }
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

  Jikgong1111Theme {
    // Preview 모드에서는 실제 ViewModel 없이 화면만 렌더링
    // CompanyJoinPage2Screen 함수는 기본적으로 Koin이 ViewModel을 주입하므로
    // Preview에서는 주석 처리
    // CompanyJoinPage2Screen(
    //   navigator = navigator,
    //   modifier = Modifier.padding(3.dp)
    // )
  }
}