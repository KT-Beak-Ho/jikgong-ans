package com.billcorea.jikgong.presentation.company.main.scout.presentation.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billcorea.jikgong.R
import com.billcorea.jikgong.api.models.location.AddressFindRoadAddress
import com.billcorea.jikgong.api.models.location.Coord2RoadAddress
import com.billcorea.jikgong.presentation.company.main.scout.presentation.viewmodel.MapLocationViewModel
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
fun MapLocationDialogOptimized(
    onDismiss: () -> Unit,
    onLocationSelected: (String) -> Unit,
    mainViewModel: MainViewModel,
    searchRadius: Int = 10
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    
    val viewModel = remember { MapLocationViewModel(mainViewModel) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var kakaoMapInstance by remember { mutableStateOf<KakaoMap?>(null) }
    
    DisposableEffect(context) {
        try {
            mapView = MapView(context)
            Log.d("MapLocationDialog", "MapView created successfully")
        } catch (e: Exception) {
            Log.e("MapLocationDialog", "Failed to create MapView: ${e.message}")
            onDismiss()
        }
        
        onDispose {
            try {
                mapView?.finish()
                mapView = null
                kakaoMapInstance = null
                Log.d("MapLocationDialog", "MapView disposed successfully")
            } catch (e: Exception) {
                Log.e("MapLocationDialog", "Error disposing MapView: ${e.message}")
            }
        }
    }
    
    DisposableEffect(uiState.isGpsLocationLoaded, uiState.currentLat, uiState.currentLon, kakaoMapInstance) {
        if (uiState.isGpsLocationLoaded && kakaoMapInstance != null && 
            uiState.currentLat != 0.0 && uiState.currentLon != 0.0) {
            
            try {
                val gpsPosition = LatLng.from(uiState.currentLat, uiState.currentLon)
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(gpsPosition)
                kakaoMapInstance?.moveCamera(cameraUpdate)
                
                kakaoMapInstance?.labelManager?.let { labelManager ->
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
                
                viewModel.selectMapPosition(gpsPosition)
            } catch (e: Exception) {
                Log.e("MapLocationDialog", "GPS location update error: ${e.message}")
            }
        }
        
        onDispose { }
    }
    
    if (mapView == null) {
        return
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
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
                MapLocationHeader(
                    onDismiss = onDismiss,
                    onGpsClick = { viewModel.loadCurrentGpsLocation(context) }
                )
                
                MapLocationSearchBar(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = viewModel::updateSearchQuery,
                    onSearch = viewModel::searchAddress
                )
                
                if (uiState.searchResults.isNotEmpty() && uiState.searchQuery.isNotEmpty()) {
                    MapLocationSearchResults(
                        searchResults = uiState.searchResults,
                        onResultClick = { address ->
                            viewModel.selectSearchResult(address)
                            kakaoMapInstance?.let { map ->
                                moveMapToAddress(map, address)
                            }
                        }
                    )
                }
                
                MapLocationMapView(
                    mapView = mapView!!,
                    screenHeight = screenHeight,
                    centerPosition = viewModel.getCenterPosition(),
                    onMapReady = { map ->
                        kakaoMapInstance = map
                        viewModel.setKakaoMapInstance(map)
                        setupMapListeners(map, viewModel)
                    }
                )
                
                MapLocationRadiusInfo(searchRadius = searchRadius)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                MapLocationBottomSection(
                    selectedAddress = uiState.selectedAddress,
                    isGpsLocationLoaded = uiState.isGpsLocationLoaded,
                    onCancel = onDismiss,
                    onConfirm = {
                        uiState.selectedAddress?.let { address ->
                            onLocationSelected(address.addressName)
                            viewModel.clearSelectedAddress()
                            onDismiss()
                        }
                    }
                )
                
                uiState.error?.let { error ->
                    Snackbar(
                        modifier = Modifier.padding(16.dp),
                        action = {
                            TextButton(onClick = viewModel::clearError) {
                                Text("닫기")
                            }
                        }
                    ) {
                        Text(error)
                    }
                }
            }
        }
    }
}

@Composable
private fun MapLocationHeader(
    onDismiss: () -> Unit,
    onGpsClick: () -> Unit
) {
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
            IconButton(onClick = onGpsClick) {
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
}

@Composable
private fun MapLocationSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
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
                    onSearch(searchQuery.trim())
                }
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedBorderColor = Color(0xFF4B7BFF)
        ),
        singleLine = true
    )
}

