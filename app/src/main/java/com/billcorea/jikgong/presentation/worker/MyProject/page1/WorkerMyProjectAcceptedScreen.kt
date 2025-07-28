package com.billcorea.jikgong.presentation.worker.MyProject.page1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerMyProjectAcceptedScreen(
  viewModel : MainViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier,
  onBack: () -> Unit = {}
) {
  val selectedTabIndex = remember { mutableIntStateOf(0) }
  val tabs = listOf("확정", "진행중", "마감")
  val today = LocalDate.of(2024, 1, 22)

  val events = remember {
    listOf(
      TimelineEvent(
        title = "신청",
        dateTime = "1월 21일 09:00",
        description = "보통인부 10명 / 사하구 낙동5블락 낙동강 온도 측정 센터 신축공사",
        isDone = true
      ),
      TimelineEvent(
        title = "출역 확정",
        dateTime = "1월 21일 14:00",
        description = "출역이 확정되었습니다. 6시 40분까지 지정된 장소에 도착해주세요.",
        isDone = true
      ),
      TimelineEvent(
        title = "출근 확인",
        dateTime = "1월 22일 06:50",
        description = "출근이 확인되었습니다.",
        isDone = true
      ),
      TimelineEvent(
        title = "퇴근 확인",
        dateTime = "1월 22일 13:50",
        description = "퇴근이 확인되었습니다. 출금이 확정되었습니다.",
        isDone = true
      ),
      TimelineEvent(
        title = "입금 확인",
        dateTime = "1월 22일 13:50",
        description = "□□",
        isDone = false
      )
    )
  }

  Scaffold(
    modifier = modifier,
    topBar = {
      TopAppBar(
        title = { Text(text = "신청 내역 (확정)", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
          }
        }
      )
    }
  ) { inner ->
    Column(modifier = Modifier.padding(inner).fillMaxSize()) {
      // Tabs
      TabRow(selectedTabIndex = selectedTabIndex.intValue) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTabIndex.intValue == index,
            onClick = { selectedTabIndex.intValue = index },
            text = { Text(title) }
          )
        }
      }

      // Calendar header (month switcher)
      MonthHeader(date = today)

      // Calendar week row (simplified: 1 week only)
      CalendarWeekRow(selectedDay = today.dayOfMonth)

      // Timeline list
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
      ) {
        items(events) { event ->
          TimelineCard(event = event)
        }
      }
    }
  }
}

@Composable
private fun MonthHeader(date: LocalDate) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "${date.year}년 ${date.month.value}월",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    )
  }
}

@Composable
private fun CalendarWeekRow(selectedDay: Int) {
  val days = (21..27).map { it }
  LazyRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 4.dp)
  ) {
    items(days) { day ->
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day.getDayOfWeekLabel(), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Box(
          modifier = Modifier
            .size(34.dp)
            .background(
              color = if (day == selectedDay) MaterialTheme.colorScheme.primary else Color.Transparent,
              shape = MaterialTheme.shapes.medium
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = day.toString(),
            color = if (day == selectedDay) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (day == selectedDay) FontWeight.Bold else FontWeight.Normal
          )
        }
      }
    }
  }
}

private fun Int.getDayOfWeekLabel(): String {
  val date = LocalDate.of(2024, 1, this)
  return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN).substring(0, 1)
}

@Composable
private fun TimelineCard(event: TimelineEvent, lineColor: Color = MaterialTheme.colorScheme.primary) {
  Row(modifier = Modifier.fillMaxWidth()) {
    // Line + bullet
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Bullet(isDone = event.isDone, size = 20.dp, color = lineColor)
      Spacer(modifier = Modifier.height(4.dp))
      if (!event.isLast) {
        Canvas(modifier = Modifier
          .width(2.dp)
          .height(60.dp)) {
          drawLine(
            color = lineColor.copy(alpha = 0.5f),
            start = Offset(x = size.width / 2, y = 0f),
            end = Offset(x = size.width / 2, y = size.height),
            strokeWidth = size.width,
            cap = StrokeCap.Round
          )
        }
      }
    }

    Spacer(modifier = Modifier.width(12.dp))

    Card(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.padding(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(text = event.title, fontWeight = FontWeight.SemiBold)
          Text(text = event.dateTime, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = event.description, fontSize = 13.sp)
      }
    }
  }
  Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun Bullet(isDone: Boolean, size: Dp, color: Color) {
  Canvas(modifier = Modifier.size(size)) {
    drawCircle(
      color = if (isDone) color else Color.White,
      radius = size.toPx() / 2,
      style = if (isDone) androidx.compose.ui.graphics.drawscope.Fill else androidx.compose.ui.graphics.drawscope.Stroke(width = 4f),
    )
  }
}

// Data model
private data class TimelineEvent(
  val title: String,
  val dateTime: String,
  val description: String,
  val isDone: Boolean,
  val isLast: Boolean = false
)

@Preview(showBackground = true)
@Composable
private fun WorkerMyProjectAcceptedScreenPreview() {
  val fakeViewModel = MainViewModel()
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    WorkerMyProjectAcceptedScreen(fakeViewModel, navigator, modifier = Modifier.padding(3.dp))
  }
}
