package com.billcorea.jikgong.presentation.company.main.projectlist.feature.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.billcorea.jikgong.presentation.company.main.projectlist.data.PreviousJobPost
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PreviousJobPostsScreen(
  navController: NavController,
  projectId: String,
  modifier: Modifier = Modifier
) {
  // CompanyMockDataFactory에서 데이터 가져오기
  val previousJobPosts = remember {
    CompanyMockDataFactory.getPreviousJobPosts()
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "기존 공고",
        onBackClick = { navController.popBackStack() }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF8F9FA))
    ) {
      // 안내 메시지
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color(0xFF4B7BFF).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF4B7BFF)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = "이전에 등록했던 공고를 재사용하여 새로운 공고를 빠르게 등록할 수 있습니다.",
            style = AppTypography.bodyMedium,
            color = Color(0xFF4B7BFF),
            fontWeight = FontWeight.Medium
          )
        }
      }

      // 기존 공고 개수
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "총 ",
            style = AppTypography.bodyMedium,
            color = Color.Gray
          )
          Text(
            text = "${previousJobPosts.size}개",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B7BFF)
          )
          Text(
            text = "의 기존 공고",
            style = AppTypography.bodyMedium,
            color = Color.Gray
          )
        }
      }

      // 기존 공고 목록
      if (previousJobPosts.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "기존 공고가 없습니다",
              style = AppTypography.bodyLarge,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "새로운 공고를 등록해보세요",
              style = AppTypography.bodySmall,
              color = Color.Gray
            )
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          items(previousJobPosts) { jobPost ->
            PreviousJobPostCard(
              jobPost = jobPost,
              onReuseClick = {
                // 기존 공고를 재사용하여 새로운 공고 등록 화면으로 이동
                navController.navigate("job_registration?reuseId=${jobPost.id}")
              }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun PreviousJobPostCard(
  jobPost: PreviousJobPost,
  onReuseClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 상단 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          // 카테고리 뱃지
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondaryContainer
          ) {
            Text(
              text = jobPost.category,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = AppTypography.bodySmall,
              color = MaterialTheme.colorScheme.onSecondaryContainer,
              fontWeight = FontWeight.Medium
            )
          }
          
          Spacer(modifier = Modifier.height(8.dp))
          
          Text(
            text = jobPost.title,
            style = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }
        
        // 완료일
        Text(
          text = jobPost.completedDate.format(DateTimeFormatter.ofPattern("MM/dd")),
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 정보 섹션
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow(
          icon = Icons.Outlined.LocationOn,
          text = jobPost.location
        )
        InfoRow(
          icon = Icons.Outlined.AttachMoney,
          text = "일당 ${NumberFormat.getNumberInstance(Locale.KOREA).format(jobPost.wage)}원"
        )
        InfoRow(
          icon = Icons.Outlined.CalendarMonth,
          text = jobPost.workPeriod
        )
        InfoRow(
          icon = Icons.Outlined.Groups,
          text = "모집인원 ${jobPost.maxWorkers}명 • 지원자 ${jobPost.totalApplicants}명"
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 재사용 버튼
      Button(
        onClick = onReuseClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF4B7BFF)
        )
      ) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "이 공고로 재등록",
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium,
          color = Color.White
        )
      }
    }
  }
}

@Composable
private fun InfoRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  text: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      modifier = Modifier.size(18.dp),
      tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = text,
      style = AppTypography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Preview(showBackground = true)
@Composable
fun PreviousJobPostsScreenPreview() {
  Jikgong1111Theme {
    PreviousJobPostsScreen(
      navController = rememberNavController(),
      projectId = "1"
    )
  }
}