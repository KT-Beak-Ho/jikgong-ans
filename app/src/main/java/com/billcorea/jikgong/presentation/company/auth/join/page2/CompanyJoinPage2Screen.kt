package com.billcorea.jikgong.presentation.company.auth.join.page2

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.page2.components.PhoneNumberDisplay
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
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

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ResetJoin2Flow)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      //  navigator.navigate(CompanyJoinPage3ScreenDestination)
      companyJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
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
        text = stringResource(R.string.next),
        onClick = {
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
        Spacer(modifier = Modifier.padding(8.dp))
      }
      //  page 1에서 등록한 휴대폰 번호 출력
      item {
        PhoneNumberDisplay(
          phoneNumber = uiState.phoneNumber,
        )
        Spacer(modifier = Modifier.padding(8.dp))
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
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserName(it))
          },

        )
        Spacer(modifier = Modifier.padding(8.dp))
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
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserId(it))
          }
        )
        Spacer(modifier = Modifier.padding(8.dp))
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
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserPassword(it))
          }
        )
        Spacer(modifier = Modifier.padding(8.dp))
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
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserMail(it))
          }
        )
        Spacer(modifier = Modifier.padding(8.dp))
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
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateBusinessNumber(it))
          }
        )
        Spacer(modifier = Modifier.padding(8.dp))
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
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateCompanyName(it))
          }
        )
        Spacer(modifier = Modifier.padding(8.dp))
      }
      //  문의 사항
      item {
        CommonTextInput(
          value = uiState.inquiry,
          labelMainText = stringResource(R.string.inquiry),
          placeholder = stringResource(R.string.enterInquiry),
          minLines = 3,
          validationError = uiState.validationErrors["inquiry"],
          onChange = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateInquiry(it))
          }
        )
        Spacer(modifier = Modifier.padding(8.dp))
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
    CompanyJoinPage2Screen(
      companyJoinViewModel = CompanyJoinSharedViewModel(), // ViewModel 직접 생성
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}