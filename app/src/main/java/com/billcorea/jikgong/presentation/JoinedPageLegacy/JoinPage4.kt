package com.billcorea.jikgong.presentation.JoinedPageLegacy

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.MainActivity
import com.billcorea.jikgong.R
import com.billcorea.jikgong.network.location.AddressFindRoadAddress
import com.billcorea.jikgong.presentation.PageIndicator
import com.billcorea.jikgong.presentation.destinations.JoinPage5Destination
import com.billcorea.jikgong.presentation.destinations.KakaoMapViewDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel


@Destination
@Composable
fun JoinPage4(
    viewModel: MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    mainActivity: MainActivity? = null
) {

    var lat by remember { mutableStateOf("") }
    var lon by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    var isSecretOk by remember { mutableStateOf(false) }
    val _address = viewModel.respAddress.observeAsState()
    val address = _address.value
    val _geoCoding = viewModel.geoCoding.observeAsState()
    val geoCoding = _geoCoding.value
    val _roadAddress = viewModel.roadAddress.observeAsState(emptyList())
    val roadAddress = _roadAddress.value
    val name by remember { mutableStateOf("") }
    var _name by remember { mutableStateOf(name) }

    // roadAddress가 null이거나 빈 값일 때 에러 처리
    LaunchedEffect(roadAddress) {
        try {
            // roadAddress에 null 값이 포함되어 있는지 확인
            if (roadAddress.any { it == null }) {
                errorMessage = "주소 검색 중 오류가 발생했습니다. 다시 시도해주세요."
                showErrorDialog = true
                // null 값들을 필터링하여 안전한 리스트로 만듦
                viewModel._roadAddress.value = roadAddress.filterNotNull()
            }
        } catch (e: Exception) {
            Log.e("JoinPage4", "Error processing roadAddress: ${e.message}")
            errorMessage = "주소 처리 중 오류가 발생했습니다."
            showErrorDialog = true
        }
    }

    // 에러 다이얼로그
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "주소 검색 오류",
                    style = AppTypography.titleMedium,
                    color = appColorScheme.error
                )
            },
            text = {
                Text(
                    text = "주소 검색 중 오류가 발생했습니다. 다시 시도해주세요.",
                    style = AppTypography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showErrorDialog = false
                        // 에러 후 초기화
                        viewModel._roadAddress.value = emptyList()
                        _name = ""
                    }
                ) {
                    Text(
                        text = "확인",
                        color = appColorScheme.primary
                    )
                }
            },
            /* dismissButton = {
                TextButton(
                    onClick = {
                        showErrorDialog = false
                        // 다시 검색하기
                        if (_name.isNotEmpty()) {
                            viewModel.doKakaoGeocoding(_name)
                        }
                    }
                ) {
                    Text(
                        text = "다시 검색",
                        color = appColorScheme.secondary
                    )
                }
            } */
        )
    }

    //gps 을 통한 위치 확인시 시간이 소요 되어 미리 해 둠.
    if (address?.isEmpty() == true) {
        viewModel.setLocation(context)
    } else {
        isSecretOk = true
    }

    val getPostNo =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data.let { data ->
                val getAddress = data?.getStringExtra("data")
                Log.e("looaf", "data=$getAddress")
                if (getAddress != null) {
                    viewModel.doKakaoGeocoding(getAddress)
                    viewModel._respAddress.value = getAddress
                }
            }
        }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp), topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    Log.e("", "backArrow")
                    viewModel._respAddress.value = ""
                    viewModel._roadAddress.value = emptyList()
                    navigator.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arrow Back"
                    )
                }
                PageIndicator(
                    numberOfPages = 6,
                    selectedPage = 3,
                    defaultRadius = 12.dp,
                    selectedLength = 24.dp,
                    space = 6.dp,
                    animationDurationInMillis = 1000,
                )
            }
        }, bottomBar = {
            TextButton(
                onClick = {
                    val editor = sp.edit()
                    editor.putString("myLocation", address)
                    editor.putString("lon", lon)
                    editor.putString("lat", lat)
                    Log.e("", "${lat} ${lon}")
                    editor.apply()
                    viewModel._respAddress.value = ""
                    viewModel._roadAddress.value = emptyList()
                    _name = ""
                    if (isSecretOk) {
                        navigator.navigate(JoinPage5Destination)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .background(if (isSecretOk) appColorScheme.primary else appColorScheme.inversePrimary)
            ) {
                Text(
                    text = stringResource(R.string.next),
                    color = if (isSecretOk) appColorScheme.onPrimary else appColorScheme.surfaceDim,
                    lineHeight = 1.25.em,
                    style = AppTypography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(modifier = modifier.padding(innerPadding)) {
            item {
                Text(
                    text = stringResource(R.string.enterMainLocation),
                    color = appColorScheme.primary,
                    lineHeight = 1.33.em,
                    style = AppTypography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (address?.isEmpty() != true) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, appColorScheme.outline)
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (address?.isEmpty() == true) stringResource(R.string.msgSearchLocation) else address.toString(),
                                color = appColorScheme.primary,
                                lineHeight = 1.25.em,
                                style = AppTypography.bodyMedium,
                            )
                            IconButton(onClick = {
//                        val intent = Intent(context, AddressFindActivity::class.java)
//                        getPostNo.launch(intent)
                                viewModel._respAddress.value = ""
                                viewModel._roadAddress.value = emptyList()
                                _name = ""

                            }) {
                                Image(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Search Location"
                                )
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = _name,
                            onValueChange = {
                                _name = it
                            },
                            placeholder = {
                                Text(text = stringResource(R.string.msgSearchLocation))
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onNext = {

                            }),
                            maxLines = 1,
                            trailingIcon = {
                                IconButton(onClick = {
//                        val intent = Intent(context, AddressFindActivity::class.java)
//                        getPostNo.launch(intent)
                                    if (_name != "") {
                                        try {
                                            viewModel.doKakaoGeocoding(_name)
                                        } catch (e: Exception) {
                                            Log.e("JoinPage4", "Geocoding error: ${e.message}")
                                            errorMessage = "주소 검색 중 오류가 발생했습니다."
                                            showErrorDialog = true
                                        }
                                    }
                                }) {
                                    Image(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Location"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

            item {
                IconButton(
                    onClick = {
                        navigator.navigate(KakaoMapViewDestination)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .border(1.dp, appColorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My Location"
                        )
                        Spacer(Modifier.padding(3.dp))
                        Text(
                            text = stringResource(R.string.selectMylocation),
                            style = AppTypography.bodyMedium
                        )
                    }
                }
            }
            if (address?.isEmpty() == true) {
                // null 체크를 추가하여 안전하게 처리
                val safeRoadAddress = roadAddress.filterNotNull()
                itemsIndexed(safeRoadAddress) { index, item ->
                    DisplayAddress(item, doSetCenterPosition = {
                        viewModel._respAddress.value = item.addressName
                        viewModel._geoCoding.value = "${item.y},${item.x}"
                        lat = item.x
                        lon = item.y
                        Log.e("", "geoCoding=${viewModel._geoCoding.value}")
                    })
                }
            }
        }
    }
}

@Composable
fun DisplayAddress(
    item: AddressFindRoadAddress,
    doSetCenterPosition: (item: AddressFindRoadAddress) -> Unit = {}
) {
    TextButton(
        onClick = {
            doSetCenterPosition(item)
        }, modifier = Modifier
            .padding(3.dp)
            .border(1.dp, appColorScheme.outline)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.addressName,
                lineHeight = 1.25.em,
                style = AppTypography.titleMedium,
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = item.roadName,
                    style = AppTypography.bodyMedium,
                )
                if (item.buildingName.isNotEmpty()) {
                    Spacer(modifier = Modifier.padding(3.dp))
                    Text(
                        text = String.format("(%s)", item.buildingName),
                        style = AppTypography.bodyMedium,
                    )
                }
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = item.zoneNo,
                    style = AppTypography.bodyLarge,
                )
            }
        }
    }
}

@Preview
@Composable
fun JoinPage4Preview() {
    val fakeViewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        JoinPage4(fakeViewModel, navigator, modifier = Modifier.padding(3.dp), mainActivity = null)
    }
}





