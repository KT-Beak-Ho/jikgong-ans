package com.billcorea.jikgong.presentation.company.main.scout.presentation.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.location.LocationPermissionHandler
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.LaunchedEffect
import com.billcorea.jikgong.api.models.location.Coord2RoadAddress
import com.billcorea.jikgong.api.models.location.AddressFindRoadAddress
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.style.TextOverflow
import com.billcorea.jikgong.utils.MainViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiImage
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation

@Composable
fun MapLocationDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (String) -> Unit,
    viewModel: MainViewModel,
    searchRadius: Int = 10
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    
    // MapView 생성 시 안전하게 처리
    val mapView = remember { 
        try {
            MapView(context)
        } catch (e: Exception) {
            Log.e("MapLocationDialog", "Failed to create MapView: ${e.message}")
            null
        }
    }
    
    // MapView가 null인 경우 다이얼로그 닫기
    if (mapView == null) {
        LaunchedEffect(Unit) {
            onDismiss()
        }
        return
    }
    
    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }
    var isGpsLocationLoaded by remember { mutableStateOf(false) }
    var kakaoMapInstance by remember { mutableStateOf<KakaoMap?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    
    val _roadAddress1 = viewModel.roadAddress1.observeAsState(emptyList())
    val roadAddress1 = _roadAddress1.value
    val _roadAddress = viewModel.roadAddress.observeAsState(emptyList())
    val searchResults = _roadAddress.value
    val currentLat by viewModel.lat.observeAsState(37.5665)
    val currentLon by viewModel.lon.observeAsState(126.9780)
    
    // GPS 위치가 로드되면 현재 위치로, 아니면 서울 중심으로 설정
    val centerPosition = remember(currentLat, currentLon, isGpsLocationLoaded) { 
        if (isGpsLocationLoaded && currentLat != 0.0 && currentLon != 0.0) {
            LatLng.from(currentLat, currentLon)
        } else {
            LatLng.from(37.5665, 126.9780)
        }
    }

    // GPS 위치 업데이트를 위한 LaunchedEffect
    LaunchedEffect(currentLat, currentLon, isGpsLocationLoaded, kakaoMapInstance) {
        if (isGpsLocationLoaded && currentLat != 0.0 && currentLon != 0.0) {
            val mapInstance = kakaoMapInstance
            if (mapInstance != null) {
                try {
                    val gpsPosition = LatLng.from(currentLat, currentLon)
                    
                    // 카메라 이동
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(gpsPosition)
                    mapInstance.moveCamera(cameraUpdate)
                    
                    // 마커 표시
                    mapInstance.labelManager?.let { labelManager ->
                        val style = labelManager.addLabelStyles(
                            LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                        )
                        if (style != null) {
                            val options = LabelOptions.from(gpsPosition).setStyles(style)
                            val layer = labelManager.layer
                            layer?.removeAll()
                            layer?.addLabel(options)
                        }
                    }
                    
                    // GPS 위치로 주소 검색
                    viewModel.doFindAddress(currentLat, currentLon)
                } catch (e: Exception) {
                    Log.e("MapLocationDialog", "GPS location update error: ${e.message}")
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 헤더
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "위치 선택",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    Row {
                        // GPS 현재 위치 버튼
                        IconButton(
                            onClick = {
                                viewModel.setLocation(context)
                                isGpsLocationLoaded = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "현재 위치",
                                tint = Color(0xFF4B7BFF)
                            )
                        }
                        
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "닫기"
                            )
                        }
                    }
                }

                // 주소 검색 바
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    placeholder = { Text("주소나 건물명을 검색하세요") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            tint = Color(0xFF4B7BFF)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchQuery.trim().isNotEmpty()) {
                                try {
                                    viewModel.doKakaoGeocoding(searchQuery.trim())
                                } catch (e: Exception) {
                                    Log.e("MapLocationDialog", "Search error: ${e.message}")
                                }
                            }
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = Color(0xFF4B7BFF)
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 검색 결과 표시
                if (searchResults.isNotEmpty() && searchQuery.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(searchResults) { address ->
                                SearchResultItem(
                                    address = address,
                                    onClick = {
                                        try {
                                            // 검색 결과 클릭시 지도로 이동하고 주소 설정
                                            val lat = address.y.toDoubleOrNull()
                                            val lon = address.x.toDoubleOrNull()
                                            
                                            if (lat != null && lon != null) {
                                                val position = LatLng.from(lat, lon)
                                                val mapInstance = kakaoMapInstance
                                                if (mapInstance != null) {
                                                    try {
                                                        val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
                                                        mapInstance.moveCamera(cameraUpdate)
                                                        
                                                        // 마커 표시
                                                        mapInstance.labelManager?.let { labelManager ->
                                                            val style = labelManager.addLabelStyles(
                                                                LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                                                            )
                                                            if (style != null) {
                                                                val options = LabelOptions.from(position).setStyles(style)
                                                                val layer = labelManager.layer
                                                                layer?.removeAll()
                                                                layer?.addLabel(options)
                                                            }
                                                        }
                                                        
                                                        // 좌표로 상세 주소 검색
                                                        viewModel.doFindAddress(lat, lon)
                                                    } catch (e: Exception) {
                                                        Log.e("MapLocationDialog", "Map operation error: ${e.message}")
                                                    }
                                                }
                                                
                                                // 검색 결과 리스트 숨기기
                                                try {
                                                    viewModel._roadAddress.value = emptyList()
                                                    searchQuery = ""
                                                } catch (e: Exception) {
                                                    Log.e("MapLocationDialog", "Clear search results error: ${e.message}")
                                                }
                                            }
                                        } catch (e: Exception) {
                                            Log.e("MapLocationDialog", "Address selection error: ${e.message}")
                                        }
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // 지도 뷰
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.6).dp)
                        .padding(horizontal = 16.dp),
                    factory = { context ->
                        mapView.apply {
                            try {
                                start(
                                object : MapLifeCycleCallback() {
                                    override fun onMapDestroy() {
                                        Log.e("MapLocationDialog", "Map destroyed")
                                    }

                                    override fun onMapError(exception: Exception?) {
                                        Log.e("MapLocationDialog", "Map error: $exception")
                                    }
                                },
                                object : KakaoMapReadyCallback() {
                                    @SuppressLint("UseCompatLoadingForDrawables")
                                    override fun onMapReady(kakaoMap: KakaoMap) {
                                        Log.d("MapLocationDialog", "Map is ready")
                                        kakaoMapInstance = kakaoMap

                                        // 반경 정보를 로그로 표시 (실제 원 그리기는 향후 구현)
                                        fun updateRadiusInfo(position: LatLng) {
                                            Log.d("MapLocationDialog", "Selected position with ${searchRadius}km radius: ${position.latitude}, ${position.longitude}")
                                            selectedPosition = position
                                        }

                                        // 안내 메시지 표시
                                        val body = GuiLayout(Orientation.Horizontal)
                                        body.setPadding(20, 20, 20, 18)
                                        val bgImage = GuiImage(R.drawable.blue_signature, true)
                                        bgImage.setFixedArea(7, 7, 7, 7)
                                        body.setBackground(bgImage)
                                        val text = GuiText("지도를 터치해서 위치를 선택하세요")
                                        text.setTextSize(30)
                                        text.setTextColor(R.color.color5)
                                        body.addView(text)
                                        val infoOptions = InfoWindowOptions.from(centerPosition)
                                        infoOptions.setBody(body)
                                        infoOptions.setBodyOffset(0F, -4F)

                                        try {
                                            kakaoMap.mapWidgetManager?.infoWindowLayer?.addInfoWindow(infoOptions)
                                        } catch (e: Exception) {
                                            Log.e("MapLocationDialog", "Info window add error: ${e.message}")
                                        }

                                        // 초기 카메라 위치 설정
                                        try {
                                            val initialCameraUpdate = CameraUpdateFactory.newCenterPosition(centerPosition)
                                            kakaoMap.moveCamera(initialCameraUpdate)
                                            updateRadiusInfo(centerPosition)
                                        } catch (e: Exception) {
                                            Log.e("MapLocationDialog", "Initial camera setup error: ${e.message}")
                                        }
                                        

                                        // 정보창 클릭 리스너
                                        kakaoMap.setOnInfoWindowClickListener { kakaoMap, _, _ ->
                                            try {
                                                kakaoMap.mapWidgetManager?.infoWindowLayer?.removeAll()
                                            } catch (e: Exception) {
                                                Log.e("MapLocationDialog", "Info window click error: ${e.message}")
                                            }
                                        }

                                        // 지도 클릭 리스너
                                        kakaoMap.setOnMapClickListener { map, _, _, _ ->
                                            try {
                                                map.mapWidgetManager?.infoWindowLayer?.removeAll()
                                            } catch (e: Exception) {
                                                Log.e("MapLocationDialog", "Map click error: ${e.message}")
                                            }
                                        }

                                        // POI 클릭 리스너
                                        kakaoMap.setOnPoiClickListener { map, latLng, title, detail ->
                                            try {
                                                Log.d("MapLocationDialog", "POI clicked: $title at ${latLng?.latitude}, ${latLng?.longitude}")
                                                
                                                if (latLng != null && map != null) {
                                                    map.mapWidgetManager?.infoWindowLayer?.removeAll()
                                                    
                                                    val newCameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                                                    
                                                    // 선택된 위치에 마커 표시
                                                    map.labelManager?.let { labelManager ->
                                                        val style = labelManager.addLabelStyles(
                                                            LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                                                        )
                                                        if (style != null) {
                                                            val options = LabelOptions.from(latLng).setStyles(style)
                                                            val layer = labelManager.layer
                                                            layer?.removeAll()
                                                            layer?.addLabel(options)
                                                        }
                                                    }
                                                    
                                                    // 카메라 이동
                                                    map.moveCamera(newCameraUpdate)
                                                    
                                                    // 위치 업데이트
                                                    updateRadiusInfo(latLng)
                                                    
                                                    // 주소 검색
                                                    viewModel.doFindAddress(latLng.latitude, latLng.longitude)
                                                }
                                            } catch (e: Exception) {
                                                Log.e("MapLocationDialog", "POI click error: ${e.message}")
                                            }
                                            false
                                        }

                                        // 지형 롱클릭 리스너
                                        kakaoMap.setOnTerrainLongClickListener { map, latLng, point ->
                                            try {
                                                Log.d("MapLocationDialog", "Terrain long clicked at ${latLng?.latitude}, ${latLng?.longitude}")
                                                
                                                if (latLng != null && map != null) {
                                                    map.mapWidgetManager?.infoWindowLayer?.removeAll()
                                                    
                                                    val newCameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                                                    
                                                    // 선택된 위치에 마커 표시
                                                    map.labelManager?.let { labelManager ->
                                                        val style = labelManager.addLabelStyles(
                                                            LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                                                        )
                                                        if (style != null) {
                                                            val options = LabelOptions.from(latLng).setStyles(style)
                                                            val layer = labelManager.layer
                                                            layer?.removeAll()
                                                            layer?.addLabel(options)
                                                        }
                                                    }
                                                    
                                                    // 카메라 이동
                                                    map.moveCamera(newCameraUpdate)
                                                    
                                                    // 위치 업데이트
                                                    updateRadiusInfo(latLng)
                                                    
                                                    // 주소 검색
                                                    viewModel.doFindAddress(latLng.latitude, latLng.longitude)
                                                }
                                            } catch (e: Exception) {
                                                Log.e("MapLocationDialog", "Terrain long click error: ${e.message}")
                                            }
                                            false
                                        }
                                    }

                                    override fun getPosition(): LatLng {
                                        return centerPosition
                                    }
                                }
                            )
                            } catch (e: Exception) {
                                Log.e("MapLocationDialog", "Failed to start MapView: ${e.message}")
                                onDismiss()
                            }
                        }
                    }
                )

                // 반경 정보 표시
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0F7FF)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "🔍 검색 반경: ${searchRadius}km",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF4B7BFF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 선택된 주소 표시 및 확인 버튼
                if (roadAddress1.isNullOrEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (!isGpsLocationLoaded) 
                                "📍 버튼을 눌러 현재 위치를 확인하거나\n지도를 탭하여 위치를 선택하세요" 
                            else 
                                "지도를 탭하거나 길게 눌러 위치를 선택하세요",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        roadAddress1.firstOrNull()?.let { address ->
                            DisplaySelectedAddress(address)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("취소")
                            }
                            
                            Button(
                                onClick = {
                                    roadAddress1.firstOrNull()?.let { address ->
                                        val selectedAddress = address.addressName
                                        onLocationSelected(selectedAddress)
                                        // 상태 초기화
                                        try {
                                            viewModel._roadAddress1.value = emptyList()
                                        } catch (e: Exception) {
                                            Log.e("MapLocationDialog", "Clear address error: ${e.message}")
                                        }
                                        onDismiss()
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4B7BFF)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("이 위치 선택")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    address: AddressFindRoadAddress,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = address.addressName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (address.roadName.isNotEmpty()) {
                Text(
                    text = address.roadName + if (address.buildingName.isNotEmpty()) " (${address.buildingName})" else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DisplaySelectedAddress(address: Coord2RoadAddress) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F7FF)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "선택된 위치",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = address.addressName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                
                if (address.zoneNo.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${address.zoneNo})",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}