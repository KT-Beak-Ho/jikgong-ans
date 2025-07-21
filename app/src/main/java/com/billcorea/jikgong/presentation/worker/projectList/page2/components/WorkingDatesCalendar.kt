package com.billcorea.jikgong.presentation.worker.projectList.page2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * 작업 가능한 날짜들을 기반으로 한 달력 컴포넌트
 *
 * @param workingDates 작업 가능한 날짜들
 * - 1개만 있으면 달력을 표시하지 않음
 * - 2개 이상이면 달력을 표시하고 해당 날짜들만 선택 가능
 *
 * @param selectedDates 현재 선택된 날짜들
 * @param onDateSelect 날짜 선택/해제 콜백
 */
@Composable
fun WorkingDatesCalendar(
  workingDates: Set<LocalDate>,
  selectedDates: Set<LocalDate> = emptySet(),
  onDateSelect: (LocalDate) -> Unit = {},
  modifier: Modifier = Modifier
) {
  // 작업 가능한 날짜가 1개 이하면 달력을 표시하지 않음
  if (workingDates.size <= 1) {
    return
  }

  // 작업 가능한 날짜들 중 가장 이른 날짜가 있는 월을 초기 표시 월로 설정
  val initialMonth = remember(workingDates) {
    if (workingDates.isNotEmpty()) {
      YearMonth.from(workingDates.minOrNull()!!)
    } else {
      YearMonth.now()
    }
  }

  var currentDisplayMonth by remember(workingDates) {
    mutableStateOf(initialMonth)
  }

  Column(modifier = modifier) {
    // 월/년 헤더와 네비게이션
    CalendarHeader(
      currentMonth = currentDisplayMonth,
      onPreviousMonth = {
        currentDisplayMonth = currentDisplayMonth.minusMonths(1)
      },
      onNextMonth = {
        currentDisplayMonth = currentDisplayMonth.plusMonths(1)
      },
      workingDates = workingDates
    )

    Spacer(modifier = Modifier.height(16.dp))

    // 요일 헤더
    WeekDaysHeader()

    Spacer(modifier = Modifier.height(8.dp))

    // 달력 그리드
    CalendarGrid(
      displayMonth = currentDisplayMonth,
      workingDates = workingDates,
      selectedDates = selectedDates,
      onDateSelect = onDateSelect
    )
  }
}

@Composable
private fun CalendarHeader(
  currentMonth: YearMonth,
  onPreviousMonth: () -> Unit,
  onNextMonth: () -> Unit,
  workingDates: Set<LocalDate>
) {
  // 작업 가능한 날짜들이 있는 월들을 계산
  val workingMonths = workingDates.map { YearMonth.from(it) }.toSet()

  // 이전/다음 달 네비게이션 가능 여부 확인
  val canNavigatePrevious = workingMonths.contains(currentMonth.minusMonths(1))
  val canNavigateNext = workingMonths.contains(currentMonth.plusMonths(1))

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(
      onClick = onPreviousMonth,
      enabled = canNavigatePrevious
    ) {
      Icon(
        imageVector = Icons.Default.ChevronLeft,
        contentDescription = "이전 달",
        tint = if (canNavigatePrevious) appColorScheme.onSurface else Color.Gray
      )
    }

    Text(
      text = currentMonth.format(DateTimeFormatter.ofPattern("yyyy년 M월")),
      fontSize = 16.sp,
      fontWeight = FontWeight.Bold,
      color = appColorScheme.onSurface
    )

    IconButton(
      onClick = onNextMonth,
      enabled = canNavigateNext
    ) {
      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "다음 달",
        tint = if (canNavigateNext) appColorScheme.onSurface else Color.Gray
      )
    }
  }
}

@Composable
private fun WeekDaysHeader() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceEvenly
  ) {
    listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
      Text(
        text = day,
        fontSize = 12.sp,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        modifier = Modifier.width(40.dp)
      )
    }
  }
}

@Composable
private fun CalendarGrid(
  displayMonth: YearMonth,
  workingDates: Set<LocalDate>,
  selectedDates: Set<LocalDate>,
  onDateSelect: (LocalDate) -> Unit
) {
  val calendarDays = generateCalendarDays(displayMonth, workingDates)
  val weeks = calendarDays.chunked(7)

  weeks.forEach { week ->
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      week.forEach { day ->
        CalendarDateCell(
          day = day,
          isSelected = selectedDates.contains(day.date),
          onClick = {
            if (day.isWorkingDate && day.isCurrentMonth) {
              onDateSelect(day.date)
            }
          }
        )
      }
    }
  }
}

