package com.billcorea.jikgong.presentation.company.main.scout.feature.pages

import android.annotation.SuppressLint
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.LocationOn
import com.billcorea.jikgong.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.billcorea.jikgong.presentation.company.main.scout.presentation.component.MapLocationDialogOptimized
import com.billcorea.jikgong.presentation.location.LocationPermissionHandler
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import org.koin.androidx.compose.koinViewModel

// 위치 데이터 저장/불러오기 유틸리티
object LocationPreferenceManager {
  private const val PREF_NAME = "location_settings"
  private const val KEY_CURRENT_LOCATION = "current_location"
  private const val KEY_SEARCH_RADIUS = "search_radius"
  private const val KEY_LATITUDE = "latitude"
  private const val KEY_LONGITUDE = "longitude"
  
  fun saveLocationData(
    context: Context,
    location: String,
    radius: Int,
    latitude: Double = 0.0,
    longitude: Double = 0.0
  ) {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    prefs.edit().apply {
      putString(KEY_CURRENT_LOCATION, location)
      putInt(KEY_SEARCH_RADIUS, radius)
      putFloat(KEY_LATITUDE, latitude.toFloat())
      putFloat(KEY_LONGITUDE, longitude.toFloat())
      apply()
    }
  }
  
  fun getLocationData(context: Context): LocationData {
    val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    return LocationData(
      location = prefs.getString(KEY_CURRENT_LOCATION, "서울특별시 강남구") ?: "서울특별시 강남구",
      radius = prefs.getInt(KEY_SEARCH_RADIUS, 10),
      latitude = prefs.getFloat(KEY_LATITUDE, 37.5665f).toDouble(),
      longitude = prefs.getFloat(KEY_LONGITUDE, 126.9780f).toDouble()
    )
  }
}

