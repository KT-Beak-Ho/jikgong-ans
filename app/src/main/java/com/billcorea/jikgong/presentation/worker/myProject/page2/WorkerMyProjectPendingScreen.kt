package com.billcorea.jikgong.presentation.worker.myProject.page2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WorkerMyProjectPendingScreen(
  modifier: Modifier = Modifier,
  onBack: () -> Unit = {}
) {
  val selectedTab = remember { mutableIntStateOf(1) } // 0: 확정, 1: 진행중, 2: 마감
  val tabs = listOf("확정", "진행중", "마감")

  // Fake sample data grouped by date
  val groups = remember { sampleJobGroups() }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("내 일자리", fontWeight = FontWeight.Bold) },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = { /* TODO notification */ }) {
            Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications")
          }
        }
      )
    },
    bottomBar = { BottomNavigationBar(selected = 1) }
  ) { inner ->
    Column(modifier = modifier.padding(inner)) {
      TabRow(selectedTabIndex = selectedTab.intValue) {
        tabs.forEachIndexed { index, label ->
          Tab(selected = selectedTab.intValue == index,
            onClick = { selectedTab.intValue = index }) {
            Text(label, modifier = Modifier.padding(vertical = 12.dp))
          }
        }
      }

      LazyColumn(modifier = Modifier.fillMaxSize()) {
        groups.forEach { group ->
          stickyHeader {
            Text(
              text = group.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.KOREAN)) + " 지원",
              modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 8.dp),
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
          }
          items(group.jobs) { job ->
            JobCard(job)
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    }
  }
}

@Composable
private fun JobCard(job: JobApplication) {
  Card(modifier = Modifier
    .fillMaxWidth()
    .padding(horizontal = 16.dp)) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = job.title,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold,
          fontSize = 16.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        AssistChip(
          onClick = {},
          label = { Text("진행중", fontSize = 11.sp) },
          colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFE5E5E5))
        )
      }
      Spacer(modifier = Modifier.height(2.dp))
      Text(job.projectName, fontSize = 13.sp)
      Spacer(modifier = Modifier.height(12.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(job.supportDate, fontSize = 12.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("지원자수: ${job.applicantCount}명", fontSize = 12.sp)
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(onClick = { /*TODO: cancel*/ }) {
          Text("지원 취소", color = MaterialTheme.colorScheme.error)
        }
      }
    }
  }
}

@Composable
private fun BottomNavigationBar(selected: Int) {
  NavigationBar {
    val items = listOf("일자리 목록", "내 일자리", "수익 관리", "내 정보")
    items.forEachIndexed { index, label ->
      NavigationBarItem(selected = selected == index,
        onClick = { /*TODO*/ },
        icon = { /* TODO: icon resource*/ Box(Modifier.size(24.dp)) },
        label = { Text(label, fontSize = 10.sp) })
    }
  }
}

// --- Sample Data & Models ---
private data class JobApplication(
  val title: String,
  val projectName: String,
  val supportDate: String,
  val applicantCount: Int
)

private data class JobGroup(val date: LocalDate, val jobs: List<JobApplication>)

private fun sampleJobGroups(): List<JobGroup> {
  return listOf(
    JobGroup(
      date = LocalDate.of(2024, 1, 21),
      jobs = listOf(
        JobApplication("보통인부 10명", "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사", "1월 25일까지 지원", 2),
        JobApplication("보통인부 10명", "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사", "1월 27일까지 지원", 25),
        JobApplication("보통인부 10명", "다른 프로젝트 1", "1월 26일까지 지원", 7)
      )
    ),
    JobGroup(
      date = LocalDate.of(2024, 1, 20),
      jobs = listOf(
        JobApplication("보통인부 10명", "다른 프로젝트 2", "1월 28일까지 지원", 8)
      )
    )
  )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
private fun WorkerMyProjectPendingScreenProgress() {
  MaterialTheme {
    WorkerMyProjectPendingScreen()
  }
}
