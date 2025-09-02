package com.billcorea.jikgong.presentation.company.main.scout.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.scout.components.MapLocationDialog
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationSettingPage(
  modifier: Modifier = Modifier,
  currentLocation: String,
  searchRadius: Int,
  onLocationChange: (String) -> Unit,
  onRadiusChange: (Int) -> Unit,
  onCurrentLocationClick: () -> Unit,
  viewModel: MainViewModel = koinViewModel()
) {
  var showMapDialog by remember { mutableStateOf(false) }
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(20.dp)
  ) {
    // 현재 위치 섹션
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
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        Text(
          text = "현재 위치",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 현재 위치 표시
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCurrentLocationClick() },
          color = Color(0xFFF5F5F5)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "위치",
              tint = Color(0xFF4B7BFF),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = currentLocation,
                style = MaterialTheme.typography.bodyLarge.copy(
                  fontWeight = FontWeight.Medium
                )
              )
              Text(
                text = "탭하여 현재 위치 재설정",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
              )
            }
            Icon(
              imageVector = Icons.Default.Refresh,
              contentDescription = "새로고침",
              tint = Color(0xFF4B7BFF),
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 위치 변경 버튼
        Button(
          onClick = { showMapDialog = true },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4B7BFF)
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "검색",
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("지도에서 위치 검색")
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // 검색 반경 설정 섹션
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
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "검색 반경",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            )
          )
          Text(
            text = "${searchRadius}km",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = Color(0xFF4B7BFF)
            )
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 슬라이더
        Slider(
          value = searchRadius.toFloat(),
          onValueChange = { onRadiusChange(it.toInt()) },
          valueRange = 1f..50f,
          steps = 48,
          colors = SliderDefaults.colors(
            thumbColor = Color(0xFF4B7BFF),
            activeTrackColor = Color(0xFF4B7BFF),
            inactiveTrackColor = Color(0xFFE5E8EB)
          )
        )

        // 범위 라벨
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "1km",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
          )
          Text(
            text = "50km",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 추천 반경 버튼들
        Text(
          text = "추천 반경",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium
          ),
          color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          RadiusChip(
            text = "5km",
            isSelected = searchRadius == 5,
            onClick = { onRadiusChange(5) }
          )
          RadiusChip(
            text = "10km",
            isSelected = searchRadius == 10,
            onClick = { onRadiusChange(10) }
          )
          RadiusChip(
            text = "20km",
            isSelected = searchRadius == 20,
            onClick = { onRadiusChange(20) }
          )
          RadiusChip(
            text = "30km",
            isSelected = searchRadius == 30,
            onClick = { onRadiusChange(30) }
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // 안내 메시지
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = Color(0xFFF0F7FF)
      )
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.Top
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = "정보",
          tint = Color(0xFF4B7BFF),
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = "검색 반경 안내",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold
            )
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "검색 반경이 넓을수록 더 많은 인력을 찾을 수 있지만, 거리가 먼 인력도 포함됩니다.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.5
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(80.dp))
  }

  // 지도 다이얼로그 표시
  if (showMapDialog) {
    MapLocationDialog(
      onDismiss = { showMapDialog = false },
      onLocationSelected = { selectedLocation ->
        onLocationChange(selectedLocation)
      },
      viewModel = viewModel,
      searchRadius = searchRadius
    )
  }
}

@Composable
private fun RadiusChip(
  text: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    modifier = Modifier
      .clip(RoundedCornerShape(20.dp))
      .clickable { onClick() },
    color = if (isSelected) Color(0xFF4B7BFF) else Color(0xFFF5F5F5)
  ) {
    Text(
      text = text,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
      style = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
      ),
      color = if (isSelected) Color.White else Color.Gray
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun LocationSettingPagePreview() {
  Jikgong1111Theme {
    LocationSettingPage(
      currentLocation = "서울특별시 강남구",
      searchRadius = 10,
      onLocationChange = {},
      onRadiusChange = {},
      onCurrentLocationClick = {},
      viewModel = MainViewModel()
    )
  }
}