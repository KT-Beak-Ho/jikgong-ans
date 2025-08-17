package com.billcorea.jikgong.presentation.worker.income.page1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.destinations.GraphScreenDestination
import com.billcorea.jikgong.presentation.worker.common.WorkerBottomNav
import com.billcorea.jikgong.presentation.worker.projectList.page2.components.WorkingDatesCalendar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import java.time.LocalDate
import java.time.YearMonth

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeManagementScreen(
  modifier: Modifier = Modifier,
  navigator: DestinationsNavigator
) {
  val config = LocalConfiguration.current
  val screenHeight = config.screenHeightDp

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .padding(top = 20.dp),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "수입 관리",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
          )
        },
        actions = {
          IconButton(onClick = { /* 알림 클릭 */ }) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "알림",
              tint = Color.Black
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    },
    bottomBar = {
      WorkerBottomNav(
        modifier = Modifier
          .fillMaxWidth()
          .height((screenHeight * .10).dp)
          .padding(5.dp),
        doWorkerProjectList = { /* 프로젝트 리스트 */ },
        doWorkerMyjob = { /* 내 일자리 */ },
        doWorkerEarning = { /* 수입 관리 */ },
        doWorkerProfile = { /* 프로필 */ }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 20.dp)
    ) {
      Spacer(modifier = Modifier.height(24.dp))

      // 수입 정보 카드
      IncomeInfoCard()

      Spacer(modifier = Modifier.height(32.dp))

      // 그래프 및 학인하기 버튼
      EarningsGraphSection(navigator)

      Spacer(modifier = Modifier.height(32.dp))

      // 수입 내역 확장 섹션
      ExpandableIncomeSection()

      Spacer(modifier = Modifier.weight(1f))
    }
  }
}

@Composable
fun IncomeInfoCard() {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .height(100.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp
    )
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 총 수입 정보
        Column {
          Text(
            text = "총 수입",
            color = Color.Gray,
            fontSize = 14.sp,
            style = AppTypography.bodyMedium
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "3,293,800원",
            color = appColorScheme.primary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // 총 근무시간 정보
        Column(
          horizontalAlignment = Alignment.End
        ) {
          Text(
            text = "총 근무시간",
            color = Color.Gray,
            fontSize = 14.sp,
            style = AppTypography.bodyMedium
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "180시간 30분",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  }
}

@Composable
fun EarningsGraphSection(
  navigator: DestinationsNavigator
) {
  Column {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "그래프로 한눈에 보는\n수입 관리",
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 1.3.em
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 확인하기 버튼
    Button(
      onClick = { navigator.navigate(GraphScreenDestination) },
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
      shape = RoundedCornerShape(8.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = appColorScheme.primary
      )
    ) {
      Text(
        text = "확인하기",
        color = Color.White,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
fun SimpleBarChart() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(120.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalAlignment = Alignment.Bottom
  ) {
    // 막대 그래프 데이터 (임시)
    val chartData = listOf(60, 40, 80, 100, 30, 70, 50)

    chartData.forEach { value ->
      Box(
        modifier = Modifier
          .weight(1f)
          .height((value * 1.2).dp)
          .background(
            appColorScheme.primary,
            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
          )
      )
    }
  }
}

@Composable
fun ExpandableIncomeSection() {
  var isExpanded by remember { mutableStateOf(false) }

  Column {
    // 헤더
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFF5F5F5))
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "수입 내역",
        color = Color.Black,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium
      )

      IconButton(
        onClick = { isExpanded = !isExpanded }
      ) {
        Icon(
          imageVector = Icons.Default.KeyboardArrowDown,
          contentDescription = if (isExpanded) "접기" else "펼치기",
          tint = Color.Gray
        )
      }
    }

    // 확장 가능한 내용 (실제로는 애니메이션 추가)
    if (isExpanded) {

      IncomeDetailBottomSheet(
        onDismiss = { isExpanded = false }
      )

      // isExpanded = false
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeDetailBottomSheet(
  onDismiss: () -> Unit
) {
  val bottomSheetState = rememberModalBottomSheetState(
    skipPartiallyExpanded = true
  )

  // selectedDates 상태를 BottomSheet에서 관리하여 하위 컴포넌트들이 공유할 수 있도록 함
  var selectedDates by remember { mutableStateOf(setOf<LocalDate>()) }

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = bottomSheetState,
    modifier = Modifier.fillMaxHeight(0.9f),
    containerColor = Color.White,
    contentColor = Color.Black
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
    ) {
      // 헤더
      BottomSheetHeader(onClose = onDismiss)

      Spacer(modifier = Modifier.height(24.dp))

      // 수입 요약 카드
      IncomeSummaryCard()

      Spacer(modifier = Modifier.height(24.dp))

      // 달력 (selectedDates 전달)
      IncomeCalendarSection(
        selectedDates = selectedDates,
        onSelectedDatesChange = { newSelectedDates ->
          selectedDates = newSelectedDates
        }
      )

      Spacer(modifier = Modifier.height(24.dp))

      // 수입 내역 리스트 (selectedDates 전달)
      IncomeHistoryList(selectedDates = selectedDates)
    }
  }
}

@Composable
fun BottomSheetHeader(onClose: () -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "수입 내역",
      fontSize = 18.sp,
      fontWeight = FontWeight.Medium,
      color = Color.Black
    )

    IconButton(onClick = onClose) {
      Icon(
        imageVector = Icons.Default.Close,
        contentDescription = "닫기",
        tint = Color.Gray
      )
    }
  }
}

@Composable
fun IncomeSummaryCard() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFF8F9FA)
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 0.dp
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "1월 수입",
          fontSize = 14.sp,
          color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "1,000,500원",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = appColorScheme.primary
        )
      }

      Column(
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = "1월 근무시간",
          fontSize = 14.sp,
          color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "130시간 30분",
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium,
          color = Color.Black
        )
      }
    }
  }
}