@Composable
private fun CalendarDateCell(
  day: CalendarDay,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  val backgroundColor = when {
    isSelected -> appColorScheme.primary
    day.isToday -> appColorScheme.primary.copy(alpha = 0.2f)
    else -> Color.Transparent
  }

  val textColor = when {
    isSelected -> Color.White
    !day.isCurrentMonth -> Color.Gray.copy(alpha = 0.3f)
    !day.isWorkingDate -> Color.Gray.copy(alpha = 0.5f)
    day.isToday -> appColorScheme.primary
    else -> appColorScheme.onSurface
  }

  Box(
    modifier = Modifier
      .width(40.dp)
      .height(40.dp)
      .padding(2.dp)
      .clip(RoundedCornerShape(4.dp))
      .background(backgroundColor)
      .clickable(
        enabled = day.isWorkingDate && day.isCurrentMonth
      ) { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = day.dayOfMonth.toString(),
      fontSize = 14.sp,
      color = textColor,
      textAlign = TextAlign.Center,
      fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal
    )
  }
}

private data class CalendarDay(
  val date: LocalDate,
  val dayOfMonth: Int,
  val isCurrentMonth: Boolean,
  val isToday: Boolean,
  val isWorkingDate: Boolean
)

private fun generateCalendarDays(
  displayMonth: YearMonth,
  workingDates: Set<LocalDate>
): List<CalendarDay> {
  val firstDayOfMonth = displayMonth.atDay(1)
  val lastDayOfMonth = displayMonth.atEndOfMonth()
  val today = LocalDate.now()

  // 첫 번째 주의 시작 (일요일)
  val startDate = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek.value % 7L)

  // 마지막 주의 끝 (토요일)
  val endDate = lastDayOfMonth.plusDays(6 - lastDayOfMonth.dayOfWeek.value % 7L)

  val days = mutableListOf<CalendarDay>()
  var currentDate = startDate

  while (!currentDate.isAfter(endDate)) {
    val isCurrentMonth = currentDate.month == displayMonth.month &&
      currentDate.year == displayMonth.year
    val isToday = currentDate == today
    val isWorkingDate = workingDates.contains(currentDate)

    days.add(
      CalendarDay(
        date = currentDate,
        dayOfMonth = currentDate.dayOfMonth,
        isCurrentMonth = isCurrentMonth,
        isToday = isToday,
        isWorkingDate = isWorkingDate
      )
    )

    currentDate = currentDate.plusDays(1)
  }

  return days
}

// 헬퍼 함수들 - 외부에서 사용 가능
object CalendarUtils {
  /**
   * 날짜 문자열을 LocalDate로 변환
   * @param dateString "yyyy-MM-dd" 형식의 문자열
   */
  fun stringToLocalDate(dateString: String): LocalDate? {
    return try {
      LocalDate.parse(dateString)
    } catch (e: Exception) {
      null
    }
  }

  /**
   * 날짜 문자열 리스트를 LocalDate Set으로 변환
   */
  fun stringListToLocalDateSet(dateStrings: List<String>): Set<LocalDate> {
    return dateStrings.mapNotNull { stringToLocalDate(it) }.toSet()
  }

  /**
   * 연속된 날짜 범위 생성
   * @param startDate 시작 날짜
   * @param endDate 종료 날짜
   */
  fun createDateRange(startDate: LocalDate, endDate: LocalDate): Set<LocalDate> {
    val dates = mutableSetOf<LocalDate>()
    var current = startDate
    while (!current.isAfter(endDate)) {
      dates.add(current)
      current = current.plusDays(1)
    }
    return dates
  }
}

@Preview(showBackground = true)
@Composable
private fun WorkingDatesCalendarPreview() {
  Jikgong1111Theme {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      // 케이스 1: 날짜 1개 (달력 숨김)
      Text("케이스 1: 날짜 1개 (달력 안보임)")
      val singleDate = setOf(LocalDate.now().plusDays(1))
      WorkingDatesCalendar(
        workingDates = singleDate
      )

      // 케이스 2: 날짜 다수 (달력 표시)
      Text("케이스 2: 날짜 다수 (달력 표시)")
      var selectedDates by remember {
        mutableStateOf(setOf<LocalDate>())
      }

      val workingDates = remember {
        setOf(
          LocalDate.now().plusDays(1),
          LocalDate.now().plusDays(2),
          LocalDate.now().plusDays(3),
          LocalDate.now().plusDays(7),
          LocalDate.now().plusDays(8)
        )
      }

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
    }
  }
}