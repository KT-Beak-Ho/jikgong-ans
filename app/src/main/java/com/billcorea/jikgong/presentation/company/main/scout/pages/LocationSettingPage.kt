package com.billcorea.jikgong.presentation.company.main.scout.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun LocationSettingPage(
  modifier: Modifier = Modifier,
  currentLocation: String,
  searchRadius: Int,
  onLocationChange: (String) -> Unit,
  onRadiusChange: (Int) -> Unit,
  onCurrentLocationClick: () -> Unit
) {
  var tempLocation by remember { mutableStateOf(currentLocation) }
  var tempRadius by remember { mutableIntStateOf(searchRadius) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    // 현재 위치 카드
    CurrentLocationCard(
      currentLocation = currentLocation,
      onCurrentLocationClick = onCurrentLocationClick
    )

    // 위치 직접 입력
    LocationInputCard(
      location = tempLocation,
      onLocationChange = {
        tempLocation = it
        onLocationChange(it)
      }
    )

    // 검색 반경 설정
    SearchRadiusCard(
      radius = tempRadius,
      onRadiusChange = {
        tempRadius = it
        onRadiusChange(it)
      }
    )

    // 위치 정보 안내
    LocationInfoCard()

    Spacer(modifier = Modifier.height(80.dp))
  }
}

@Composable
private fun CurrentLocationCard(
  currentLocation: String,
  onCurrentLocationClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "현재 위치",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            )
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = currentLocation,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF4B7BFF)
          )
        }

        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = "위치",
          modifier = Modifier.size(32.dp),
          tint = Color(0xFF4B7BFF)
        )
      }

      Button(
        onClick = onCurrentLocationClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF4B7BFF)
        )
      ) {
        Icon(
          imageVector = Icons.Default.MyLocation,
          contentDescription = "현재 위치",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("현재 위치로 설정")
      }
    }
  }
}

@Composable
private fun LocationInputCard(
  location: String,
  onLocationChange: (String) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Text(
        text = "위치 직접 입력",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold
        )
      )

      OutlinedTextField(
        value = location,
        onValueChange = onLocationChange,
        label = { Text("주소 입력") },
        placeholder = { Text("예: 서울특별시 강남구") },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "검색",
            tint = Color.Gray
          )
        },
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = Color(0xFF4B7BFF),
          unfocusedBorderColor = Color(0xFFE5E8EB)
        )
      )

      Text(
        text = "도로명 주소 또는 지번 주소를 입력하세요",
        style = MaterialTheme.typography.bodySmall,
        color = Color.Gray
      )
    }
  }
}

@Composable
private fun SearchRadiusCard(
  radius: Int,
  onRadiusChange: (Int) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
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

        Surface(
          shape = MaterialTheme.shapes.small,
          color = Color(0xFF4B7BFF).copy(alpha = 0.1f)
        ) {
          Text(
            text = "${radius}km",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF4B7BFF)
          )
        }
      }

      Slider(
        value = radius.toFloat(),
        onValueChange = { onRadiusChange(it.toInt()) },
        valueRange = 1f..50f,
        steps = 48,
        colors = SliderDefaults.colors(
          thumbColor = Color(0xFF4B7BFF),
          activeTrackColor = Color(0xFF4B7BFF),
          inactiveTrackColor = Color(0xFFE5E8EB)
        )
      )

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

      // 추천 반경 칩들
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(5, 10, 20, 30).forEach { recommendedRadius ->
          FilterChip(
            selected = radius == recommendedRadius,
            onClick = { onRadiusChange(recommendedRadius) },
            label = { Text("${recommendedRadius}km") },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}

@Composable
private fun LocationInfoCard() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFF5F5F5)
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = "정보",
          modifier = Modifier.size(20.dp),
          tint = Color(0xFF4B7BFF)
        )
        Column(
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "위치 설정 안내",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold
            )
          )

          Text(
            text = "• 설정한 위치를 기준으로 주변 인력을 검색합니다",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
          )

          Text(
            text = "• 검색 반경이 넓을수록 더 많은 인력을 찾을 수 있습니다",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
          )

          Text(
            text = "• 위치 정보는 인력 매칭에만 사용됩니다",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
          )
        }
      }
    }
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
      onCurrentLocationClick = {}
    )
  }
}