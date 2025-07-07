package com.billcorea.jikgong.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.JoinPage1Destination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage6ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerLoginPageDestination
import com.billcorea.jikgong.presentation.worker.login.page1.LoginBottomMiddleView
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.billcorea.jikgong.presentation.destinations.CompanyLoginScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun JikgongApp (
    navigator: DestinationsNavigator,
    modifier: Modifier
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val screenWidth = config.screenWidthDp
    val isDark = isSystemInDarkTheme()
    val navOptions = NavOptions.Builder()
        .setPopUpTo(0, true)
        .setLaunchSingleTop(true)
        .build()

    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false)
    var showBottomSheet by remember { mutableStateOf(false) }
    var showLoginBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    }
                    , sheetState = sheetState
                    , modifier = Modifier.height((screenHeight * .8).dp)
                ) {
                    BottomMiddleView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        doCloseBottom = {
                            showBottomSheet = false
                        },
                        doJoinPerson = {
                            showBottomSheet = false
                            // navigator.navigate(JoinPage2Destination)
                            navigator.navigate(WorkerJoinPage6ScreenDestination)
                                       },
                        doJoinCorp = {
                            showBottomSheet = false
//                            navigator.navigate(CompanyJoinPage1ScreenDestination)
                            navigator.navigate(CompanyJoinPage2ScreenDestination)
                        }
                    )
                }
            }
            if (showLoginBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showLoginBottomSheet = false
                    }
                    , sheetState = sheetState
                    , modifier = Modifier.height((screenHeight * .8).dp)
                ) {
                  LoginBottomMiddleView(
                    modifier = Modifier
                      .fillMaxWidth()
                      .padding(5.dp),
                    doCloseBottom = {
                      showLoginBottomSheet = false
                    },
                    doLoginPerson = {
                      showLoginBottomSheet = false
                      navigator.navigate(WorkerLoginPageDestination)
                    },
                    doLoginCorp = {

                    }
                  )
                }
            }
        }
    ) { innerPadding ->

        Column (
            modifier = modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height((screenHeight * .6).dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Column(modifier=Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(if (isDark) R.drawable.ic_jikgong_white else R.drawable.ic_jikgong_v1),
                        contentDescription = "Logo"
                    )
                    Image(
                        painter = painterResource(if (isDark) R.drawable.ic_example else R.drawable.ic_example_black),
                        contentDescription = "example"
                    )
                }
            }

            Card(
                modifier = Modifier
                    .width((screenWidth * .95).dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                TextButton(
                    onClick = {
                        showBottomSheet = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.joinMember),
                        style = AppTypography.bodyLarge.copy(
                            colorScheme.onPrimary
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .width((screenWidth * .95).dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                TextButton(
                    onClick = {
                        showLoginBottomSheet = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = appColorScheme.secondary)
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        style = AppTypography.bodyLarge.copy(
                            appColorScheme.onSecondary
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun JikgongAppPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()
    Jikgong1111Theme {
        JikgongApp(navigator, modifier = Modifier.padding(3.dp))
    }
}