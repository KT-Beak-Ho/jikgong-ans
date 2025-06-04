package com.billcorea.jikgong.presentation

import android.annotation.SuppressLint
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.network.Coord2RoadAddress
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.Poi
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle
import com.kakao.vectormap.label.LodLabel
import com.kakao.vectormap.label.LodLabelLayer
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiImage
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@Destination
@Composable
fun KakaoMapView(
    viewModel: MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val mapView = remember { MapView(context) }
    var clickLabelName by remember { mutableStateOf("") }
    val _lat = viewModel.lat.observeAsState()
    val _lon = viewModel.lon.observeAsState()
    val xPos by remember { mutableStateOf(_lon.value) }
    val yPos by remember { mutableStateOf(_lat.value) }
    lateinit var centerPosition: LatLng

    val _roadAddress1 = viewModel.roadAddress1.observeAsState(emptyList())
    val roadAddress1 = _roadAddress1.value

    centerPosition = LatLng.from(37.5665, 126.9780)
    if (xPos != 0.0 && yPos != 0.0) {
        //centerPosition = LatLng.from(yPos!!, xPos!!)
    }
    try {

    } catch (e : Exception) {

    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp), topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        Log.e("", "backArrow")
                        navigator.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = stringResource(R.string.findedAddress),
                        color = appColorScheme.primary,
                        lineHeight = 1.33.em,
                        style = AppTypography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )

                }
            }, bottomBar = {

            }
        ) { innerPadding ->

            Column(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()) {

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((screenHeight * .8).dp), // AndroidView의 높이 임의 설정
                    factory = { context ->
                        mapView.apply {
                            mapView.start(
                                object : MapLifeCycleCallback() {
                                    // 지도 생명 주기 콜백: 지도가 파괴될 때 호출
                                    override fun onMapDestroy() {
                                        // 필자가 직접 만든 Toast생성 함수
                                        //makeToast(context = context, message = "지도를 불러오는데 실패했습니다.")
                                    }

                                    // 지도 생명 주기 콜백: 지도 로딩 중 에러가 발생했을 때 호출
                                    override fun onMapError(exception: Exception?) {
                                        // 필자가 직접 만든 Toast생성 함수
                                        //makeToast(context = context, message = "지도를 불러오는 중 알 수 없는 에러가 발생했습니다.\n onMapError: $exception")
                                    }
                                },
                                object : KakaoMapReadyCallback() {
                                    // KakaoMap이 준비되었을 때 호출
                                    @SuppressLint("UseCompatLoadingForDrawables")
                                    override fun onMapReady(kakaoMap: KakaoMap) {

                                        val body = GuiLayout(Orientation.Horizontal)
                                        body.setPadding(20, 20, 20, 18)
                                        val bgImage = GuiImage(R.drawable.blue_signature, true)
                                        bgImage.setFixedArea(7, 7, 7, 7)
                                        body.setBackground(bgImage)
                                        val text = GuiText("지도을 움직여 대표위치를 설정 하세요.")
                                        text.setTextSize(30)
                                        text.setTextColor(R.color.color5)
                                        body.addView(text)
                                        val infoOptions = InfoWindowOptions.from(centerPosition)
                                        infoOptions.setBody(body)
                                        infoOptions.setBodyOffset(0F, -4F)

                                        Log.e("", "center ${centerPosition.latitude} ${centerPosition.longitude} 1")
                                        kakaoMap.mapWidgetManager?.infoWindowLayer?.addInfoWindow(infoOptions)

                                        // 카메라를 (locationY, locationX) 위치로 이동시키는 업데이트 생성
                                        var cameraUpdate = CameraUpdateFactory.newCenterPosition(centerPosition)
                                        // 카메라를 지정된 위치로 이동
                                        kakaoMap.moveCamera(cameraUpdate)

                                        Log.e("", "center ${centerPosition.latitude} ${centerPosition.longitude}")
//                                        // 지도에 표시할 라벨의 스타일 설정
//                                        val style = LabelStyle.from(R.drawable.ic_mylocation_v2)
//                                        // 라벨 옵션을 설정하고 위치와 스타일을 적용
//                                        val options = LabelOptions.from(centerPosition).setStyles(style)
//                                        // KakaoMap의 labelManager에서 레이어를 가져옴
//                                        val layer = kakaoMap.labelManager?.layer
//                                        layer?.addLabel(options)

                                        kakaoMap.setOnInfoWindowClickListener { kakaoMap, infoWindow, s ->
                                            kakaoMap.mapWidgetManager?.infoWindowLayer?.removeAll()
                                        }
                                        kakaoMap.setOnMapClickListener { map, _, _, _ ->
                                            map.mapWidgetManager?.infoWindowLayer?.removeAll()
                                        }
                                        kakaoMap.setOnLodLabelClickListener { _, _, lodLabel ->
                                            lodLabel?.texts?.forEach { it ->
                                                Log.e("", "lodLabel $it, ${it.length}")
                                                clickLabelName = it
                                            }
                                            false;
                                        }
                                        kakaoMap.setOnPoiClickListener { map, latLng, title, detail ->
                                            Log.e(
                                                "",
                                                "PoiClick ${latLng?.latitude} ${latLng?.longitude} $title, $detail"
                                            )
                                            map?.mapWidgetManager?.infoWindowLayer?.removeAll()
                                            cameraUpdate =
                                                CameraUpdateFactory.newCenterPosition(latLng)
                                            // 지도에 표시할 라벨의 스타일 설정
                                            val style = map?.labelManager?.addLabelStyles(
                                                LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                                            )
                                            Log.e(
                                                "",
                                                "center ${centerPosition.latitude} ${centerPosition.longitude}"
                                            )
                                            // 라벨 옵션을 설정하고 위치와 스타일을 적용
                                            val options = LabelOptions.from(latLng).setStyles(style)
                                            // KakaoMap의 labelManager에서 레이어를 가져옴
                                            val layer = map?.labelManager?.layer
                                            layer?.removeAll()
                                            layer?.addLabel(options)
                                            // 카메라를 지정된 위치로 이동
                                            map?.moveCamera(cameraUpdate)
                                            if (latLng != null) {
                                                viewModel.doFindAddress(
                                                    latLng.latitude,
                                                    latLng.longitude
                                                )
                                            }
                                        }
                                        kakaoMap.setOnTerrainLongClickListener { map, latLng, point ->
                                            Log.e(
                                                "",
                                                "longClick ${latLng?.latitude} ${latLng?.longitude} ${point?.y} ${point?.x}"
                                            )
                                            map?.mapWidgetManager?.infoWindowLayer?.removeAll()
                                            cameraUpdate =
                                                CameraUpdateFactory.newCenterPosition(latLng)
                                            // 지도에 표시할 라벨의 스타일 설정
                                            val style = map?.labelManager?.addLabelStyles(
                                                LabelStyles.from(LabelStyle.from(R.drawable.ic_mylocation_v2))
                                            )
                                            Log.e(
                                                "",
                                                "center ${centerPosition.latitude} ${centerPosition.longitude}"
                                            )
                                            // 라벨 옵션을 설정하고 위치와 스타일을 적용
                                            val options = LabelOptions.from(latLng).setStyles(style)
                                            // KakaoMap의 labelManager에서 레이어를 가져옴
                                            val layer = map?.labelManager?.layer
                                            layer?.removeAll()
                                            layer?.addLabel(options)
                                            // 카메라를 지정된 위치로 이동
                                            map?.moveCamera(cameraUpdate)
                                            if (latLng != null) {
                                                viewModel.doFindAddress(
                                                    latLng.latitude,
                                                    latLng.longitude
                                                )
                                            }
                                        }
                                    }

                                    override fun getPosition(): LatLng {
                                        // 현재 위치를 반환
                                        return centerPosition
                                    }
                                },
                            )
                        }
                    },
                )
                if (roadAddress1.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.moveCenterSelect),
                            style = AppTypography.bodyMedium.copy(
                                appColorScheme.primary
                            )
                        )

                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DisplayRoadAddress(roadAddress1[0])
                        TextButton(
                            onClick = {
                                viewModel._respAddress.value = roadAddress1[0].addressName
                                viewModel._roadAddress1.value = emptyList()
                                navigator.navigateUp()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .background(appColorScheme.onPrimaryContainer)
                        ) {
                            Text(
                                text = stringResource(R.string.selectThisPosition),
                                style = AppTypography.bodyMedium.copy(
                                    appColorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayRoadAddress(item: Coord2RoadAddress) {
    Log.e("", "display ${item.addressName}/${item.zoneNo}")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.addressName.isNotEmpty()) {
            Text(text = item.addressName, style = AppTypography.bodyMedium)
        }
        if (item.zoneNo.isNotEmpty()) {
            Text(text = String.format("(%s)", item.zoneNo), style = AppTypography.bodyMedium)
        }
    }
}


@Preview
@Composable
fun JikgongAPreview() {
    val viewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        KakaoMapView(viewModel, navigator)
    }
}


