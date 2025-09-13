package com.billcorea.jikgong.presentation.worker.projectList.page3

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ApplyProject(
  onViewApplicationClick: () -> Unit = {},
  onGoBackClick: () -> Unit = {}
) {
  val configuration = LocalConfiguration.current
  val screenHeight = configuration.screenHeightDp.dp
  val screenWidth = configuration.screenWidthDp.dp

  Scaffold(
    topBar = {
      TopAppBar(
        title = { },
        navigationIcon = {
          IconButton(onClick = onGoBackClick) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "뒤로가기"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    },
    containerColor = Color.White
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 24.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Spacer(modifier = Modifier.height(80.dp))

      // 제목 텍스트
      Text(
        text = "지원이 완료되었어요",
        style = TextStyle(
          fontSize = 24.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(24.dp))

      // 설명 텍스트
      Text(
        text = "신청한 내역을 자세히 관리할 수 있어요",
        style = TextStyle(
          fontSize = 16.sp,
          fontWeight = FontWeight.Normal,
          color = Color.Gray
        ),
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(60.dp))

      // 신청 내역 보기 버튼
      Button(
        onClick = onViewApplicationClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "신청 내역 보기",
          style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
          )
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 뒤로 가기 버튼
      Button(
        onClick = onGoBackClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.White,
          contentColor = Color.Black
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        shape = RoundedCornerShape(8.dp)
      ) {
        Text(
          text = "뒤로 가기",
          style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
          )
        )
      }

      Spacer(modifier = Modifier.weight(1f))
    }
  }
}

@Preview(showBackground = true)
@Composable
fun ApplicationCompleteScreenPreview() {
  MaterialTheme {
    ApplyProject()
  }
}