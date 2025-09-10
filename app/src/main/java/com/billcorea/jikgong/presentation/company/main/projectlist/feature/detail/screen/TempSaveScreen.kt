package com.billcorea.jikgong.presentation.company.main.projectlist.feature.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.TempSavedJob
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun TempSaveScreen(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  // CompanyMockDataFactory에서 임시저장 데이터 가져오기
  var tempSavedJobs by remember { 
    mutableStateOf(CompanyMockDataFactory.getTempSavedJobs())
  }
  
  var showDeleteDialog by remember { mutableStateOf(false) }
  var selectedJob by remember { mutableStateOf<TempSavedJob?>(null) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "임시저장",
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
      Divider(thickness = 1.dp, color = Color.LightGray)
      
      if (tempSavedJobs.isEmpty()) {
        // 임시저장글이 없는 경우
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "임시저장글이 없습니다",
            style = AppTypography.bodyLarge,
            color = Color.Gray
          )
        }
      } else {
        // 임시저장글이 있는 경우
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            // 총 건수 표시
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "총 ",
                style = AppTypography.bodyMedium,
                color = Color.Gray
              )
              Text(
                text = "${tempSavedJobs.size}건",
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = appColorScheme.primary
              )
            }
          }
          
          items(tempSavedJobs) { job ->
            TempSaveCard(
              job = job,
              onLoadClick = {
                // 임시저장된 내용을 불러와서 새 공고 작성 화면으로 이동
                navController.navigate("job_registration/${job.id}")
              },
              onDeleteClick = {
                selectedJob = job
                showDeleteDialog = true
              }
            )
          }
        }
      }
    }
  }
  
  // 삭제 확인 다이얼로그
  if (showDeleteDialog && selectedJob != null) {
    DeleteConfirmDialog(
      onDismiss = { 
        showDeleteDialog = false
        selectedJob = null
      },
      onConfirm = {
        tempSavedJobs = tempSavedJobs.filter { it.id != selectedJob?.id }
        showDeleteDialog = false
        selectedJob = null
      }
    )
  }
}

@Composable
private fun TempSaveCard(
  job: TempSavedJob,
  onLoadClick: () -> Unit,
  onDeleteClick: () -> Unit
) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
  
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onLoadClick() },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        // 좌측 정보
        Column(
          modifier = Modifier.weight(1f)
        ) {
          // 임시저장 날짜
          Text(
            text = "임시저장: ${job.savedDate.format(dateFormatter)}",
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
          
          Spacer(modifier = Modifier.height(4.dp))
          
          // 제목
          Text(
            text = job.title,
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black
          )
          
          Spacer(modifier = Modifier.height(4.dp))
          
          // 상세 정보
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            if (job.jobType.isNotEmpty()) {
              Text(
                text = job.jobType,
                style = AppTypography.bodySmall,
                color = Color(0xFF666666)
              )
            }
            if (job.location.isNotEmpty()) {
              Text(
                text = "• ${job.location}",
                style = AppTypography.bodySmall,
                color = Color(0xFF666666)
              )
            }
          }
        }
        
        // 우측 삭제 버튼
        IconButton(
          onClick = onDeleteClick,
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            Icons.Default.Delete,
            contentDescription = "삭제",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
          )
        }
      }
      
      Spacer(modifier = Modifier.height(12.dp))
      
      // 작성 진행률
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "작성 진행률",
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
          Text(
            text = "${job.completionRate}%",
            style = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = if (job.completionRate == 100) appColorScheme.primary else Color(0xFF666666)
          )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 진행률 바
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(
              color = Color(0xFFE0E0E0),
              shape = RoundedCornerShape(2.dp)
            )
        ) {
          Box(
            modifier = Modifier
              .fillMaxWidth(job.completionRate / 100f)
              .fillMaxHeight()
              .background(
                color = if (job.completionRate == 100) appColorScheme.primary else Color(0xFF90CAF9),
                shape = RoundedCornerShape(2.dp)
              )
          )
        }
      }
    }
  }
}

@Composable
private fun DeleteConfirmDialog(
  onDismiss: () -> Unit,
  onConfirm: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // 제목 (중앙 정렬, Bold)
        Text(
          text = "임시저장글 삭제",
          style = AppTypography.titleMedium,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 설명 문구
        Text(
          text = "임시저장글을 삭제하시겠습니까?\n삭제된 글은 복구되지 않습니다.",
          style = AppTypography.bodyMedium,
          color = Color.Black,
          textAlign = TextAlign.Center,
          lineHeight = AppTypography.bodyMedium.lineHeight
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 버튼들
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("취소")
          }
          
          Button(
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF4B7BFF)
            )
          ) {
            Text(
              text = "삭제",
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun TempSaveScreenPreview() {
  Jikgong1111Theme {
    TempSaveScreen(
      navController = rememberNavController()
    )
  }
}