@Composable
private fun MapLocationSearchResults(
    searchResults: List<AddressFindRoadAddress>,
    onResultClick: (AddressFindRoadAddress) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(searchResults) { address ->
                SearchResultItem(
                    address = address,
                    onClick = { onResultClick(address) }
                )
            }
        }
    }
}

@Composable
private fun MapLocationMapView(
    mapView: MapView,
    screenHeight: Int,
    centerPosition: LatLng,
    onMapReady: (KakaoMap) -> Unit
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height((screenHeight * 0.5).dp)
            .padding(horizontal = 16.dp),
        factory = { _ ->
            mapView.apply {
                start(
                    object : MapLifeCycleCallback() {
                        override fun onMapDestroy() {
                            Log.d("MapLocationDialog", "Map destroyed")
                        }

                        override fun onMapError(exception: Exception?) {
                            Log.e("MapLocationDialog", "Map error: $exception")
                        }
                    },
                    object : KakaoMapReadyCallback() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        override fun onMapReady(kakaoMap: KakaoMap) {
                            Log.d("MapLocationDialog", "Map is ready")
                            onMapReady(kakaoMap)
                            setupInitialMapState(kakaoMap, centerPosition)
                        }

                        override fun getPosition(): LatLng = centerPosition
                    }
                )
            }
        }
    )
}

@Composable
private fun MapLocationRadiusInfo(searchRadius: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
}

@Composable
private fun MapLocationBottomSection(
    selectedAddress: Coord2RoadAddress?,
    isGpsLocationLoaded: Boolean,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    if (selectedAddress == null) {
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
            DisplaySelectedAddress(selectedAddress)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("취소")
                }
                
                Button(
                    onClick = onConfirm,
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

private fun setupInitialMapState(kakaoMap: KakaoMap, centerPosition: LatLng) {
    try {
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
        
        val initialCameraUpdate = CameraUpdateFactory.newCenterPosition(centerPosition)
        kakaoMap.moveCamera(initialCameraUpdate)
    } catch (e: Exception) {
        Log.e("MapLocationDialog", "Initial map setup error: ${e.message}")
    }
}

private fun setupMapListeners(kakaoMap: KakaoMap, viewModel: MapLocationViewModel) {
    kakaoMap.setOnInfoWindowClickListener { map, _, _ ->
        try {
            map.mapWidgetManager?.infoWindowLayer?.removeAll()
        } catch (e: Exception) {
            Log.e("MapLocationDialog", "Info window click error: ${e.message}")
        }
    }
    
    kakaoMap.setOnMapClickListener { map, _, _, _ ->
        try {
            map.mapWidgetManager?.infoWindowLayer?.removeAll()
        } catch (e: Exception) {
            Log.e("MapLocationDialog", "Map click error: ${e.message}")
        }
    }
    
    kakaoMap.setOnPoiClickListener { map, latLng, title, _ ->
        handleMapPositionSelection(map, latLng, viewModel, "POI: $title")
        false
    }
    
    kakaoMap.setOnTerrainLongClickListener { map, latLng, _ ->
        handleMapPositionSelection(map, latLng, viewModel, "Terrain")
        false
    }
}

private fun handleMapPositionSelection(
    map: KakaoMap?,
    latLng: LatLng?,
    viewModel: MapLocationViewModel,
    source: String
) {
    try {
        if (latLng != null && map != null) {
            Log.d("MapLocationDialog", "$source selected at ${latLng.latitude}, ${latLng.longitude}")
            
            map.mapWidgetManager?.infoWindowLayer?.removeAll()
            
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
            
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
            
            map.moveCamera(cameraUpdate)
            viewModel.selectMapPosition(latLng)
        }
    } catch (e: Exception) {
        Log.e("MapLocationDialog", "$source selection error: ${e.message}")
    }
}

private fun moveMapToAddress(kakaoMap: KakaoMap, address: AddressFindRoadAddress) {
    try {
        val lat = address.y.toDoubleOrNull()
        val lon = address.x.toDoubleOrNull()
        
        if (lat != null && lon != null) {
            val position = LatLng.from(lat, lon)
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
            kakaoMap.moveCamera(cameraUpdate)
            
            kakaoMap.labelManager?.let { labelManager ->
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
        }
    } catch (e: Exception) {
        Log.e("MapLocationDialog", "Move map to address error: ${e.message}")
    }
}