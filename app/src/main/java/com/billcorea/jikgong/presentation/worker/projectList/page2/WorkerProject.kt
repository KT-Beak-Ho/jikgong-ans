package com.billcorea.jikgong.presentation.worker.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.worker.projectList.page2.components.WorkingDatesCalendar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun WorkerProject(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit = {},
  onBookmarkClick: () -> Unit = {},
  onApplyClick: () -> Unit = {}
) {
  val config = LocalConfiguration.current
  val screenWidth = config.screenWidthDp.dp
  val scrollState = rememberScrollState()
  var showCheckDialog by remember { mutableStateOf(false) }

  // 작업 가능한 날짜들 설정
  // 📝 사용법:
  // - 1개 날짜: 달력 숨김, 텍스트로만 표시
  // - 2개 이상: 달력 표시하여 사용자가 선택 가능
  val workingDates = remember {
    // 🔄 다양한 케이스 테스트:

    // 케이스 1: 단일 날짜 (달력 숨김)
    // setOf(LocalDate.of(2024, 1, 25))

    // 케이스 2: 연속된 날짜들 (달력 표시) ✅ 현재 활성화
    /* setOf(
      LocalDate.of(2024, 1, 25),
      LocalDate.of(2024, 1, 26),
      LocalDate.of(2024, 1, 27)
    ) */

    // 케이스 3: 불연속 날짜들 (달력 표시)
    // setOf(
    //     LocalDate.of(2024, 1, 25),
    //     LocalDate.of(2024, 1, 27),
    //     LocalDate.of(2024, 2, 1),
    //     LocalDate.of(2024, 2, 3)
    // )

    // 케이스 4: 여러 달에 걸친 날짜들 (달력 표시)
    setOf(
        LocalDate.now().plusDays(5),
        LocalDate.now().plusDays(10),
        LocalDate.now().plusDays(35),
        LocalDate.now().plusDays(40)
    )
  }

  // 사용자가 선택한 작업 날짜들
  var selectedDates by remember {
    mutableStateOf(setOf<LocalDate>())
  }

  if(showCheckDialog) {
    val firstDate = selectedDates.firstOrNull()
    val firstDate2 = workingDates.firstOrNull()
    AlertDialog(
      onDismissRequest = { showCheckDialog = false },
      title = {
        Text(
          text = if(selectedDates.size == 1) "$firstDate 지원" else if(workingDates.size == 1) "$firstDate2 지원" else "$firstDate 외 ${selectedDates.size - 1} 지원",
          style = AppTypography.titleMedium,
          color = Color.Black
        )
      },
      text = {
        Text(
          text = "지원하시겠습니까?",
          style = AppTypography.bodyMedium
        )
      },
      dismissButton = {
        TextButton(
          onClick = {
            showCheckDialog = false
          }
        ) {
          Text(
            text = "취소",
            color = appColorScheme.primary
          )
        }
      },
      confirmButton = {
        TextButton(
          onClick = {
            showCheckDialog = false
            // 버튼 추가 및 클릭 시 이벤트 추가
          }
        ) {
          Text(
            text = "확인",
            color = appColorScheme.primary
          )
        }
      }
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "일자리 정보",
            style = AppTypography.bodyLarge,
            color = appColorScheme.onSurface
          )
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "뒤로가기",
              tint = appColorScheme.onSurface
            )
          }
        },
        actions = {
          IconButton(onClick = onBookmarkClick) {
            Icon(
              imageVector = Icons.Default.BookmarkBorder,
              contentDescription = "북마크",
              tint = appColorScheme.onSurface
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = appColorScheme.surface
        )
      )
    },
    bottomBar = {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
      ) {
        HorizontalDivider(
          thickness = 1.dp,
          color = Color.LightGray
        )
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Column(
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = "100,000원",
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              color = appColorScheme.primary
            )

            val daysText = if (selectedDates.isNotEmpty()) {
              "${selectedDates.size}일 선택됨"
            } else if (workingDates.size == 1) {
              "1일 근무"
            } else {
              "상여금 별도"
            }

            Text(
              text = daysText,
              fontSize = 12.sp,
              color = Color.Gray
            )
          }

          Button(
            onClick =  {
              showCheckDialog = true
            }, // onApplyClick,
            colors = ButtonDefaults.buttonColors(
              containerColor = appColorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = workingDates.size == 1 || selectedDates.isNotEmpty(),
            modifier = Modifier.widthIn(min = 120.dp)
          ) {
            Text(
              text = if (workingDates.size == 1 || selectedDates.isNotEmpty()) {
                "지원하기"
              } else {
                "날짜 선택"
              },
              color = Color.White,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(vertical = 4.dp)
            )
          }
        }
      }
    }
  ) { paddingValues ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(paddingValues)
        .verticalScroll(scrollState)
        .background(Color.White)
    ) {
      // 메인 이미지
      Image(
        painter = painterResource(R.drawable.ic_launcher_background),
        contentDescription = "현장 이미지",
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp),
        contentScale = ContentScale.Crop
      )

      // 프로젝트 정보
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        // 회사명
        Text(
          text = "(주)직공건설",
          fontSize = 12.sp,
          color = Color.Gray,
          modifier = Modifier.padding(bottom = 4.dp)
        )

        // 프로젝트 제목
        Text(
          text = "사하구 낙동5블럭 낙동 온도 측정 센터 신축공사",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = appColorScheme.onSurface,
          lineHeight = 1.4.em,
          modifier = Modifier.padding(bottom = 16.dp)
        )

        // 구분선
        HorizontalDivider(
          thickness = 1.dp,
          color = Color.LightGray,
          modifier = Modifier.padding(bottom = 16.dp)
        )

        // 모집요강
        SectionTitle("모집요강")
        InfoItem("직종", "보통인부")
        InfoItem("모집인원", "10명")
        InfoItem("출근일", "1월 25일~27일")
        InfoItem("출근시간", "06:30~15:00")

        Spacer(modifier = Modifier.height(24.dp))

        // 작업장소
        SectionTitle("작업장소")
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(bottom = 8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "위치",
            tint = appColorScheme.primary,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "부산광역시 사하구 낙동대로 550 (하단동)",
            fontSize = 14.sp,
            color = appColorScheme.onSurface
          )
        }

        // 지도 플레이스홀더
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.LightGray),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "지도",
            color = Color.Gray
          )
        }

        Row(
          modifier = Modifier.padding(top = 8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "신평역(부산지하철) 3.4km",
            fontSize = 12.sp,
            color = Color.Blue
          )
          Spacer(modifier = Modifier.weight(1f))
          Text(
            text = "주소 복사",
            fontSize = 12.sp,
            color = Color.Gray
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 근무환경
        SectionTitle("근무환경")
        InfoItem("위치", "재공")
        InfoItem("회사", "재공")
        InfoItem("주차공간", "무료주차")
        InfoItem("추가근무", "2~3일이후 추가")
        InfoItem("준비물", "신분증, 야쿠죄, 안전화, 장갑등 건설 장비 구비")

        Spacer(modifier = Modifier.height(24.dp))

        // 날짜 선택
        SectionTitle("날짜 선택")

        // 작업 가능한 날짜들에 따라 달력 표시
        // - 1개 날짜: 달력 숨김, 텍스트로만 표시
        // - 2개 이상: 달력 표시하여 선택 가능
        if (workingDates.size == 1) {
          // 날짜가 1개뿐인 경우 텍스트로 표시
          Text(
            text = "작업일: ${workingDates.first()}",
            fontSize = 14.sp,
            color = appColorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp)
          )
        } else {
          // 날짜가 여러 개인 경우 달력으로 선택
          WorkingDatesCalendar(
            workingDates = workingDates,
            selectedDates = selectedDates,
            onDateSelect = { date ->
              selectedDates = if (selectedDates.contains(date)) {
                selectedDates - date
              } else {
                selectedDates + date
              }
            }
          )

          // 선택된 날짜 개수 표시
          if (selectedDates.isNotEmpty()) {
            Text(
              text = "선택된 날짜: ${selectedDates.size}개",
              fontSize = 12.sp,
              color = appColorScheme.primary,
              modifier = Modifier.padding(top = 8.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 담당자 정보
        SectionTitle("담당자 정보")
        InfoItem("담당자", "김형성")
        InfoItem("연락처", "전화하기")

        Spacer(modifier = Modifier.height(100.dp)) // 하단 버튼과 겹치지 않도록
      }
    }
  }
}

@Composable
private fun SectionTitle(title: String) {
  Text(
    text = title,
    fontSize = 16.sp,
    fontWeight = FontWeight.Bold,
    color = appColorScheme.onSurface,
    modifier = Modifier.padding(bottom = 12.dp)
  )
}

@Composable
private fun InfoItem(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      fontSize = 14.sp,
      color = Color.Gray,
      modifier = Modifier.weight(1f)
    )
    Text(
      text = value,
      fontSize = 14.sp,
      color = appColorScheme.onSurface,
      modifier = Modifier.weight(2f),
      textAlign = TextAlign.Start
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ProjectDetailScreenPreview() {
  Jikgong1111Theme {
    WorkerProject()
  }
}
