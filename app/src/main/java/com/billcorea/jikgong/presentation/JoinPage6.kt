package com.billcorea.jikgong.presentation

import android.content.Context
import android.net.Uri
import android.os.Environment
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.FileProvider
import androidx.navigation.compose.rememberNavController
import com.afollestad.materialdialogs.BuildConfig
import com.afollestad.materialdialogs.MaterialDialog
import com.billcorea.jikgong.R
import com.billcorea.jikgong.network.WorkExperience
import com.billcorea.jikgong.presentation.destinations.JikgongAppDestination
import com.billcorea.jikgong.presentation.destinations.JoinPage2Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage5Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage6Destination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun JoinPage6(
    viewModel: MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isSecretOk by remember { mutableStateOf(false) }
    val focusRequester = FocusRequester()

    // 입력 상태
    var _jobName by remember { mutableStateOf(context.getString(R.string.msgSelectJob)) }
    var _jobCode by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("") }
    var yearInput by remember { mutableStateOf("") }
    var monthInput by remember { mutableStateOf("") }
    var totalMonthInput by remember { mutableStateOf("") }
    val addedItems = remember { mutableStateListOf<Pair<String, Int>>() }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false)
    val workERList = remember { mutableStateListOf<WorkExperience>() }

    val name by remember { mutableStateOf(sp.getString("name", "").toString()) }
    val gender by remember { mutableStateOf(sp.getString("gender", "").toString()) }
    val id by remember { mutableStateOf(sp.getString("id", "").toString()) }
    val password by remember { mutableStateOf(sp.getString("password", "").toString()) }
    val email by remember { mutableStateOf(sp.getString("email", "").toString()) }
    val birthday by remember { mutableStateOf(sp.getString("birthday", "").toString()) }
    val nationality by remember { mutableStateOf(sp.getString("nationality", "").toString()) }
    val phoneNumber by remember { mutableStateOf(sp.getString("phoneNumber", "").toString()) }
    val accountName by remember { mutableStateOf(sp.getString("accountName", "").toString()) }
    val bankName by remember { mutableStateOf(sp.getString("bankName", "").toString()) }
    val accountNumber by remember { mutableStateOf(sp.getString("accountNumber", "").toString()) }
    val myLocation by remember { mutableStateOf(sp.getString("myLocation", "").toString()) }
    val lon by remember { mutableStateOf(sp.getString("lon", "").toString()) }
    val lat by remember { mutableStateOf(sp.getString("lat", "").toString()) }
    val educationCertificateImage by remember { mutableStateOf(sp.getString("safeManagerCardUri", "").toString()) }
    val workerCardImage by remember { mutableStateOf(sp.getString("workerCardCardUri", "").toString()) }
    val hasVisa by remember { mutableStateOf(sp.getString("haaVisa", "").toBoolean()) }

    val _resultMessage = viewModel.registerResult.observeAsState(initial = " ")
    val resultMessage = _resultMessage.value

    fun validationData() {
        if (textInput.isNotEmpty() && numberInput.isNotEmpty()) {
            isSecretOk = true
        } else {
            isSecretOk = false
        }
    }

    LaunchedEffect(resultMessage) {
        if (!resultMessage.isNullOrBlank()) {
            MaterialDialog(context).show {
                icon(R.drawable.ic_launcher_v1_foreground)
                // message(R.string.resister)
                message(text = resultMessage)
                positiveButton(R.string.OK) {
                    viewModel.clearRegisterResult()
                    navigator.navigate(JikgongAppDestination) }
            }
        }
        else if(resultMessage.isNullOrEmpty()) {
            Log.e("", "이메일 중복")
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }
            , sheetState = sheetState
            , modifier = Modifier.fillMaxHeight()
        ) {
            JobSelectList(
                doJobSelect = { jobCode ->
                    Log.e(",", "jobCode = $jobCode")
                    _jobName = viewModel.jobName[viewModel.jobCode.indexOf(jobCode)]
                    textInput = _jobName
                    _jobCode = jobCode
                    showBottomSheet = false
                    focusRequester.requestFocus()
                    validationData()
                },
                doClose = {
                    showBottomSheet = false
                }
            )
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        topBar = {
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
                    selectedPage = 5,
                    defaultRadius = 12.dp,
                    selectedLength = 24.dp,
                    space = 6.dp,
                    animationDurationInMillis = 1000,
                )
            }
        },
        bottomBar = {
            TextButton(
                onClick = {
                    val editor = sp.edit()
                    // editor.putString("myLocation", address)
                    // editor.apply()

                    Log.e("", "17. ${educationCertificateImage}")
                    Log.e("", "18. ${workerCardImage}")

                    viewModel.doRegisterWorker(id, password, phoneNumber, email, "ROLE_WORKER", true, "token", true, name, birthday.replace("-", ""), gender, nationality, accountName, accountNumber, bankName, "null", hasVisa, true, workERList, myLocation, lat.toDouble(), lon.toDouble(), educationCertificateImage, workerCardImage)
                    Log.e("", "1. ${id}")
                    Log.e("", "2. ${password}")
                    Log.e("", "3. ${phoneNumber}")
                    Log.e("", "4. ${email}")
                    Log.e("", "5. ${birthday.replace("-", "")}")
                    Log.e("", "6. ${gender}")
                    Log.e("", "7. ${nationality}")
                    Log.e("", "8. ${accountName}")
                    Log.e("", "9. ${accountNumber}")
                    Log.e("", "10. ${bankName}")
                    Log.e("", "11. ${hasVisa}")
                    Log.e("", "12. ${myLocation}")
                    Log.e("", "13. ${lat}")
                    Log.e("", "14. ${lon}")
                    // Log.e("", "15. ${workERList[0]}")
                    Log.e("", "16. ${name}")

                    /* MaterialDialog(context).show {
                        icon(R.drawable.ic_launcher_v1_foreground)
                        // message(R.string.resister)
                        message(text = resultMessage)
                        positiveButton(R.string.OK) { navigator.navigate(JikgongAppDestination) }
                    } */
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .background(appColorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.joinMember),
                    color = appColorScheme.onPrimary,
                    lineHeight = 1.25.em,
                    style = AppTypography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // ✅ 입력 필드 및 추가 버튼
            item {
                /* OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("직종 입력") },
                    modifier = Modifier.fillMaxWidth()
                ) */
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, appColorScheme.inverseSurface),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = _jobName,
                        color = appColorScheme.primary,
                        lineHeight = 1.25.em,
                        style = AppTypography.bodyMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    IconButton(onClick = {
                        showBottomSheet = true
                    },
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                validationData()
                            }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_keyboard_arrow_down_24dp),
                            contentDescription = "Arrow Down"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                //  경력 입력
                Text(
                    text = stringResource(R.string.testText),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
//                    text = "${totalMonthInput} ${stringResource(R.string.testText2)}" ,
                    text = stringResource(R.string.testText2) ,
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = yearInput,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                yearInput = newValue
//                                totalMonthInput = yearInput * 12
                            }
                        },
                        label = { Text("년") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = monthInput,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                monthInput = newValue
                                totalMonthInput += monthInput
                            }
                        },
                        label = { Text("월") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        if (textInput.isNotBlank() && numberInput.isNotBlank()) {
                            addedItems.add(textInput to numberInput.toInt())
                            workERList += WorkExperience(tech=_jobCode, experienceMonths=numberInput.toInt())
                            textInput = ""
                            numberInput = ""
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(appColorScheme.primary)
                ) {
                    Text(
                        text = "추가하기",
                        color = appColorScheme.onPrimary,
                        lineHeight = 1.25.em,
                        style = AppTypography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ✅ 리스트 출력
            items(addedItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "직종: ${item.first}")
                        Text(text = "경력(개월): ${item.second}")
                    }
                }
            }
        }
    }
}