@Composable
fun IncomeCalendarSection(
  selectedDates: Set<LocalDate>,
  onSelectedDatesChange: (Set<LocalDate>) -> Unit
) {
  var currentMonth by remember { mutableStateOf(YearMonth.now()) }

  // 실제 근무 일정이 있는 날짜들 (예시 데이터)
  val workingDates = remember {
    setOf(
      LocalDate.of(2025, 1, 16),
      LocalDate.of(2025, 1, 17),
      LocalDate.of(2025, 1, 18)
    )
  }

  Column {
    Spacer(modifier = Modifier.height(16.dp))

    // WorkingDatesCalendar 컴포넌트 사용
    WorkingDatesCalendar(
      workingDates = workingDates,
      selectedDates = selectedDates,
      onDateSelect = { date ->
        val newSelectedDates = if (selectedDates.contains(date)) {
          selectedDates - date
        } else {
          selectedDates + date
        }
        onSelectedDatesChange(newSelectedDates)
      },
      modifier = Modifier.fillMaxWidth()
    )
  }
}

@Composable
fun IncomeHistoryList(selectedDates: Set<LocalDate>) {
  // 수입 내역 데이터 (예시)
  val allIncomeHistory = remember {
    listOf(
      IncomeHistoryItem(
        date = LocalDate.of(2025, 1, 18),
        dayOfWeek = "목",
        time = "06:30-15:00 근무",
        location = "시흥구 낙동5블럭 낙동강 온도 측정 센...",
        workType = "보통인부",
        amount = 123400,
        status = ""
      ),
      IncomeHistoryItem(
        date = LocalDate.of(2025, 1, 17),
        dayOfWeek = "수",
        time = "06:30-15:00 근무",
        location = "시흥구 낙동5블럭 낙동강 온도 측정 센...",
        workType = "보통인부",
        amount = 123400,
        status = ""
      ),
      IncomeHistoryItem(
        date = LocalDate.of(2025, 1, 16),
        dayOfWeek = "화",
        time = "06:30-15:00 근무",
        location = "시흥구 낙동5블럭 낙동강 온도 측정 센...",
        workType = "보통인부",
        amount = 123400,
        status = "직접입력"
      )
    )
  }

  // selectedDates가 비어있으면 모든 데이터를 보여주고, 선택된 날짜가 있으면 해당 날짜만 필터링
  val filteredIncomeHistory = remember(allIncomeHistory, selectedDates) {
    if (selectedDates.isEmpty()) {
      allIncomeHistory
    } else {
      allIncomeHistory.filter { item ->
        selectedDates.contains(item.date)
      }
    }
  }

  Column {
    // 선택된 날짜 정보 표시 (선택사항)
    if (selectedDates.isNotEmpty()) {
      Text(
        text = "선택된 날짜: ${selectedDates.size}개",
        fontSize = 14.sp,
        color = appColorScheme.primary,
        modifier = Modifier.padding(bottom = 16.dp)
      )
    }

    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      itemsIndexed(filteredIncomeHistory) { index, item ->
        IncomeHistoryCard(item = item)
      }

      // 하단 여백
      item {
        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}

@Composable
fun IncomeHistoryCard(item: IncomeHistoryItem) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFF8F9FA)
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 0.dp
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 날짜 섹션
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(40.dp)
      ) {
        Text(
          text = "(${item.dayOfWeek})",
          fontSize = 12.sp,
          color = Color.Gray
        )
        Text(
          text = item.date.dayOfMonth.toString(),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        )
      }

      Spacer(modifier = Modifier.width(16.dp))

      // 내용 섹션
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = item.time,
          fontSize = 12.sp,
          color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = item.location,
          fontSize = 14.sp,
          color = Color.Black,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = item.workType,
            fontSize = 12.sp,
            color = appColorScheme.primary,
            modifier = Modifier
              .background(
                appColorScheme.primary.copy(alpha = 0.1f),
                RoundedCornerShape(4.dp)
              )
              .padding(horizontal = 8.dp, vertical = 2.dp)
          )

          if (item.status.isNotEmpty()) {
            Text(
              text = item.status,
              fontSize = 12.sp,
              color = Color.Gray,
              modifier = Modifier
                .background(
                  Color.Gray.copy(alpha = 0.1f),
                  RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }
        }
      }

      // 금액 섹션
      Column(
        horizontalAlignment = Alignment.End
      ) {
        if (item.status == "직접입력") {
          Icon(
            imageVector = Icons.Default.MoreHoriz,
            contentDescription = "더보기",
            tint = Color.Gray,
            modifier = Modifier.size(16.dp)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "${String.format("%,d", item.amount)}원",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          color = appColorScheme.primary
        )
      }
    }
  }
}

// 데이터 클래스
data class IncomeHistoryItem(
  val date: LocalDate,
  val dayOfWeek: String,
  val time: String,
  val location: String,
  val workType: String,
  val amount: Int,
  val status: String
)

@Preview(showBackground = true)
@Composable
fun IncomeManagementScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()
  Jikgong1111Theme {
    IncomeManagementScreen(navigator = navigator)
  }
}