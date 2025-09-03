package com.billcorea.jikgong.presentation.company.main.scout.components

import android.annotation.SuppressLint
import android.util.Log
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
import com.billcorea.jikgong.api.models.location.Coord2RoadAddress
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
    val mapView = remember { MapView(context) }
    var selectedPosition by remember { mutableStateOf<LatLng?>(null) }
    
    val _roadAddress1 = viewModel.roadAddress1.observeAsState(emptyList())
    val roadAddress1 = _roadAddress1.value
    
    // 서울 중심으로 초기 위치 설정
    val centerPosition = remember { LatLng.from(37.5665, 126.9780) }

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
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기"
                        )
                    }
                }

                // 지도 뷰
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * 0.6).dp)
                        .padding(horizontal = 16.dp),
                    factory = { context ->
                        mapView.apply {
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

                                        kakaoMap.mapWidgetManager?.infoWindowLayer?.addInfoWindow(infoOptions)

                                        // 카메라를 중심 위치로 이동
                                        val cameraUpdate = CameraUpdateFactory.newCenterPosition(centerPosition)
                                        kakaoMap.moveCamera(cameraUpdate)
                                        
                                        // 초기 위치 설정
                                        updateRadiusInfo(centerPosition)

                                        // 정보창 클릭 리스너
                                        kakaoMap.setOnInfoWindowClickListener { kakaoMap, _, _ ->
                                            kakaoMap.mapWidgetManager?.infoWindowLayer?.removeAll()
                                        }

                                        // 지도 클릭 리스너
                                        kakaoMap.setOnMapClickListener { map, _, _, _ ->
                                            map.mapWidgetManager?.infoWindowLayer?.removeAll()
                                        }

                                        // POI 클릭 리스너
                                        kakaoMap.setOnPoiClickListener { map, latLng, title, detail ->
                                            Log.d("MapLocationDialog", "POI clicked: $title at ${latLng?.latitude}, ${latLng?.longitude}")
                                            
                                            if (latLng != null && map != null) {
                                                map.mapWidgetManager?.infoWindowLayer?.removeAll()
                                                
                                                val newCameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                                                
                                                // 선택된 위치에 마커 표시
                                                val style = map.labelManager?.addLabelStyles(
                                                    LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                                                )
                                                val options = LabelOptions.from(latLng).setStyles(style)
                                                val layer = map.labelManager?.layer
                                                layer?.removeAll()
                                                layer?.addLabel(options)
                                                
                                                // 카메라 이동
                                                map.moveCamera(newCameraUpdate)
                                                
                                                // 위치 업데이트
                                                updateRadiusInfo(latLng)
                                                
                                                // 주소 검색
                                                viewModel.doFindAddress(latLng.latitude, latLng.longitude)
                                            }
                                        }

                                        // 지형 롱클릭 리스너
                                        kakaoMap.setOnTerrainLongClickListener { map, latLng, point ->
                                            Log.d("MapLocationDialog", "Terrain long clicked at ${latLng?.latitude}, ${latLng?.longitude}")
                                            
                                            if (latLng != null && map != null) {
                                                map.mapWidgetManager?.infoWindowLayer?.removeAll()
                                                
                                                val newCameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                                                
                                                // 선택된 위치에 마커 표시
                                                val style = map.labelManager?.addLabelStyles(
                                                    LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                                                )
                                                val options = LabelOptions.from(latLng).setStyles(style)
                                                val layer = map.labelManager?.layer
                                                layer?.removeAll()
                                                layer?.addLabel(options)
                                                
                                                // 카메라 이동
                                                map.moveCamera(newCameraUpdate)
                                                
                                                // 위치 업데이트
                                                updateRadiusInfo(latLng)
                                                
                                                // 주소 검색
                                                viewModel.doFindAddress(latLng.latitude, latLng.longitude)
                                            }
                                        }
                                    }

                                    override fun getPosition(): LatLng {
                                        return centerPosition
                                    }
                                }
                            )
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
                if (roadAddress1.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "지도를 탭하거나 길게 눌러 위치를 선택하세요",
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
                        DisplaySelectedAddress(roadAddress1[0])
                        
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
                                    val selectedAddress = roadAddress1[0].addressName
                                    onLocationSelected(selectedAddress)
                                    // 상태 초기화
                                    viewModel._roadAddress1.value = emptyList()
                                    onDismiss()
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