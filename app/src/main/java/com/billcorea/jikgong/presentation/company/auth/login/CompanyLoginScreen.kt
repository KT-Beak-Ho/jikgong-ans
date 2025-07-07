package com.billcorea.jikgong.presentation.company.auth.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.compose.rememberNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.destinations.CompanyProjectListScreenDestination
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
fun CompanyLoginScreen(
    viewModel: MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var loginIdOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val _loginResult = viewModel.loginResult.observeAsState()
    val _loginError = viewModel.loginError.observeAsState()
    val loginResult = _loginResult.value
    val loginError = _loginError.value

    LaunchedEffect(loginError) {
        loginError?.let {
            MaterialDialog(context).show {
                icon(R.drawable.ic_jikgong_white)
                message(text = it)
                positiveButton(R.string.OK) { dialog -> dialog.dismiss() }
            }
        }
    }

    LaunchedEffect(loginResult) {
        loginResult?.let {
            // 기업 로그인 성공 시 CompanyProjectListScreen으로 이동
            navigator.navigate(CompanyProjectListScreenDestination)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = appColorScheme.outlineVariant,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    Log.e("", "backArrow")
                    navigator.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arrow Back"
                    )
                }
                Text(
                    text = "기업 로그인",
                    color = appColorScheme.onPrimaryContainer,
                    style = AppTypography.titleMedium,
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedTextField(
                value = loginIdOrPhone,
                onValueChange = {
                    loginIdOrPhone = it
                },
                placeholder = {
                    Text(text = stringResource(R.string.loginIdOrPhone))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                maxLines = 1,
                modifier = Modifier
                    .width((screenWidth * .90).dp)
                    .align(Alignment.CenterHorizontally)
                    .focusRequester(focusRequester)
            )

            Spacer(modifier = Modifier.padding(4.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                placeholder = {
                    Text(text = stringResource(R.string.password))
                },
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }),
                modifier = Modifier
                    .width((screenWidth * .90).dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            // 로그인 버튼
            TextButton(
                onClick = {
                    if (loginIdOrPhone.isNotBlank() && password.isNotBlank()) {
                        val deviceToken = "test_device_token"
                        viewModel.doLogin(loginIdOrPhone, password, deviceToken)
                    } else {
                        MaterialDialog(context).show {
                            icon(R.drawable.ic_jikgong_white)
                            message(R.string.errorLoginBlank)
                            positiveButton(R.string.OK) { it.dismiss() }
                        }
                    }
                },
                modifier = Modifier
                    .width((screenWidth * .90).dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .background(appColorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.login),
                    color = appColorScheme.onPrimary,
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

@Preview(showBackground = true)
@Composable
fun CompanyLoginScreenPreview() {
    val viewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyLoginScreen(viewModel, navigator, modifier = Modifier.padding(3.dp))
    }
}