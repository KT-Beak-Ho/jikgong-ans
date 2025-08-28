package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobRegistrationScreen(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  var selectedOption by remember { mutableStateOf("") }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      CompanyTopBar(
        title = "일자리 등록",
        showBackButton = true,
        onBackClick = { navController.popBackStack() },
        actions = {
          // 임시 | 기존공고 이용 버튼
          Row(
            modifier = Modifier.padding(end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            TextButton(
              onClick = { 
                selectedOption = "임시"
                navController.navigate("temp_save")
              },
              colors = ButtonDefaults.textButtonColors(
                contentColor = if (selectedOption == "임시") appColorScheme.primary else Color.Gray
              )
            ) {
              Text(
                text = "임시",
                style = AppTypography.bodyMedium,
                fontWeight = if (selectedOption == "임시") FontWeight.Bold else FontWeight.Normal
              )
            }
            
            Text(
              text = "|",
              color = Color.Gray,
              style = AppTypography.bodyMedium
            )
            
            TextButton(
              onClick = { 
                selectedOption = "기존공고 이용"
                navController.navigate("existing_job")
              },
              colors = ButtonDefaults.textButtonColors(
                contentColor = if (selectedOption == "기존공고 이용") appColorScheme.primary else Color.Gray
              )
            ) {
              Text(
                text = "기존공고 이용",
                style = AppTypography.bodyMedium,
                fontWeight = if (selectedOption == "기존공고 이용") FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }
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
      
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        item {
          // 모집 요강 섹션
          RecruitmentSection()
        }
      }
    }
  }
}

@Composable
private fun RecruitmentSection() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Text(
        text = "모집 요강",
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.Black
      )
      
      Spacer(modifier = Modifier.height(16.dp))
      
      // 모집 요강 내용
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        RecruitmentItem(
          label = "모집 직종",
          content = "보통인부, 철근공, 목공 등"
        )
        
        RecruitmentItem(
          label = "모집 인원",
          content = "15명"
        )
        
        RecruitmentItem(
          label = "근무 기간",
          content = "2025-08-01 ~ 2025-08-31"
        )
        
        RecruitmentItem(
          label = "근무 시간",
          content = "08:00 ~ 18:00 (휴게시간 1시간)"
        )
        
        RecruitmentItem(
          label = "급여",
          content = "일당 200,000원"
        )
        
        RecruitmentItem(
          label = "근무 장소",
          content = "서울시 강남구 역삼동 아파트 신축공사 현장"
        )
        
        RecruitmentItem(
          label = "지원 자격",
          content = "건설업 경력자 우대, 안전교육 이수 필수"
        )
        
        RecruitmentItem(
          label = "우대 사항",
          content = "관련 자격증 소지자, 현장 경력 3년 이상"
        )
      }
    }
  }
}

@Composable
private fun RecruitmentItem(
  label: String,
  content: String
) {
  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    Text(
      text = label,
      style = AppTypography.bodyMedium,
      fontWeight = FontWeight.Medium,
      color = appColorScheme.primary
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = content,
      style = AppTypography.bodyMedium,
      color = Color.Black,
      lineHeight = AppTypography.bodyMedium.lineHeight
    )
  }
}

@Preview
@Composable
fun JobRegistrationScreenPreview() {
  Jikgong1111Theme {
    JobRegistrationScreen(
      navController = rememberNavController()
    )
  }
}