package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 임시저장 데이터
data class TempSavePost(
  val id: String,
  val title: String,
  val saveDate: LocalDateTime
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempSaveScreen(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  // 샘플 임시저장 데이터 (빈 리스트로 시작해서 없는 경우 테스트 가능)
  var tempSavePosts by remember { 
    mutableStateOf(
      listOf(
        TempSavePost("1", "아파트 신축공사 철근 작업자 모집", LocalDateTime.now().minusDays(1)),
        TempSavePost("2", "사무실 인테리어 목공 인력 모집", LocalDateTime.now().minusDays(3)),
        TempSavePost("3", "상가건물 전기공 모집", LocalDateTime.now().minusDays(7))
      )
    )
  }
  
  var showDeleteDialog by remember { mutableStateOf(false) }
  var selectedPost by remember { mutableStateOf<TempSavePost?>(null) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "임시저장",
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(
              Icons.Default.ArrowBack,
              contentDescription = "뒤로가기"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
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
      
      if (tempSavePosts.isEmpty()) {
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
                text = "${tempSavePosts.size}건",
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = appColorScheme.primary
              )
            }
          }
          
          items(tempSavePosts) { post ->
            TempSaveCard(
              post = post,
              onDeleteClick = {
                selectedPost = post
                showDeleteDialog = true
              }
            )
          }
        }
      }
    }
  }
  
  // 삭제 확인 다이얼로그
  if (showDeleteDialog && selectedPost != null) {
    DeleteConfirmDialog(
      onDismiss = { 
        showDeleteDialog = false
        selectedPost = null
      },
      onConfirm = {
        tempSavePosts = tempSavePosts.filter { it.id != selectedPost?.id }
        showDeleteDialog = false
        selectedPost = null
      }
    )
  }
}

@Composable
private fun TempSaveCard(
  post: TempSavePost,
  onDeleteClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 좌측 정보
      Column(
        modifier = Modifier.weight(1f)
      ) {
        // 임시저장 날짜
        Text(
          text = post.saveDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")),
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 게시물 이름
        Text(
          text = post.title,
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium,
          color = Color.Black
        )
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
              containerColor = appColorScheme.primary
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