package com.billcorea.jikgong.presentation.company.auth.join.page1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.common.components.KeyboardConstants
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedUiState
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage2ScreenDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyJoinPage1Screen(
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
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ResetJoin1Flow)
    //  에러 변수 초기화
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(CompanyJoinPage2ScreenDestination)
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

  /** 에러 다이얼로그 처리 */
  uiState.errorMessage?.let { message ->
    AlertDialog(
      onDismissRequest = {
        companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
      },
      title = { Text("알림") },
      text = { Text(message) },
      confirmButton = {
        TextButton(
          onClick = {
            companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
          }
        ) {
          Text("확인")
        }
      }
    )
  }

  CompanyJoinPage1ScreenContent(
    uiState = uiState,
    onEvent = companyJoinViewModel::onEvent,
    modifier = modifier
  )
}

@Preview(showBackground = true, name = "회원가입 1단계 - 초기 상태")
@Composable
fun JoinPage1ScreenPreview() {
  Jikgong1111Theme {
    CompanyJoinPage1ScreenContent(
      uiState = CompanyJoinSharedUiState(
        currentPage = 1,
        phoneNumber = "",
        isValidPhoneNumber = false,
        isSecurityStepActive = false
      ),
      onEvent = {}
    )
  }
}

@Preview(showBackground = true, name = "회원가입 1단계 - 인증번호 입력")
@Composable
fun JoinPage1ScreenPreview2() {
  Jikgong1111Theme {
    CompanyJoinPage1ScreenContent(
      uiState = CompanyJoinSharedUiState(
        currentPage = 1,
        phoneNumber = "01012345678",
        isValidPhoneNumber = true,
        isSecurityStepActive = true,
        verificationCode = "123456",
        authCode = "123456"
      ),
      onEvent = {}
    )
  }
}

@Preview(showBackground = true, name = "회원가입 1단계 - 인증 완료")
@Composable
fun JoinPage1ScreenPreview3() {
  Jikgong1111Theme {
    CompanyJoinPage1ScreenContent(
      uiState = CompanyJoinSharedUiState(
        currentPage = 1,
        phoneNumber = "01012345678",
        isValidPhoneNumber = true,
        isSecurityStepActive = true,
        verificationCode = "123456",
        authCode = "123456",
        isPhoneVerified = true
      ),
      onEvent = {}
    )
  }
}

