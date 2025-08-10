package com.billcorea.jikgong

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.billcorea.jikgong.presentation.JikgongApp
import com.billcorea.jikgong.presentation.JoinPage1
import com.billcorea.jikgong.presentation.JoinPage2
import com.billcorea.jikgong.presentation.JoinPage3
import com.billcorea.jikgong.presentation.JoinPage4
import com.billcorea.jikgong.presentation.JoinPage5
import com.billcorea.jikgong.presentation.JoinPage6
import com.billcorea.jikgong.presentation.KakaoMapView
import com.billcorea.jikgong.presentation.SplashScreen
import com.billcorea.jikgong.presentation.company.auth.join.page1.CompanyJoinPage1Screen
import com.billcorea.jikgong.presentation.company.auth.join.page2.CompanyJoinPage2Screen
import com.billcorea.jikgong.presentation.company.auth.join.page3.CompanyJoinPage3Screen
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage3ScreenDestination
import com.billcorea.jikgong.presentation.destinations.GraphScreenDestination
import com.billcorea.jikgong.presentation.destinations.IncomeManagementScreenDestination
import com.billcorea.jikgong.presentation.destinations.JikgongAppDestination
import com.billcorea.jikgong.presentation.destinations.JoinPage1Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage2Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage3Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage4Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage5Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage6Destination
import com.billcorea.jikgong.presentation.destinations.KakaoMapViewDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage3ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage4ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage5ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage6ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerLoginPageDestination
import com.billcorea.jikgong.presentation.destinations.WorkerProjectDestination
import com.billcorea.jikgong.presentation.destinations.WorkerProjectListDestination
import com.billcorea.jikgong.presentation.worker.auth.join.page1.WorkerJoinPage1Screen
import com.billcorea.jikgong.presentation.worker.auth.join.page2.WorkerJoinPage2Screen
import com.billcorea.jikgong.presentation.worker.auth.join.page3.WorkerJoinPage3Screen
import com.billcorea.jikgong.presentation.worker.auth.join.page4.WorkerJoinPage4Screen
import com.billcorea.jikgong.presentation.worker.auth.join.page5.WorkerJoinPage5Screen
import com.billcorea.jikgong.presentation.worker.auth.join.page6.WorkerJoinPage6Screen
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import com.billcorea.jikgong.presentation.worker.income.page1.IncomeManagementScreen
import com.billcorea.jikgong.presentation.worker.income.page2.GraphScreen
import com.billcorea.jikgong.presentation.worker.login.page1.WorkerLoginPage
import com.billcorea.jikgong.presentation.worker.project.WorkerProject
import com.billcorea.jikgong.presentation.worker.projectList.page1.WorkerProjectList
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMapSdk
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

class MainActivity : ComponentActivity() {

    val viewModel : MainViewModel by viewModels()
    private val companyJoinViewModel = CompanyJoinSharedViewModel()
    private val workerJoinViewModel = WorkerJoinSharedViewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("", "KeyHash: ${getKeyHash(this)} ${BuildConfig.KAKAO_API}")

        checkLocationPermission()

        setContent {

            var isSplash by remember { mutableStateOf(true)}
            val navController = rememberNavController()
            val navigator = navController.toDestinationsNavigator()

            Jikgong1111Theme(dynamicColor = true) {
                Surface (tonalElevation = 5.dp) {
                    if (isSplash) {
                        SplashScreen(modifier = Modifier.padding(3.dp), onTimeout = {
                            Log.e("", "이제 뭐 하지 ???")
                            isSplash = false
                        })
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = JikgongAppDestination.route,
                            modifier = Modifier.padding(1.dp)
                        ) {
                            composable(JikgongAppDestination.route) {
                                JikgongApp(navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(JoinPage1Destination.route) {
                                JoinPage1(viewModel, navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(JoinPage2Destination.route) {
                                JoinPage2(viewModel, navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(JoinPage3Destination.route) {
                                JoinPage3(viewModel, navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(JoinPage4Destination.route) {
                                JoinPage4(viewModel, navigator, modifier = Modifier.padding(5.dp), this@MainActivity)
                            }
                            composable(KakaoMapViewDestination.route) {
                                KakaoMapView(viewModel, navigator)
                            }
                            composable(JoinPage5Destination.route) {
                                JoinPage5(viewModel, navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(JoinPage6Destination.route) {
                                JoinPage6(viewModel, navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(WorkerLoginPageDestination.route) {
                                WorkerLoginPage(viewModel, navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(WorkerProjectListDestination.route) {
                                WorkerProjectList(viewModel, navigator, modifier = Modifier.padding(5.dp))
                            }
                            composable(WorkerProjectDestination.route) {
                                WorkerProject()
                            }
                            composable(IncomeManagementScreenDestination.route) {
                                IncomeManagementScreen(navigator = navigator)
                            }
                            composable(GraphScreenDestination.route) {
                                GraphScreen()
                            }
                            composable(WorkerJoinPage1ScreenDestination.route) {
                                WorkerJoinPage1Screen(
                                    workerJoinViewModel = workerJoinViewModel,
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(WorkerJoinPage2ScreenDestination.route) {
                                WorkerJoinPage2Screen(
                                    workerJoinViewModel = workerJoinViewModel,
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(WorkerJoinPage3ScreenDestination.route) {
                                WorkerJoinPage3Screen(
                                    workerJoinViewModel = workerJoinViewModel,
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(WorkerJoinPage4ScreenDestination.route) {
                                WorkerJoinPage4Screen(
                                    workerJoinViewModel = workerJoinViewModel,
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(WorkerJoinPage5ScreenDestination.route) {
                                WorkerJoinPage5Screen(
                                    workerJoinViewModel = workerJoinViewModel,
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(WorkerJoinPage6ScreenDestination.route) {
                                WorkerJoinPage6Screen(
                                    workerJoinViewModel = workerJoinViewModel,
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(CompanyJoinPage1ScreenDestination.route) {
                                CompanyJoinPage1Screen(
                                    companyJoinViewModel = companyJoinViewModel, // 기업 전용 ViewModel 전달
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(CompanyJoinPage2ScreenDestination.route) {
                                CompanyJoinPage2Screen(
                                    companyJoinViewModel = companyJoinViewModel, // 기업 전용 ViewModel 전달
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            composable(CompanyJoinPage3ScreenDestination.route) {
                                CompanyJoinPage3Screen(
                                    companyJoinViewModel = companyJoinViewModel, // 기업 전용 ViewModel 전달
                                    navigator = navigator,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        Log.e("", "checkLocationPermission ...")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLastKnownLocation()
        } else {
            MaterialDialog(this).show {
                icon(R.drawable.ic_jikgong_v1)
                title(R.string.app_name)
                message(R.string.location_permission_message)
                positiveButton(R.string.OK) {
                    finish()
                }
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission()
            return
        }
    }

    fun getKeyHash(context: Context): String? {
        return  KakaoMapSdk.INSTANCE.getHashKey()
    }
}

@Composable
@Preview
fun AppPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()
    Jikgong1111Theme(dynamicColor = true) {
        Surface (tonalElevation = 5.dp) {
            JikgongApp(navigator = navigator, modifier = Modifier.padding(3.dp))
        }
    }
}

@Composable
@Preview
fun DarkAppPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()
    Jikgong1111Theme(darkTheme = true,
        dynamicColor = true
    ) {
        Surface (tonalElevation = 5.dp) {
            JikgongApp(navigator = navigator, modifier = Modifier.padding(3.dp))
        }
    }
}
