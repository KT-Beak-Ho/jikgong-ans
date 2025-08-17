package com.billcorea.jikgong.presentation.worker.income.page2

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(
  onBackClick: () -> Unit = {}
) {
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("일별", "월별")

  // 샘플 데이터
  val workingHoursData = listOf(8f, 9f, 0f, 8f, 7f, 8f, 8f)
  val earningsData = listOf(180000f, 198000f, 0f, 180000f, 154000f, 170000f, 175000f)
  val days = listOf("2", "3", "4", "5", "6", "7", "8")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            "그래프",
            style = AppTypography.titleMedium,
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
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color.White)
    ) {
      // 날짜 선택 헤더
      DateSelectionHeader()

      // 탭 선택
      TabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier.padding(horizontal = 16.dp),
        containerColor = Color.Transparent,
        divider = {},
        indicator = { tabPositions ->
          Box(
            modifier = Modifier
              .tabIndicatorOffset(tabPositions[selectedTab])
              .height(2.dp)
              .background(
                appColorScheme.primary,
                RoundedCornerShape(1.dp)
              )
          )
        }
      ) {
        tabs.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
          ) {
            Text(
              text = title,
              modifier = Modifier.padding(vertical = 12.dp),
              color = if (selectedTab == index) appColorScheme.primary else Color.Gray,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 근무시간 차트
      ChartSection(
        title = "근무시간",
        data = workingHoursData,
        days = days,
        maxValue = 10f,
        unit = ""
      )

      Spacer(modifier = Modifier.height(32.dp))

      // 수익금 차트
      ChartSection(
        title = "수익금",
        subtitle = "(만원)",
        data = earningsData.map { it / 10000f }, // 만원 단위로 변환
        days = days,
        maxValue = 20f,
        unit = ""
      )
    }
  }
}

@Composable
private fun DateSelectionHeader() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(onClick = { /* 이전 달 */ }) {
      Icon(
        imageVector = Icons.Default.KeyboardArrowLeft,
        contentDescription = "이전 달",
        tint = appColorScheme.onSurface
      )
    }

    Text(
      text = "2024년 1월",
      style = AppTypography.titleMedium,
      color = appColorScheme.onSurface,
      modifier = Modifier.padding(horizontal = 16.dp)
    )

    IconButton(onClick = { /* 다음 달 */ }) {
      Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = "다음 달",
        tint = appColorScheme.onSurface
      )
    }
  }
}

@Composable
private fun ChartSection(
  title: String,
  subtitle: String = "",
  data: List<Float>,
  days: List<String>,
  maxValue: Float,
  unit: String
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
  ) {
    // 제목
    Row(
      verticalAlignment = Alignment.Bottom
    ) {
      Text(
        text = title,
        style = AppTypography.titleMedium,
        color = appColorScheme.onSurface,
        fontWeight = FontWeight.Bold
      )
      if (subtitle.isNotEmpty()) {
        Text(
          text = subtitle,
          style = AppTypography.bodySmall,
          color = Color.Gray,
          modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // 차트 영역
    BarChart(
      data = data,
      days = days,
      maxValue = maxValue,
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
    )
  }
}

@Composable
private fun BarChart(
  data: List<Float>,
  days: List<String>,
  maxValue: Float,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    // Y축 라벨과 차트
    Row(
      modifier = Modifier.weight(1f)
    ) {
      // Y축 라벨
      Column(
        modifier = Modifier.width(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
      ) {
        Text(
          text = maxValue.toInt().toString(),
          fontSize = 10.sp,
          color = Color.Gray
        )
        Text(
          text = (maxValue / 2).toInt().toString(),
          fontSize = 10.sp,
          color = Color.Gray
        )
        Text(
          text = "0",
          fontSize = 10.sp,
          color = Color.Gray
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // 바 차트
      Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
      ) {
        data.forEachIndexed { index, value ->
          val barHeight = if (value > 0) (value / maxValue).coerceIn(0.05f, 1f) else 0f

          Box(
            modifier = Modifier
              .width(32.dp)
              .fillMaxHeight()
          ) {
            Box(
              modifier = Modifier
                .width(24.dp)
                .fillMaxHeight(barHeight)
                .align(Alignment.BottomCenter)
                .background(
                  appColorScheme.primary,
                  RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                )
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // X축 라벨 (요일)
    Row(
      modifier = Modifier.padding(start = 32.dp)
    ) {
      Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        days.forEach { day ->
          Text(
            text = day,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp)
          )
        }
      }
    }

    // "오늘" 라벨
    Row(
      modifier = Modifier.padding(start = 32.dp, top = 4.dp)
    ) {
      Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        days.forEachIndexed { index, _ ->
          Text(
            text = if (index == days.size - 1) "오늘" else "",
            fontSize = 10.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(32.dp)
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun GraphScreenPreview() {
  Jikgong1111Theme {
    GraphScreen()
  }
}