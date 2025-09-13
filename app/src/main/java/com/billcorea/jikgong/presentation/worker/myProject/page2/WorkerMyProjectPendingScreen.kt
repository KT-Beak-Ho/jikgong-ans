package com.billcorea.jikgong.presentation.worker.myProject.page2

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.worker.common.WorkerBottomNav
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

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
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = { 
          Text(
            "내 일자리", 
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = appColorScheme.onSurface
          ) 
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack, 
              contentDescription = "Back",
              tint = appColorScheme.onSurface
            )
          }
        },
        actions = {
          IconButton(onClick = { /* TODO notification */ }) {
            Icon(
              Icons.Default.NotificationsNone, 
              contentDescription = "Notifications",
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
    },
    bottomBar = {
      WorkerBottomNav(
        doWorkerProjectList = { /* TODO */ },
        doWorkerMyjob = { /* TODO */ },
        doWorkerEarning = { /* TODO */ },
        doWorkerProfile = { /* TODO */ }
      )
    }
  ) { inner ->
    Column(
      modifier = Modifier
        .padding(inner)
        .fillMaxSize()
        .background(Color(0xFFF8F9FA))
    ) {
      TabRow(
        selectedTabIndex = selectedTab.intValue,
        containerColor = Color.White
      ) {
        tabs.forEachIndexed { index, label ->
          Tab(
            selected = selectedTab.intValue == index,
            onClick = { selectedTab.intValue = index },
            text = {
              Text(
                label,
                style = AppTypography.bodyLarge,
                color = if (selectedTab.intValue == index)
                  appColorScheme.primary else appColorScheme.onSurfaceVariant
              )
            }
          )
        }
      }

      if (groups.isNotEmpty()) {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          groups.forEach { group ->
            stickyHeader {
              Text(
                text = group.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.KOREAN)) + " 지원",
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.White, RoundedCornerShape(8.dp))
                  .padding(horizontal = 16.dp, vertical = 12.dp),
                style = AppTypography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = appColorScheme.primary
              )
            }
            items(group.jobs) { job ->
              JobCard(job)
            }
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
            text = "신청한 일자리가 없습니다.",
            style = AppTypography.bodyLarge,
            color = appColorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Composable
private fun JobCard(job: JobApplication) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
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
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = job.title,
          style = AppTypography.titleMedium,
          color = appColorScheme.primary,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        AssistChip(
          onClick = {},
          label = { 
            Text(
              "진행중", 
              style = AppTypography.bodySmall
            ) 
          },
          colors = AssistChipDefaults.assistChipColors(
            containerColor = appColorScheme.primaryContainer.copy(alpha = 0.3f),
            labelColor = appColorScheme.primary
          )
        )
      }
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        job.projectName, 
        style = AppTypography.bodyMedium,
        color = appColorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(16.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          Icons.Default.CalendarToday, 
          contentDescription = null, 
          modifier = Modifier.size(18.dp),
          tint = appColorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          job.supportDate, 
          style = AppTypography.bodyMedium,
          color = appColorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(24.dp))
        Icon(
          Icons.Default.People, 
          contentDescription = null, 
          modifier = Modifier.size(18.dp),
          tint = appColorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          "지원자수: ${job.applicantCount}명", 
          style = AppTypography.bodyMedium,
          color = appColorScheme.onSurfaceVariant
        )
      }
      Spacer(modifier = Modifier.height(16.dp))
      OutlinedButton(
        onClick = { /*TODO: cancel*/ },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          contentColor = MaterialTheme.colorScheme.error
        )
      ) {
        Text(
          "지원 취소", 
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
      }
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

private fun emptyJobGroups(): List<JobGroup> {
  return emptyList()
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
private fun WorkerMyProjectPendingScreenProgress() {
  Jikgong1111Theme {
    WorkerMyProjectPendingScreen()
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun WorkerMyProjectPendingScreenEmptyState(
  modifier: Modifier = Modifier,
  onBack: () -> Unit = {}
) {
  val selectedTab = remember { mutableIntStateOf(1) }
  val tabs = listOf("확정", "진행중", "마감")
  val groups = remember { emptyJobGroups() }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = { 
          Text(
            "내 일자리", 
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = appColorScheme.onSurface
          ) 
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack, 
              contentDescription = "Back",
              tint = appColorScheme.onSurface
            )
          }
        },
        actions = {
          IconButton(onClick = { /* TODO notification */ }) {
            Icon(
              Icons.Default.NotificationsNone, 
              contentDescription = "Notifications",
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
    },
    bottomBar = {
      WorkerBottomNav(
        doWorkerProjectList = { /* TODO */ },
        doWorkerMyjob = { /* TODO */ },
        doWorkerEarning = { /* TODO */ },
        doWorkerProfile = { /* TODO */ }
      )
    }
  ) { inner ->
    Column(
      modifier = Modifier
        .padding(inner)
        .fillMaxSize()
        .background(Color(0xFFF8F9FA))
    ) {
      TabRow(
        selectedTabIndex = selectedTab.intValue,
        containerColor = Color.White
      ) {
        tabs.forEachIndexed { index, label ->
          Tab(
            selected = selectedTab.intValue == index,
            onClick = { selectedTab.intValue = index },
            text = {
              Text(
                label,
                style = AppTypography.bodyLarge,
                color = if (selectedTab.intValue == index)
                  appColorScheme.primary else appColorScheme.onSurfaceVariant
              )
            }
          )
        }
      }

      if (groups.isNotEmpty()) {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          groups.forEach { group ->
            stickyHeader {
              Text(
                text = group.date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.KOREAN)) + " 지원",
                modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.White, RoundedCornerShape(8.dp))
                  .padding(horizontal = 16.dp, vertical = 12.dp),
                style = AppTypography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = appColorScheme.primary
              )
            }
            items(group.jobs) { job ->
              JobCard(job)
            }
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
            text = "신청한 일자리가 없습니다.",
            style = AppTypography.bodyLarge,
            color = appColorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
private fun WorkerMyProjectPendingScreenEmpty() {
  Jikgong1111Theme {
    WorkerMyProjectPendingScreenEmptyState()
  }
}
