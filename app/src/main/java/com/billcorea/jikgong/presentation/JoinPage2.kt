package com.billcorea.jikgong.presentation

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.compose.rememberNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.billcorea.jikgong.R
import com.billcorea.jikgong.datepicker.WheelDatePicker
import com.billcorea.jikgong.network.VisaExpiryDateRequest
import com.billcorea.jikgong.presentation.destinations.JoinPage3Destination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.holix.android.bottomsheetdialog.compose.BottomSheetBehaviorProperties
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialog
import com.holix.android.bottomsheetdialog.compose.BottomSheetDialogProperties
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun JoinPage2 (
    viewModel : MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val config = LocalConfiguration.current
    val screenWeight = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val phoneNumber by remember { mutableStateOf(sp.getString("phoneNumber", "").toString()) }
    val name by remember { mutableStateOf("") }
    var _name by remember { mutableStateOf(name) }
    val id by remember { mutableStateOf("") }
    var _id by remember { mutableStateOf(id) }
    val password by remember { mutableStateOf("") }
    var _password by remember { mutableStateOf(password) }
    val passwordCheck by remember { mutableStateOf("") }
    var _passwordCheck by remember { mutableStateOf(passwordCheck) }
    val email by remember { mutableStateOf("") }
    var _email by remember { mutableStateOf(name) }
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
    val birthday by remember { mutableStateOf(sdf.format(System.currentTimeMillis())) }
    var _birthday by remember { mutableStateOf(birthday) }
    val nationality by remember { mutableStateOf("") }
    var _nationality by remember { mutableStateOf(nationality) }
    val passportNo by remember { mutableStateOf("") }
    var _passportNo by remember { mutableStateOf(passportNo) }
    val gender by remember { mutableStateOf("") }
    var _gender by remember { mutableStateOf(gender) }
    var isSecurity by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false)
    var showBottomSheet by remember { mutableStateOf(false) }
    var isSecretOk by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val idFocusRequester = FocusRequester()
    val passwordFocusRequester = FocusRequester()
    val passwordCheckFocusRequester = FocusRequester()
    val emailFocusRequester = FocusRequester()
    val _expiryDate = viewModel.expiryMessage.observeAsState()
    val expiryDate = _expiryDate.value
    val _loginVal = viewModel.isLoginValidation.observeAsState()
    var loginVal = _loginVal.value
    var isCheck by remember { mutableStateOf(false) }


    fun doCheckPassportNumber() {
        viewModel.doVisaExpiryDate(VisaExpiryDateRequest("", "", "", passportNo))
        if (expiryDate?.isNotEmpty() == true) {
            MaterialDialog(context).show {
                icon(R.drawable.ic_launcher_v1_foreground)
                message(text = expiryDate)
                positiveButton(R.string.OK) { it.dismiss() }
            }
        }
    }

  Log.e("", "${_password} == ${_passwordCheck}")
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    val idRegex = "^[A-Za-z0-9]*$".toRegex()
    if (_name.isEmpty() || _birthday.isEmpty() || _nationality.isEmpty() || _gender.isEmpty() || _id.isEmpty() || _password.isEmpty() || _email.isEmpty() || !isCheck || (_password != _passwordCheck)) {
        isSecretOk = false
    }
    else if(!_email.matches(emailRegex) || !_id.matches((idRegex))) {
        isSecretOk = false
    }
    else {
        isSecretOk = true
    }

    fun validationData() {
        Log.e("1", "${_password} == ${_passwordCheck}")
        /* val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        val idRegex = "^[A-Za-z0-9]*$".toRegex()
        if (_name.isEmpty() || _birthday.isEmpty() || _nationality.isEmpty() || _gender.isEmpty() || _id.isEmpty() || _password.isEmpty() || _email.isEmpty() || !isCheck || (_password != _passwordCheck)) {
            isSecretOk = false
        }
        else if(!_email.matches(emailRegex) || !_id.matches((idRegex))) {
            isSecretOk = false
        }
        else {
            isSecretOk = true
        } */
    }

    // 뒤로가기 버튼 처리
    BackHandler {
        // 뒤로가기 시 상태 초기화
        _id = ""
        isSecurity = false
        showBottomSheet = false
        isSecretOk = false
        isCheck = false

        // ViewModel 상태 초기화 (필요한 경우)
        viewModel.clearRegisterResult() // 또는 다른 초기화 메서드가 있다면 호출

        // 실제 뒤로가기 실행
        navigator.navigateUp()
    }

    LaunchedEffect(loginVal) {
        loginVal?.let {
            if (it.indexOf("false") >= 0) {
                isCheck = false
                idFocusRequester.requestFocus()
                MaterialDialog(context).show {
                    icon(R.drawable.ic_jikgong_white)
                    message(R.string.idIsNotValid)
                    positiveButton(R.string.OK) { it.dismiss() }
                }
            } else if (it.indexOf("true") >= 0) {
                isCheck = true
                passwordFocusRequester.requestFocus()
                MaterialDialog(context).show {
                    icon(R.drawable.ic_jikgong_white)
                    message(R.string.idIsValid)
                    positiveButton(R.string.OK) { it.dismiss() }
                }
            }
        }
    }

    LaunchedEffect(_id) {
        _id.let {
            isCheck = false
            validationData()
        }
    }

    Scaffold (
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
        , topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    Log.e("", "backArrow")
                    navigator.navigateUp()
                    val editor = sp.edit()
                    editor.remove("phoneNumber")
                    editor.apply()
                    viewModel.clearRegisterResult()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arrow Back"
                    )
                }
                PageIndicator(
                    numberOfPages = 6,
                    selectedPage = 1,
                    defaultRadius = 12.dp,
                    selectedLength = 24.dp,
                    space = 6.dp,
                    animationDurationInMillis = 1000,
                )
            }
        }
        , bottomBar = {
            TextButton(
                onClick = {
                    if (isSecretOk) {
                        val editor = sp.edit()
                        editor.putString("name", _name)
                        editor.putString("id", _id)
                        Log.e("", "1. ${id} ${_id}")
                        editor.putString("password", _password)
                        editor.putString("email", _email)
                        editor.putString("gender", _gender)
                        editor.putString("birthday", _birthday)
                        editor.putString("nationality", _nationality)
                        if(passportNo == "" && nationality == "FOREIGN") {
                            editor.putBoolean("hasVisa", false)
                        }
                        else {
                            editor.putBoolean("hasVisa", true)
                        }
                        editor.apply()
                        _id = ""
                        isSecurity = false
                        isSecretOk = false
                        isCheck = false

                        viewModel.clearRegisterResult()

                        navigator.navigate(JoinPage3Destination)
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

        LazyColumn (modifier = modifier.padding(innerPadding)){

            item {
                Text(
                    text = stringResource(R.string.enterYourInfo),
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
                Text(
                    text = stringResource(R.string.telnumber),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(appColorScheme.surfaceDim)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text=phoneNumber.toString(), style = AppTypography.titleMedium.copy(appColorScheme.secondary))
                    TextButton(onClick = {
                        val editor = sp.edit()
                        editor.remove("phoneNumber")
                        editor.apply()
                        navigator.navigateUp()
                    }) {
                        Text(text= stringResource(R.string.btnModify), style = AppTypography.bodyMedium.copy(appColorScheme.primary))
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.name),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = _name,
                    onValueChange = {
                        _name = it
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.enterName))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusRequester.requestFocus()
                        keyboardController?.hide()
                        showBottomSheet = true
                    }),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).onFocusChanged { isFocus ->
                        validationData()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = stringResource(R.string.birthday),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, appColorScheme.inverseSurface)
                        .clickable {
                            coroutine.launch {
                                showBottomSheet = true
                                focusManager.clearFocus()
                            }
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = _birthday,
                        color = appColorScheme.primary,
                        lineHeight = 1.25.em,
                        style = AppTypography.bodyMedium,
                        modifier = Modifier.padding(20.dp)
                    )
                    IconButton(onClick = {
                        coroutine.launch {
                            showBottomSheet = true
                            focusManager.clearFocus()
                            }
                        },
                        modifier = Modifier.focusRequester(focusRequester).onFocusChanged {
                            validationData()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_keyboard_arrow_down_24dp),
                            contentDescription = "Arrow Down"
                        )
                    }
                }

                if (showBottomSheet) {
                    BottomSheetDialog(
                        onDismissRequest = {
                            Log.d("[BottomSheetDialog]", "onDismissRequest")
                            showBottomSheet = false
                            focusManager.clearFocus()
                            // focusRequester.requestFocus()
                        },
                        properties = BottomSheetDialogProperties(
                            behaviorProperties = BottomSheetBehaviorProperties(
                            )
                        ),
                    ) {
                        Column( modifier = Modifier.fillMaxWidth()) {
                            WheelDatePicker(
                                startDate = LocalDate.parse(_birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                rowCount = 5,
                                size = DpSize(400.dp, 300.dp),
                                textStyle = AppTypography.bodyMedium,
                                selectorProperties = WheelPickerDefaults.selectorProperties(
                                    enabled = true,
                                    shape = MaterialTheme.shapes.medium,
                                    color = appColorScheme.surfaceDim,
                                    border = BorderStroke(2.dp, appColorScheme.outline)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(appColorScheme.secondaryContainer)
                            ) { snappedDate ->
                                Log.e("", "date ${snappedDate.year}, ${snappedDate.month.value}, ${snappedDate.dayOfMonth}")
                                _birthday = LocalDate.of(snappedDate.year, snappedDate.month.value, snappedDate.dayOfMonth)
                                    .toString()
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = {
                                        showBottomSheet = false
                                        focusManager.clearFocus()
                                    },
                                    modifier = Modifier
                                        .width((screenWeight * .4).dp)
                                        .background(appColorScheme.secondary)
                                ) {
                                    Text(
                                        text = stringResource(R.string.Cancel),
                                        style = AppTypography.bodyMedium.copy(appColorScheme.onSecondary)
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        showBottomSheet = false
                                        focusManager.clearFocus()
                                        // focusRequester.requestFocus()
                                    },
                                    modifier = Modifier
                                        .width((screenWeight * .6).dp)
                                        .background(appColorScheme.primary)
                                ) {
                                    Text(
                                        text = stringResource(R.string.OK),
                                        style = AppTypography.bodyMedium.copy(appColorScheme.onPrimary)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = stringResource(R.string.nationality),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            _nationality = "KOREAN"
                            focusManager.clearFocus()
                        }, modifier = Modifier
                            .width((screenWeight * .5).dp)
                            .border(
                                if (_nationality == "KOREAN") 3.dp else 1.dp,
                                if (_nationality == "KOREAN") appColorScheme.primary else appColorScheme.outline
                            )
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                validationData()
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.local),
                            style = AppTypography.bodyMedium.copy(if (_nationality == "K") appColorScheme.primary else appColorScheme.outline),
                            modifier = Modifier
                                .padding(3.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                    TextButton(
                        onClick = {
                            _nationality = "FOREIGN"
                            focusManager.clearFocus()
                        }, modifier = Modifier
                            .width((screenWeight * .5).dp)
                            .border(
                                if (_nationality == "FOREIGN") 3.dp else 1.dp,
                                if (_nationality == "FOREIGN") appColorScheme.primary else appColorScheme.outline
                            )
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                validationData()
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.foreigner),
                            style = AppTypography.bodyMedium.copy(if (_nationality == "FOREIGN") appColorScheme.primary else appColorScheme.outline),
                            modifier = Modifier
                                .padding(3.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            item {
                if (_nationality == "FOREIGN") {
                    Spacer(modifier = Modifier.padding(3.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = _passportNo,
                            onValueChange = {
                                _passportNo = it
                            },
                            placeholder = {
                                Text(text = stringResource(R.string.enterPassportNumber))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                doCheckPassportNumber()
                            }),
                            maxLines = 1,
                            modifier = Modifier
                                .width((screenWeight * .67).dp)
                                .focusRequester(focusRequester)
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        TextButton( onClick = {
                            isSecurity = false
                            keyboardController?.hide()
                            doCheckPassportNumber()
                        }, modifier = Modifier
                            .width((screenWeight * .3).dp)
                            .background(if(_passportNo.isEmpty()) appColorScheme.secondary else appColorScheme.primary)
                        ) {
                            Text(
                                text = stringResource(R.string.check),
                                color = if(_passportNo.isEmpty()) appColorScheme.onSecondary else appColorScheme.onPrimary,
                                lineHeight = 1.25.em,
                                style = AppTypography.labelMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))

                Text(
                    text = stringResource(R.string.gender),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            _gender = "MALE"
                            focusManager.clearFocus()
                            validationData()
                        }, modifier = Modifier
                            .width((screenWeight * .5).dp)
                            .border(
                                if (_gender == "MALE") 3.dp else 1.dp,
                                if (_gender == "MALE") appColorScheme.primary else appColorScheme.outline
                            )
                            .focusRequester(focusRequester)
                    ) {
                        Text(
                            text = stringResource(R.string.male),
                            style = AppTypography.bodyMedium.copy(if (_gender == "MALE") appColorScheme.primary else appColorScheme.outline),
                            modifier = Modifier
                                .padding(3.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                    TextButton(
                        onClick = {
                            _gender = "FEMALE"
                            focusManager.clearFocus()
                            validationData()
                        }, modifier = Modifier
                            .width((screenWeight * .5).dp)
                            .border(
                                if (_gender == "FEMALE") 3.dp else 1.dp,
                                if (_gender == "FEMALE") appColorScheme.primary else appColorScheme.outline
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.female),
                            style = AppTypography.bodyMedium.copy(if (_gender == "FEMALE") appColorScheme.primary else appColorScheme.outline),
                            modifier = Modifier
                                .padding(3.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.id),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = _id,
                        onValueChange = {
                            _id = it
                        },
                        placeholder = {
                            Text(text = stringResource(R.string.enterId))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = {
                            // focusRequester.requestFocus()
                            keyboardController?.hide()
                        }),
                        maxLines = 1,
                        modifier = Modifier.width((screenWeight * .67).dp).fillMaxWidth().focusRequester(focusRequester).onFocusChanged { isFocus ->
                            validationData()
                        }
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    TextButton( onClick = {
                        // if(isCheck) {
                        viewModel.doLoginIdValidation(_id)
                        validationData()
                        // }
                    }, modifier = Modifier
                        .width((screenWeight * .3).dp)
                        .background(/* if (isCheck) */ appColorScheme.primary /* else appColorScheme.inversePrimary */)) {
                        Text(
                            text = stringResource(R.string.validation),
                            color = /* if (isCheck) */ appColorScheme.onPrimary, // else appColorScheme.surfaceDim,
                            lineHeight = 1.25.em,
                            style = AppTypography.labelMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally)
                                .focusRequester(idFocusRequester).onFocusChanged { isFocus ->
                                validationData()
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.password),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = _password,
                    onValueChange = {
                        _password = it
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.enterPassword))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, // 비밀번호용 키보드 타입
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        passwordCheckFocusRequester.requestFocus()
                        keyboardController?.hide()
                    }),
                    visualTransformation = PasswordVisualTransformation(), // 입력값 마스킹
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester)
                        .onFocusChanged { isFocus ->
                            validationData()
                        }
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.passwordCheck),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = _passwordCheck,
                    onValueChange = {
                        _passwordCheck = it
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.enterCheckPassword))
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, // 비밀번호용 키보드 타입
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        emailFocusRequester.requestFocus()
                        keyboardController?.hide()
                    }),
                    visualTransformation = PasswordVisualTransformation(), // 입력값 마스킹
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordCheckFocusRequester)
                        .onFocusChanged { isFocus ->
                            validationData()
                        },
                    isError = _password != _passwordCheck
                )
            }


            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.email),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = _email,
                    onValueChange = {
                        _email = it
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.enterEmail))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        validationData()
                    }),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(emailFocusRequester)
                        .onFocusChanged { isFocus ->
                        validationData()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun JoinPage2Preview() {
    val fakeViewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        JoinPage2(fakeViewModel, navigator, modifier = Modifier.padding(3.dp))
    }
}