@Composable
fun JobSelectList(
    doJobSelect:(jobCode : String) -> Unit,
    doClose:() -> Unit
) {
    val config = LocalConfiguration.current
    val screenWeight = config.screenWidthDp
    val screenHeight = config.screenHeightDp

    Column (modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.selectJob),
                color = appColorScheme.primary,
                lineHeight = 1.25.em,
                style = AppTypography.bodyMedium,
                modifier = Modifier.padding(3.dp)
            )
            IconButton(onClick = {
                doClose()
            }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("NORMAL")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "보통인부", style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doJobSelect("FOREMAN")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "작업반장", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("SKILLED_LABORER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "특별인부", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("HELPER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "조력공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("SCAFFOLDER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "비계공", style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doJobSelect("FORMWORK_CARPENTER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "형틀목공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("REBAR_WORKER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "철근공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("STEEL_STRUCTURE")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "철골공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("WELDER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "용접공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("CONCRETE_WORKER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "콘트리트공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("BRICKLAYER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "조적공", style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doJobSelect("DRYWALL_FINISHER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "견출공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("CONSTRUCTION_CARPENTER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "건축목공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("WINDOW_DOOR_INSTALLER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "창호공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("GLAZIER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "유리공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("WATERPROOFING_WORKER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "방수공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("PLASTERER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "미장공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("TILE")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "타일공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("PAINTER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "도장공", style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doJobSelect("INTERIOR_FINISHER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "내장공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("WALLPAPER_INSTALLER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "도배공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("POLISHER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "연마공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("STONEMASON")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "석공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("GROUT_WORKER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "줄눈공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("PANEL_ASSEMBLER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "판넬조립공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("ROOFER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "지붕잇기공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("LANDSCAPER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "조경공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("CAULKER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "코킹공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("PLUMBER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "배관공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("BOILER_TECHNICIAN")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "보일러공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("SANITARY_TECHNICIAN")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "위생공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("DUCT_INSTALLER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "덕트공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("INSULATION_WORKER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "보온공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("MECHANICAL_EQUIPMENT_TECHNICIAN")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "기계설비공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("ELECTRICIAN")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "내선진공", style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doJobSelect("TELECOMMUNICATIONS_INSTALLER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "통신내선공", style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doJobSelect("TELECOMMUNICATIONS_EQUIPMENT_INSTALLER")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text= "통신설비공", style = AppTypography.bodyMedium)
                }
            }
        }
    }
}

@Preview
@Composable
fun JoinPage6Preview() {
    val fakeViewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // 8.dp 간격으로 설정
            ) {
                Text(text = "[ 이름여섯글자 ]")
                Text(text = "30년 10개월 ( 370 개월 )")
                Spacer(modifier = Modifier.weight(1f)) // 남은 공간을 차지
                Text(text = "X")
            }
        }
    }
}