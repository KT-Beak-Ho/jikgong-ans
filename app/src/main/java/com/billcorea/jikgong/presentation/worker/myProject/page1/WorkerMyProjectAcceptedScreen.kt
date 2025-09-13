package com.billcorea.jikgong.presentation.worker.myProject.page1

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun WorkerMyProjectAcceptedScreen(
  viewModel : MainViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier,
  onBack: () -> Unit = {}
) {
  val selectedTabIndex = remember { mutableIntStateOf(0) }
  val tabs = listOf("확정", "진행중", "마감")
  var selectedDay by remember { mutableIntStateOf(22) } // Default to 22nd

  val events = remember(selectedDay) {
    if (selectedDay == 22) {
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
          description = "퇴근이 확인되었습니다. 급여 지급이 확정되었습니다.",
          isDone = true
        ),
        TimelineEvent(
          title = "급여 지급 예정",
          dateTime = "1월 25일 예정",
          description = "급여가 지급될 예정입니다.",
          isDone = false
        )
      )
    } else if (selectedDay == 23) {
      listOf(
        TimelineEvent(
          title = "신청",
          dateTime = "1월 22일 15:00",
          description = "보통인부 5명 / 해운대구 센텀시티 상업시설 리모델링",
          isDone = true
        ),
        TimelineEvent(
          title = "출역 확정",
          dateTime = "1월 22일 18:30",
          description = "출역이 확정되었습니다. 7시까지 지정된 장소에 도착해주세요.",
          isDone = true
        ),
        TimelineEvent(
          title = "출근 확인",
          dateTime = "1월 23일 07:10",
          description = "출근이 확인되었습니다.",
          isDone = true
        ),
        TimelineEvent(
          title = "점심시간",
          dateTime = "1월 23일 12:00",
          description = "점심시간입니다. 13:00까지 복귀해주세요.",
          isDone = true
        ),
        TimelineEvent(
          title = "퇴근 예정",
          dateTime = "1월 23일 17:00 예정",
          description = "오후 5시 퇴근 예정입니다.",
          isDone = false
        )
      )
    } else {
      emptyList()
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "신청 내역",
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = appColorScheme.onSurface
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
              tint = appColorScheme.onSurface
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White,
          titleContentColor = appColorScheme.onSurface,
          navigationIconContentColor = appColorScheme.onSurface
        )
      )
    }
  ) { inner ->
    Column(
      modifier = Modifier
        .padding(inner)
        .fillMaxSize()
        .background(Color(0xFFF8F9FA))
    ) {
      // Tabs
      TabRow(
        selectedTabIndex = selectedTabIndex.intValue,
        containerColor = Color.White
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTabIndex.intValue == index,
            onClick = { selectedTabIndex.intValue = index },
            text = {
              Text(
                title,
                style = AppTypography.bodyLarge,
                color = if (selectedTabIndex.intValue == index)
                  appColorScheme.primary else appColorScheme.onSurfaceVariant
              )
            }
          )
        }
      }

      // Calendar header (month switcher)
      MonthHeader(date = LocalDate.of(2024, 1, selectedDay))

      // Calendar week row (simplified: 1 week only)
      CalendarWeekRow(
        selectedDay = selectedDay,
        onDaySelected = { day ->
          selectedDay = day
        }
      )

      // Timeline list
      if (events.isNotEmpty()) {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
          items(events) { event ->
            TimelineCard(event = event)
          }
        }
      } else {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(
            text = "선택한 날짜에 일정이 없습니다.",
            style = AppTypography.bodyLarge,
            color = appColorScheme.onSurfaceVariant
          )
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
      .padding(horizontal = 20.dp, vertical = 16.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "${date.year}년 ${date.month.value}월",
      style = AppTypography.titleMedium,
      fontWeight = FontWeight.SemiBold,
      color = appColorScheme.onSurface
    )
  }
}


@Composable
private fun CalendarWeekRow(
  selectedDay: Int,
  onDaySelected: (Int) -> Unit
) {
  val days = (21..27).map { it }
  val activeDays = setOf(22, 23) // Only 22nd and 23rd have data

  LazyRow(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceEvenly,
    contentPadding = PaddingValues(horizontal = 24.dp)
  ) {
    items(days) { day ->
      val isActive = activeDays.contains(day)
      val isSelected = day == selectedDay
      
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = day.getDayOfWeekLabel(),
          style = AppTypography.bodySmall,
          color = if (isActive) appColorScheme.onSurfaceVariant else appColorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
          modifier = Modifier
            .size(36.dp)
            .background(
              color = when {
                isSelected && isActive -> appColorScheme.primary
                isActive -> appColorScheme.primaryContainer
                else -> Color.Transparent
              },
              shape = RoundedCornerShape(8.dp)
            )
            .then(
              if (isActive) {
                Modifier.clickable { onDaySelected(day) }
              } else {
                Modifier
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = day.toString(),
            style = AppTypography.bodyMedium,
            color = when {
              isSelected && isActive -> Color.White
              isActive -> appColorScheme.primary
              else -> appColorScheme.onSurface.copy(alpha = 0.4f)
            },
            fontWeight = if (isSelected && isActive) FontWeight.Bold else FontWeight.Normal
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

    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(12.dp),
      colors = CardDefaults.cardColors(
        containerColor = Color.White
      ),
      elevation = CardDefaults.cardElevation(
        defaultElevation = 2.dp
      )
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = event.title,
            style = AppTypography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = appColorScheme.onSurface
          )
          Text(
            text = event.dateTime,
            style = AppTypography.bodySmall,
            color = appColorScheme.onSurfaceVariant
          )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = event.description,
          style = AppTypography.bodyMedium,
          color = appColorScheme.onSurfaceVariant
        )
      }
    }
  }
  Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun Bullet(isDone: Boolean, size: Dp, color: Color) {
  Canvas(modifier = Modifier.size(size)) {
    drawCircle(
      color = if (isDone) appColorScheme.primary else Color.White,
      radius = size.toPx() / 2,
      style = if (isDone) androidx.compose.ui.graphics.drawscope.Fill else androidx.compose.ui.graphics.drawscope.Stroke(
        width = 3.dp.toPx()
      ),
    )
    if (isDone) {
      drawCircle(
        color = Color.White,
        radius = size.toPx() / 4,
        style = androidx.compose.ui.graphics.drawscope.Fill
      )
    }
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
