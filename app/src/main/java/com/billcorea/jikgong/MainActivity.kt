package com.billcorea.jikgong

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
import com.billcorea.jikgong.presentation.KakaoMapView
import com.billcorea.jikgong.presentation.SplashScreen
import com.billcorea.jikgong.presentation.destinations.JikgongAppDestination
import com.billcorea.jikgong.presentation.destinations.JoinPage1Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage2Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage3Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage4Destination
import com.billcorea.jikgong.presentation.destinations.JoinPage5Destination
import com.billcorea.jikgong.presentation.destinations.KakaoMapViewDestination
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMapSdk
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : ComponentActivity() {

    val viewModel : MainViewModel by viewModels()
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
