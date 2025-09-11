package com.billcorea.jikgong.presentation.company.auth.join.page3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedUiState
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.JikgongAppDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyJoinPage3Screen(
  companyJoinViewModel: CompanyJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by companyJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateHome by companyJoinViewModel.shouldNavigateHome.collectAsStateWithLifecycle()
  val shouldNavigateBack by companyJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    //  로컬 변수 초기화
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ResetJoin3Flow)
    //  에러 변수 초기화
    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(CompanyJoinPage2ScreenDestination)
      companyJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 초기 화면(로그인/회원가입 선택)으로 돌아가기
  LaunchedEffect(shouldNavigateHome) {
    if (shouldNavigateHome) {
      // 모든 회원가입 관련 스택을 제거하고 초기 화면으로 이동
      navigator.popBackStack(JikgongAppDestination, inclusive = false)
      companyJoinViewModel.clearNavigationEvents()
    }
  }

  CompanyJoinPage3ScreenContent(
    uiState = uiState,
    onEvent = companyJoinViewModel::onEvent,
    modifier = modifier
  )
}

@Composable
fun CompanyJoinPage3ScreenContent(
  uiState: CompanyJoinSharedUiState,
  onEvent: (CompanyJoinSharedEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .padding(top = 20.dp),
    //  상단바 - 완료 페이지에서는 뒤로가기 버튼 제거
    topBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
      )
    },
    //  하단바
    bottomBar = {
      //  확인 버튼 - 초기 화면(로그인/회원가입 선택)으로 이동
      CommonButton(
        text = "확인",
        onClick = {
          onEvent(CompanyJoinSharedEvent.HomePage)
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(WindowInsets.navigationBars.asPaddingValues())
          .padding(horizontal = 16.dp, vertical = 16.dp)
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // 성공 아이콘
      Surface(
        modifier = Modifier.size(120.dp),
        shape = CircleShape,
        color = appColorScheme.primary.copy(alpha = 0.1f)
      ) {
        Box(
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            modifier = Modifier.size(80.dp),
            tint = appColorScheme.primary
          )
        }
      }
      
      Spacer(modifier = Modifier.height(32.dp))
      
      // 축하 메시지
      Text(
        text = buildAnnotatedString {
          append("🎉 ")
          withStyle(
            style = SpanStyle(
              color = appColorScheme.primary,
              fontWeight = FontWeight.Bold,
              fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
          ) {
            append("회원가입 완료!")
          }
        },
        textAlign = TextAlign.Center
      )
      
      Spacer(modifier = Modifier.height(16.dp))
      
      // 환영 메시지
      Text(
        text = "지공의 기업 회원이 되신 것을\n진심으로 환영합니다!",
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = colorResource(R.color.black),
        lineHeight = 1.5.em
      )
      
      Spacer(modifier = Modifier.height(32.dp))
      
      // 안내 카드
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
          defaultElevation = 4.dp
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Text(
            text = "다음 단계 안내",
            style = MaterialTheme.typography.titleMedium,
            color = appColorScheme.primary
          )
          
          Spacer(modifier = Modifier.height(16.dp))
          
          // 승인 대기 안내
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
          ) {
            Text(
              text = "1.",
              style = MaterialTheme.typography.bodyMedium,
              color = appColorScheme.primary,
              modifier = Modifier.padding(end = 8.dp)
            )
            Text(
              text = buildAnnotatedString {
                append("담당자가 제출하신 정보를 검토 후\n빠른 시일 내에 ")
                withStyle(
                  style = SpanStyle(
                    color = appColorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    background = appColorScheme.primary.copy(alpha = 0.1f)
                  )
                ) {
                  append("전화로 연락드립니다")
                }
                append(".")
              },
              style = MaterialTheme.typography.bodyMedium,
              lineHeight = 1.4.em
            )
          }
          
          Spacer(modifier = Modifier.height(12.dp))
          
          // 승인 완료 안내
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
          ) {
            Text(
              text = "2.",
              style = MaterialTheme.typography.bodyMedium,
              color = appColorScheme.primary,
              modifier = Modifier.padding(end = 8.dp)
            )
            Text(
              text = buildAnnotatedString {
                append("승인이 완료되면 등록하신 ")
                withStyle(
                  style = SpanStyle(
                    color = appColorScheme.primary,
                    fontWeight = FontWeight.Bold
                  )
                ) {
                  append("이메일")
                }
                append("로\n")
                withStyle(
                  style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    background = Color(0xFFFFF3E0)
                  )
                ) {
                  append("알림을 보내드립니다")
                }
                append(".")
              },
              style = MaterialTheme.typography.bodyMedium,
              lineHeight = 1.4.em
            )
          }
          
          Spacer(modifier = Modifier.height(12.dp))
          
          // 로그인 안내
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
          ) {
            Text(
              text = "3.",
              style = MaterialTheme.typography.bodyMedium,
              color = appColorScheme.primary,
              modifier = Modifier.padding(end = 8.dp)
            )
            Text(
              text = "승인 완료 후 로그인하여\n서비스를 이용하실 수 있습니다.",
              style = MaterialTheme.typography.bodyMedium,
              lineHeight = 1.4.em
            )
          }
        }
      }
      
      Spacer(modifier = Modifier.height(24.dp))
      
      // 문의 안내
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = "문의사항이 있으신가요?",
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
              )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone",
                modifier = Modifier.size(16.dp),
                tint = appColorScheme.primary
              )
              Text(
                text = " 1588-0000",
                style = MaterialTheme.typography.bodySmall
              )
            }
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email",
                modifier = Modifier.size(16.dp),
                tint = appColorScheme.primary
              )
              Text(
                text = " support@jikgong.com",
                style = MaterialTheme.typography.bodySmall
              )
            }
          }
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "회원가입 완료 화면")
@Composable
fun JoinPage3ScreenPreview() {
  Jikgong1111Theme {
    CompanyJoinPage3ScreenContent(
      uiState = CompanyJoinSharedUiState(
        currentPage = 3,
        isRegistrationComplete = true,
        name = "홍길동",
        companyName = "테스트 회사"
      ),
      onEvent = {}
    )
  }
}