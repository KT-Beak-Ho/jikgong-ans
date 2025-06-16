package com.billcorea.jikgong.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.activity.compose.BackHandler
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.compose.rememberNavController
import com.afollestad.materialdialogs.BuildConfig
import com.afollestad.materialdialogs.MaterialDialog
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.destinations.JoinPage2Destination
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
fun JoinPage1(
    viewModel : MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var isSecurity by remember { mutableStateOf(false) }
    var isSecretOk by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var secretCode by remember { mutableStateOf("") }
    var isFocusPhoneNo by remember { mutableStateOf(false) }
    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }

    // 뒤로가기 버튼 처리
    BackHandler {
        // 뒤로가기 시 상태 초기화
        isSecurity = false
        isSecretOk = false
        phoneNumber = ""
        secretCode = ""
        viewModel.clearRegisterResult()
        // 실제 뒤로가기 실행
        navigator.navigateUp()
    }
    val _authCode = viewModel.authCode.observeAsState()
    val authCode = _authCode.value

    val _phoneVal = viewModel.isPhoneValidation.observeAsState()
    val phoneVal = _phoneVal.value

    // 수신된 검증 번호와 입력한 값이 동일할 떄만 ...
    isSecretOk = authCode == secretCode
    Log.e("", "ERROR ... $authCode == $secretCode")

    // phoneVal이 변경될 때만 실행 (간단하게 되돌림)
    LaunchedEffect(phoneVal) {
        phoneVal?.let { phoneValResult ->
            if (phoneValResult.indexOf("false") >= 0) {
                isSecurity = false
                MaterialDialog(context).show {
                    icon(R.drawable.ic_jikgong_white)
                    message(R.string.phoneIsNotValid)
                    positiveButton(R.string.OK) {
                        it.dismiss()
                    }
                }
            }
            else if(phoneValResult.indexOf("true") >= 0) {
                if (phoneNumber.startsWith("010") == true && phoneNumber.length >= 11) {
                    isSecurity = true
                    viewModel.doSmsVerification(phoneNumber)
                } else {
                    isSecurity = false
                    MaterialDialog(context).show {
                        icon(R.drawable.ic_jikgong_white)
                        message(R.string.msgPhoneNumberIsNotValid)
                        positiveButton(R.string.OK) {
                            it.dismiss()
                        }
                        cancelOnTouchOutside(false)
                    }
                }
            }
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
                    viewModel.clearRegisterResult()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arrow Back"
                    )
                }
                PageIndicator(
                    numberOfPages = 6,
                    selectedPage = 0, // 이건 index 라서 0 ~ 4 범위내 에서
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
                    val editor = sp.edit()
                    editor.putString("phoneNumber", phoneNumber)
                    editor.apply()
                    if (isSecretOk) {
                        isSecurity = false
                        isSecretOk = false
                        phoneNumber = ""
                        secretCode = ""
                        viewModel.clearRegisterResult()
                        navigator.navigate(JoinPage2Destination)
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
        Column(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Text(
                text = stringResource(R.string.enterPhoneNumber),
                color = appColorScheme.primary,
                lineHeight = 1.33.em,
                style = AppTypography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = stringResource(R.string.telnumber),
                color = appColorScheme.primary,
                lineHeight = 1.25.em,
                style = AppTypography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.enterForNumberOnly))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        isSecurity = true
                    }),
                    maxLines = 1,
                    modifier = Modifier
                        .width((screenWidth * .67).dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocusPhoneNo = focusState.isFocused
                            if (isFocusPhoneNo && isSecurity) {
                                isSecurity = false
                                secretCode = ""
                            }
                        }
                )
                Spacer(modifier = Modifier.padding(2.dp))
                TextButton( onClick = {
                    viewModel.doPhoneValidation(phoneNumber)
                }, modifier = Modifier
                    .width((screenWidth * .3).dp)
                    .background(appColorScheme.primary)) {
                    Text(
                        text = stringResource(R.string.getSecretCode),
                        color = appColorScheme.onPrimary,
                        lineHeight = 1.25.em,
                        style = AppTypography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
            if (isSecurity) {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.secretCode),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = secretCode,
                    onValueChange = {
                        secretCode = it
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.enterSecretCode))
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        // focusManager.clearFocus()
                    }),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinPage1Preview() {
    val viewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        JoinPage1(viewModel, navigator, modifier = Modifier.padding(3.dp))
    }
}