data class LocationData(
  val location: String,
  val radius: Int,
  val latitude: Double,
  val longitude: Double
)

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
  val context = LocalContext.current
  
  // 저장된 위치 데이터 로드 및 초기화
  LaunchedEffect(Unit) {
    val savedData = LocationPreferenceManager.getLocationData(context)
    if (currentLocation.isEmpty() || currentLocation == "서울특별시 강남구") {
      onLocationChange(savedData.location)
      onRadiusChange(savedData.radius)
    }
  }
  
  // MainViewModel의 위치 정보 실시간 관찰
  val respAddress by viewModel.respAddress.observeAsState("")
  val roadAddress1 by viewModel.roadAddress1.observeAsState(emptyList())
  
  // MainViewModel에서 위치 정보가 업데이트되면 상위 컴포넌트에 알림 및 저장
  LaunchedEffect(respAddress) {
    if (respAddress.isNotEmpty() && respAddress != currentLocation) {
      onLocationChange(respAddress)
      LocationPreferenceManager.saveLocationData(
        context = context,
        location = respAddress,
        radius = searchRadius
      )
    }
  }
  
  LaunchedEffect(roadAddress1) {
    if (roadAddress1.isNotEmpty()) {
      val newAddress = roadAddress1[0].addressName
      if (newAddress != currentLocation && newAddress.isNotEmpty()) {
        onLocationChange(newAddress)
        // 위치 변경 시 저장
        LocationPreferenceManager.saveLocationData(
          context = context,
          location = newAddress,
          radius = searchRadius
        )
      }
    }
  }
  
  // 반경 변경 시에도 저장
  LaunchedEffect(searchRadius) {
    if (currentLocation.isNotEmpty()) {
      LocationPreferenceManager.saveLocationData(
        context = context,
        location = currentLocation,
        radius = searchRadius
      )
    }
  }
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
            .clickable { 
              // GPS 위치 요청 및 주소 변환
              viewModel.setLocation(context)
              onCurrentLocationClick() 
            },
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

    // 지도 미리보기 섹션
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
            text = "현재 위치 미리보기",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            )
          )
          TextButton(
            onClick = { showMapDialog = true }
          ) {
            Icon(
              imageVector = Icons.Default.Fullscreen,
              contentDescription = "전체화면",
              modifier = Modifier.size(16.dp),
              tint = Color(0xFF4B7BFF)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "크게 보기",
              color = Color(0xFF4B7BFF),
              style = MaterialTheme.typography.bodySmall
            )
          }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 지도 미리보기
        CompactMapView(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp)),
          viewModel = viewModel,
          currentLocation = currentLocation,
          searchRadius = searchRadius,
          onLocationSelected = { selectedLocation ->
            onLocationChange(selectedLocation)
          }
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // 지도 안내 텍스트
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.TouchApp,
            contentDescription = "터치",
            modifier = Modifier.size(16.dp),
            tint = Color.Gray
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "지도를 터치하여 정확한 위치를 선택하세요",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
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

  // 지도 다이얼로그 표시 (위치 권한 확인 포함)
  if (showMapDialog) {
    LocationPermissionHandler(
      onPermissionGranted = {
        // 권한이 승인되면 지도 다이얼로그 표시
      },
      onPermissionDenied = {
        showMapDialog = false
      }
    ) {
      MapLocationDialogOptimized(
        onDismiss = { showMapDialog = false },
        onLocationSelected = { selectedLocation ->
          onLocationChange(selectedLocation)
        },
        mainViewModel = viewModel,
        searchRadius = searchRadius
      )
    }
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

@SuppressLint("UseCompatLoadingForDrawables")
@Composable
private fun CompactMapView(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel,
  currentLocation: String,
  searchRadius: Int,
  onLocationSelected: (String) -> Unit
) {
  val context = LocalContext.current
  
  // KakaoMap SDK 초기화 확인 - try-catch로 안전하게 처리
  var isMapAvailable by remember { mutableStateOf(true) }
  var mapError by remember { mutableStateOf<String?>(null) }
  
  // 지도 사용 불가능한 경우 대체 UI 표시
  if (!isMapAvailable || mapError != null) {
    Box(
      modifier = modifier
        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
        .padding(20.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = "위치",
          tint = Color(0xFF9CA3AF),
          modifier = Modifier.size(48.dp)
        )
        Text(
          text = mapError ?: "지도 미리보기",
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF6B7280)
        )
        Text(
          text = currentLocation,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF9CA3AF),
          textAlign = TextAlign.Center
        )
      }
    }
    return
  }
  
  // MapView 생성 시 안전하게 처리
  val mapView = remember { 
    try {
      MapView(context)
    } catch (e: Exception) {
      android.util.Log.e("CompactMapView", "MapView creation failed: ${e.message}")
      isMapAvailable = false
      mapError = "지도를 초기화할 수 없습니다"
      null
    }
  }
  
  // MapView가 null인 경우 대체 UI 표시
  if (mapView == null) {
    Box(
      modifier = modifier
        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
        .padding(20.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = "위치",
          tint = Color(0xFF9CA3AF),
          modifier = Modifier.size(48.dp)
        )
        Text(
          text = "지도 미리보기",
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF6B7280)
        )
        Text(
          text = currentLocation,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF9CA3AF),
          textAlign = TextAlign.Center
        )
      }
    }
    return
  }
  
  // 현재 위치 좌표 (기본값: 서울 시청)
  var centerPosition by remember { mutableStateOf(LatLng.from(37.5665, 126.9780)) }
  var kakaoMap: KakaoMap? by remember { mutableStateOf(null) }
  
  // 주소 검색 결과 관찰
  val roadAddress by viewModel.roadAddress1.observeAsState(emptyList())
  
  // 주소가 업데이트되면 콜백 호출
  LaunchedEffect(roadAddress) {
    if (roadAddress.isNotEmpty()) {
      onLocationSelected(roadAddress[0].addressName)
    }
  }
  
  // 컴포넌트가 해제될 때 지도 정리
  DisposableEffect(mapView) {
    onDispose {
      try {
        mapView.finish()
      } catch (e: Exception) {
        // 이미 해제된 경우 무시
      }
    }
  }
  
  AndroidView(
    modifier = modifier,
    factory = { context ->
      mapView.apply {
        try {
          start(
          object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
              kakaoMap = null
            }
            override fun onMapError(exception: Exception?) {
              // 지도 오류 로그
              android.util.Log.e("CompactMapView", "Map error: ${exception?.message}")
            }
          },
          object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
              kakaoMap = map
              
              try {
                // 카메라를 현재 위치로 이동
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(centerPosition, 15)
                map.moveCamera(cameraUpdate)
                
                // 현재 위치에 마커 표시
                val style = map.labelManager?.addLabelStyles(
                  LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                )
                
                if (style != null) {
                  val options = LabelOptions.from(centerPosition).setStyles(style)
                  map.labelManager?.layer?.addLabel(options)
                }
                
                // 지도 롱클릭 리스너 (터치 후 꾹 누르기)
                map.setOnTerrainLongClickListener { _, latLng, _ ->
                  latLng?.let { newLatLng ->
                    centerPosition = newLatLng
                    
                    try {
                      // 기존 마커 제거 후 새 마커 추가
                      map.labelManager?.layer?.removeAll()
                      if (style != null) {
                        val newOptions = LabelOptions.from(newLatLng).setStyles(style)
                        map.labelManager?.layer?.addLabel(newOptions)
                      }
                      
                      // 카메라 이동
                      val newCameraUpdate = CameraUpdateFactory.newCenterPosition(newLatLng, 15)
                      map.moveCamera(newCameraUpdate)
                      
                      // 주소 검색
                      viewModel.doFindAddress(newLatLng.latitude, newLatLng.longitude)
                    } catch (e: Exception) {
                      android.util.Log.e("CompactMapView", "Error updating marker: ${e.message}")
                    }
                  }
                }
                
                // POI 클릭 리스너
                map.setOnPoiClickListener { _, latLng, _, _ ->
                  latLng?.let { newLatLng ->
                    centerPosition = newLatLng
                    
                    try {
                      // 기존 마커 제거 후 새 마커 추가
                      map.labelManager?.layer?.removeAll()
                      if (style != null) {
                        val newOptions = LabelOptions.from(newLatLng).setStyles(style)
                        map.labelManager?.layer?.addLabel(newOptions)
                      }
                      
                      // 카메라 이동
                      val newCameraUpdate = CameraUpdateFactory.newCenterPosition(newLatLng, 15)
                      map.moveCamera(newCameraUpdate)
                      
                      // 주소 검색
                      viewModel.doFindAddress(newLatLng.latitude, newLatLng.longitude)
                    } catch (e: Exception) {
                      android.util.Log.e("CompactMapView", "Error updating marker: ${e.message}")
                    }
                  }
                }
              } catch (e: Exception) {
                android.util.Log.e("CompactMapView", "Error initializing map: ${e.message}")
              }
            }
            
            override fun getPosition(): LatLng {
              return centerPosition
            }
          }
          )
        } catch (e: Exception) {
          android.util.Log.e("CompactMapView", "MapView start failed: ${e.message}")
          isMapAvailable = false
          mapError = "지도를 시작할 수 없습니다"
        }
      }
    }
  )
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