@Composable
fun CompanyJoinPage1ScreenContent(
  uiState: CompanyJoinSharedUiState,
  onEvent: (CompanyJoinSharedEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  // 타이머 상태 관리
  var remainingTime by remember { mutableStateOf(0) }
  var isTimerRunning by remember { mutableStateOf(false) }
  
  // 타이머 시작 시 3분(180초) 카운트다운
  LaunchedEffect(isTimerRunning) {
    if (isTimerRunning) {
      remainingTime = 180
      while (remainingTime > 0 && isTimerRunning) {
        delay(1000)
        remainingTime--
      }
      isTimerRunning = false
    }
  }
  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      CommonTopBar(
        currentPage = uiState.currentPage,
        totalPages = JoinConstants.TOTAL_PAGES,
        onBackClick = {
          onEvent(CompanyJoinSharedEvent.PreviousPage)
        }
      )
    },
    bottomBar = {
      Column {
        androidx.compose.material3.Divider(
          thickness = 1.dp,
          color = MaterialTheme.colorScheme.outlineVariant
        )
        CommonButton(
          text = if (uiState.isPhoneVerified) "다음" else "인증을 완료해주세요",
          onClick = {
            onEvent(CompanyJoinSharedEvent.NextPage)
          },
          enabled = uiState.isPage1Complete,
          modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(horizontal = 20.dp)
            .padding(top = 16.dp, bottom = 16.dp)
            .height(52.dp)
        )
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 20.dp)
    ) {
      Spacer(modifier = Modifier.height(24.dp))
      
      Text(
        text = stringResource(R.string.enterPhoneNumber),
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        ),
        color = appColorScheme.onSurface
      )
      
      Spacer(modifier = Modifier.height(8.dp))
      
      Text(
        text = "본인 확인을 위해 휴대폰 인증을 진행해주세요",
        style = MaterialTheme.typography.bodyMedium,
        color = appColorScheme.onSurfaceVariant
      )
      
      Spacer(modifier = Modifier.height(24.dp))

      // 전화번호 입력 섹션
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "휴대폰 번호",
          style = MaterialTheme.typography.labelLarge,
          color = appColorScheme.onSurface,
          modifier = Modifier.padding(bottom = 8.dp)
        )
        
        CommonTextInput(
          value = uiState.phoneNumber,
          labelMainText = "",
          placeholder = "010-0000-0000",
          validationError = uiState.validationErrors["phoneNumber"],
          keyboardOptions = KeyboardConstants.Options.PHONE,
          modifier = Modifier.fillMaxWidth(),
          enabled = !uiState.isPhoneVerified,  // 인증 완료 시 비활성화
          onChange = {
            onEvent(CompanyJoinSharedEvent.UpdatePhoneNumber(it))
          },
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 인증번호 받기 버튼
        CommonButton(
          text = if (uiState.isPhoneVerified) {
            "✓ 인증 완료"
          } else if (remainingTime > 0) {
            "재전송 (${remainingTime / 60}:${String.format("%02d", remainingTime % 60)})"
          } else {
            "인증번호 받기"
          },
          onClick = {
            if (!uiState.isPhoneVerified) {
              onEvent(CompanyJoinSharedEvent.RequestVerificationCode)
              isTimerRunning = true
            }
          },
          enabled = !uiState.isPhoneVerified &&  // 인증 완료 시 비활성화
                   uiState.phoneNumber.isNotEmpty() && 
                   uiState.isValidPhoneNumber && 
                   remainingTime == 0 &&
                   !uiState.isWaiting,
          isLoading = uiState.isWaiting,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
        )
        
        // 발송 완료 메시지
        if (remainingTime > 0 && !uiState.isPhoneVerified) {
          Spacer(modifier = Modifier.height(12.dp))
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = appColorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "인증번호가 발송되었습니다",
              style = MaterialTheme.typography.bodySmall,
              color = appColorScheme.primary
            )
          }
        }
      }

      // 인증번호 입력 섹션
      if (uiState.isSecurityStepActive) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Column(
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "인증번호 입력",
            style = MaterialTheme.typography.labelLarge,
            color = appColorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
          )
          
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            // 인증번호 입력 필드 (작게)
            CommonTextInput(
              value = uiState.verificationCode,
              labelMainText = "",
              placeholder = "000000",
              validationError = if (!uiState.isPhoneVerified && uiState.validationErrors["verificationCode"] != null) 
                uiState.validationErrors["verificationCode"] else null,
              keyboardOptions = KeyboardConstants.Options.NUMBER,
              keyboardActions = KeyboardConstants.Actions.doneAndHide(),
              modifier = Modifier.weight(0.6f),
              enabled = !uiState.isPhoneVerified,  // 인증 완료 시 비활성화
              onChange = { input ->
                // 6자리까지만 입력 가능
                if (input.length <= 6 && input.all { it.isDigit() }) {
                  onEvent(CompanyJoinSharedEvent.UpdateVerificationCode(input))
                }
              },
            )
            
            // 확인 버튼 (더 예쁘게)
            androidx.compose.material3.Button(
              onClick = {
                onEvent(CompanyJoinSharedEvent.VerifyCode)
              },
              enabled = uiState.verificationCode.length == 6 && !uiState.isPhoneVerified,
              modifier = Modifier
                .weight(0.4f)
                .height(56.dp),
              shape = RoundedCornerShape(8.dp),
              colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = if (uiState.verificationCode.length == 6 && !uiState.isPhoneVerified) 
                  appColorScheme.primary 
                else 
                  MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (uiState.verificationCode.length == 6 && !uiState.isPhoneVerified)
                  Color.White
                else
                  MaterialTheme.colorScheme.onSurfaceVariant
              )
            ) {
              Text(
                text = if (uiState.isPhoneVerified) "인증완료" else "확인",
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
              )
            }
          }
          
          // 타이머 및 상태 메시지
          Spacer(modifier = Modifier.height(12.dp))
          
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // 인증 상태 메시지
            if (uiState.isPhoneVerified) {
              Row(
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp),
                  tint = Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "인증 완료",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                  ),
                  color = Color(0xFF4CAF50)
                )
              }
            } else if (remainingTime > 0) {
              Text(
                text = "남은 시간",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            } else {
              Spacer(modifier = Modifier.width(1.dp))
            }
            
            // 타이머
            if (remainingTime > 0 && !uiState.isPhoneVerified) {
              Text(
                text = "${remainingTime / 60}:${String.format("%02d", remainingTime % 60)}",
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                ),
                color = if (remainingTime < 30) MaterialTheme.colorScheme.error 
                       else appColorScheme.primary
              )
            }
          }
        }
      }
    }
  }
}