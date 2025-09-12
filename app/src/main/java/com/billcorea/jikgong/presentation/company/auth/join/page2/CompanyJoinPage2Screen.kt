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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.page2.components.PhoneNumberDisplay
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedUiState
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage3ScreenDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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

  CompanyJoinPage2ScreenContent(
    uiState = uiState,
    onEvent = companyJoinViewModel::onEvent,
    modifier = modifier
  )
}

@Composable
fun CompanyJoinPage2ScreenContent(
  uiState: CompanyJoinSharedUiState,
  onEvent: (CompanyJoinSharedEvent) -> Unit,
  modifier: Modifier = Modifier
) {
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
          onEvent(CompanyJoinSharedEvent.PreviousPage)
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
          onEvent(CompanyJoinSharedEvent.NextPage)
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
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            onEvent(CompanyJoinSharedEvent.UpdateUserName(it))
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
                onEvent(CompanyJoinSharedEvent.UpdateUserId(it))
              }
            )
            Button(
              onClick = {
                onEvent(CompanyJoinSharedEvent.RequestVerificationID)
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
          placeholder = "8자리 이상 입력",
          validationError = uiState.validationErrors["password"],
          isPassword = true,  // 비밀번호 모드 활성화
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            onEvent(CompanyJoinSharedEvent.UpdateUserPassword(it))
          }
        )
      }
      
      //  비밀번호 확인
      item {
        CommonTextInput(
          value = uiState.passwordConfirm,
          labelMainText = "비밀번호 확인",
          labelAppendText = "*",
          labelAppendTextColor = colorResource(R.color.secondary_B),
          placeholder = "비밀번호 재입력",
          validationError = uiState.validationErrors["passwordConfirm"],
          isPassword = true,  // 비밀번호 모드 활성화
          modifier = Modifier.fillMaxWidth(),
          onChange = {
            onEvent(CompanyJoinSharedEvent.UpdateUserPasswordConfirm(it))
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
                onEvent(CompanyJoinSharedEvent.UpdateUserMail(it))
              }
            )
            Button(
              onClick = {
                onEvent(CompanyJoinSharedEvent.RequestVerificationEmail)
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
            onEvent(CompanyJoinSharedEvent.UpdateBusinessNumber(it))
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
            onEvent(CompanyJoinSharedEvent.UpdateCompanyName(it))
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
            onEvent(CompanyJoinSharedEvent.UpdateInquiry(it))
          }
        )
      }
    }
  }
}

@Preview(showBackground = true, name = "회원가입 2단계 - 초기 상태")
@Composable
fun JoinPage2ScreenPreview() {
  Jikgong1111Theme {
    CompanyJoinPage2ScreenContent(
      uiState = CompanyJoinSharedUiState(
        currentPage = 2,
        phoneNumber = "01012345678",
        isPhoneVerified = true
      ),
      onEvent = {}
    )
  }
}

@Preview(showBackground = true, name = "회원가입 2단계 - 입력 중")
@Composable
fun JoinPage2ScreenPreview2() {
  Jikgong1111Theme {
    CompanyJoinPage2ScreenContent(
      uiState = CompanyJoinSharedUiState(
        currentPage = 2,
        phoneNumber = "01012345678",
        isPhoneVerified = true,
        name = "홍길동",
        id = "testuser123",
        isIdAvailable = true,
        idCheckMessage = "사용 가능한 아이디입니다.",
        email = "test@example.com",
        isEmailAvailable = true,
        emailCheckMessage = "올바른 이메일 형식입니다.",
        password = "password123",
        businessNumber = "123-45-67890",
        companyName = "테스트 회사",
        inquiry = "문의사항입니다."
      ),
      onEvent = {}
    )
  }
}

@Preview(showBackground = true, name = "회원가입 2단계 - 에러 상태")
@Composable
fun JoinPage2ScreenPreview3() {
  Jikgong1111Theme {
    CompanyJoinPage2ScreenContent(
      uiState = CompanyJoinSharedUiState(
        currentPage = 2,
        phoneNumber = "01012345678",
        isPhoneVerified = true,
        id = "user",
        isIdAvailable = false,
        idCheckMessage = "이미 사용중인 아이디입니다.",
        validationErrors = mapOf(
          "name" to "이름을 입력해주세요.",
          "password" to "비밀번호는 8자 이상이어야 합니다."
        )
      ),
      onEvent = {}
    )
  }
}