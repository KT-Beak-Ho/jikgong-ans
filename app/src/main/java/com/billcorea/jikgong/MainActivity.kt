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
import com.billcorea.jikgong.presentation.JoinedPageLegacy.JoinPage1
import com.billcorea.jikgong.presentation.JoinedPageLegacy.JoinPage2
import com.billcorea.jikgong.presentation.JoinedPageLegacy.JoinPage3
import com.billcorea.jikgong.presentation.JoinedPageLegacy.JoinPage4
import com.billcorea.jikgong.presentation.JoinedPageLegacy.JoinPage5
import com.billcorea.jikgong.presentation.JoinedPageLegacy.JoinPage6
import com.billcorea.jikgong.presentation.KakaoMapView
import com.billcorea.jikgong.presentation.SplashScreen
import com.billcorea.jikgong.presentation.company.auth.join.page1.CompanyJoinPage1Screen
import com.billcorea.jikgong.presentation.company.auth.join.page2.CompanyJoinPage2Screen
import com.billcorea.jikgong.presentation.company.auth.join.page3.CompanyJoinPage3Screen
import com.billcorea.jikgong.presentation.company.auth.login.page1.CompanyLoginPage1Screen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.project.ProjectDetailScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.worker.AttendanceCheckScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.worker.CheckoutScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.worker.WorkerManagementScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.worker.WorkerInfoScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.job.ExistingJobScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen.JobCreationScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.worker.payment.PaymentSummaryScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.job.TempSaveScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.job.PreviousJobPostsScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.screen.ProjectListScreen
import com.billcorea.jikgong.presentation.company.main.scout.CompanyScoutMainScreen
import com.billcorea.jikgong.presentation.company.main.money.CompanyMoneyScreen
import com.billcorea.jikgong.presentation.company.main.info.screen.CompanyInfoScreen
import com.billcorea.jikgong.presentation.company.main.info.screen.NotificationSettingsScreen
import com.billcorea.jikgong.presentation.company.main.info.screen.AnnouncementScreen
import com.billcorea.jikgong.presentation.company.main.info.screen.CustomerServiceScreen
import com.billcorea.jikgong.presentation.company.main.info.screen.TermsAndPoliciesScreen
import com.billcorea.jikgong.presentation.company.main.info.screen.MyInfoScreen
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyScoutMainScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyMoneyScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyInfoScreenDestination
import com.billcorea.jikgong.presentation.destinations.NotificationSettingsScreenDestination
import com.billcorea.jikgong.presentation.destinations.AnnouncementScreenDestination
import com.billcorea.jikgong.presentation.destinations.CustomerServiceScreenDestination
import com.billcorea.jikgong.presentation.destinations.TermsAndPoliciesScreenDestination
import com.billcorea.jikgong.presentation.destinations.MyInfoScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyJoinPage3ScreenDestination
import com.billcorea.jikgong.presentation.destinations.CompanyLoginPage1ScreenDestination
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
import com.billcorea.jikgong.presentation.destinations.MyInfoDestination
import com.billcorea.jikgong.presentation.destinations.ProjectDetailScreenDestination
import com.billcorea.jikgong.presentation.destinations.ProjectListScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage1ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage2ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage3ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage4ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage5ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage6ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerLoginPageDestination
import com.billcorea.jikgong.presentation.destinations.WorkerMyProjectAcceptedScreenDestination
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
import com.billcorea.jikgong.presentation.worker.login.shared.WorkerLoginViewModel
import com.billcorea.jikgong.presentation.worker.myInfo.page1.MyInfo
import com.billcorea.jikgong.presentation.worker.myProject.page1.WorkerMyProjectAcceptedScreen
import com.billcorea.jikgong.presentation.worker.project.WorkerProject
import com.billcorea.jikgong.presentation.worker.projectList.page1.WorkerProjectList
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMapSdk
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

class MainActivity : ComponentActivity() {

  val viewModel: MainViewModel by viewModels()
  private val workerJoinViewModel = WorkerJoinSharedViewModel()
  private val workerLoginViewModel = WorkerLoginViewModel()
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    Log.e("", "KeyHash: ${getKeyHash(this)} ${BuildConfig.KAKAO_API}")

    checkLocationPermission()

    setContent {

      var isSplash by remember { mutableStateOf(true) }
      val navController = rememberNavController()
      val navigator = navController.toDestinationsNavigator()


      Jikgong1111Theme(dynamicColor = true) {
        Surface(tonalElevation = 5.dp) {
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
                JoinPage4(
                  viewModel,
                  navigator,
                  modifier = Modifier.padding(5.dp),
                  this@MainActivity
                )
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
                WorkerLoginPage(
                  workerLoginViewModel = workerLoginViewModel,
                  navigator = navigator,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(WorkerProjectListDestination.route) {
                WorkerProjectList(viewModel, navigator, modifier = Modifier.padding(5.dp))
              }
              composable(WorkerMyProjectAcceptedScreenDestination.route) {
                WorkerMyProjectAcceptedScreen(
                  viewModel,
                  navigator,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(MyInfoDestination.route) {
                MyInfo(viewModel, navigator, modifier = Modifier.padding(5.dp))
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
                  navigator = navigator,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(CompanyJoinPage2ScreenDestination.route) {
                CompanyJoinPage2Screen(
                  navigator = navigator,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(CompanyJoinPage3ScreenDestination.route) {
                CompanyJoinPage3Screen(
                  navigator = navigator,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(CompanyLoginPage1ScreenDestination.route) {
                CompanyLoginPage1Screen(
                  navigator = navigator,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(ProjectListScreenDestination.route) {
                ProjectListScreen(
                  navController = navController,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(CompanyScoutMainScreenDestination.route) {
                CompanyScoutMainScreen(
                  navigator = navigator,
                  navController = navController,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(CompanyMoneyScreenDestination.route) {
                CompanyMoneyScreen(
                  navigator = navigator,
                  navController = navController,
                  modifier = Modifier.padding(5.dp)
                )
              }
              composable(CompanyInfoScreenDestination.route) {
                CompanyInfoScreen(
                  navigator = navigator,
                  navController = navController
                )
              }
              composable(NotificationSettingsScreenDestination.route) {
                NotificationSettingsScreen(
                  navController = navController
                )
              }
              composable(AnnouncementScreenDestination.route) {
                AnnouncementScreen(
                  navController = navController
                )
              }
              composable(CustomerServiceScreenDestination.route) {
                CustomerServiceScreen(
                  navController = navController
                )
              }
              composable(TermsAndPoliciesScreenDestination.route) {
                TermsAndPoliciesScreen(
                  navController = navController
                )
              }
              composable(MyInfoScreenDestination.route) {
                MyInfoScreen(
                  navController = navController
                )
              }
              /** ProjectListScreen 에서 프로젝트 관리 버튼 클릭시 사용하는 Router */
              composable(
                route = ProjectDetailScreenDestination.route,
                arguments = ProjectDetailScreenDestination.arguments
              ) { navBackStackEntry ->
                val projectId = navBackStackEntry.arguments?.getString("projectId") ?: ""
                ProjectDetailScreen(
                  navController = navController,
                  projectId = projectId,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** ProjectDetailScreen 에서 인력 관리 버튼 클릭시 사용하는 Router */
              composable("worker_management/{workDayId}") { navBackStackEntry ->
                val workDayId = navBackStackEntry.arguments?.getString("workDayId") ?: ""
                WorkerManagementScreen(
                  navController = navController,
                  workDayId = workDayId,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** WorkerManagementScreen 에서 출근확정 근로자 정보 버튼 클릭시 사용하는 Router */
              composable("worker_info/{workDayId}?selectedDate={selectedDate}") { navBackStackEntry ->
                val workDayId = navBackStackEntry.arguments?.getString("workDayId") ?: ""
                val selectedDate = navBackStackEntry.arguments?.getString("selectedDate")
                WorkerInfoScreen(
                  navController = navController,
                  workDayId = workDayId,
                  selectedDate = selectedDate,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** WorkerManagementScreen 에서 출근확인 버튼 클릭시 사용하는 Router */
              composable("attendance_check/{workDayId}?selectedDate={selectedDate}") { navBackStackEntry ->
                val workDayId = navBackStackEntry.arguments?.getString("workDayId") ?: ""
                val selectedDate = navBackStackEntry.arguments?.getString("selectedDate")
                AttendanceCheckScreen(
                  navController = navController,
                  workDayId = workDayId,
                  selectedDate = selectedDate,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** WorkerManagementScreen 에서 퇴근확인 버튼 클릭시 사용하는 Router */
              composable("checkout/{workDayId}?selectedDate={selectedDate}") { navBackStackEntry ->
                val workDayId = navBackStackEntry.arguments?.getString("workDayId") ?: ""
                val selectedDate = navBackStackEntry.arguments?.getString("selectedDate")
                CheckoutScreen(
                  navController = navController,
                  workDayId = workDayId,
                  selectedDate = selectedDate,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** WorkerManagementScreen 에서 지급내역서 보기 버튼 클릭시 사용하는 Router */
              composable("payment_summary/{workDayId}?selectedDate={selectedDate}") { navBackStackEntry ->
                val workDayId = navBackStackEntry.arguments?.getString("workDayId") ?: ""
                val selectedDate = navBackStackEntry.arguments?.getString("selectedDate")
                PaymentSummaryScreen(
                  navController = navController,
                  workDayId = workDayId,
                  selectedDate = selectedDate,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** ProjectDetailScreen 에서 일자리 등록 버튼 클릭시 사용하는 Router */
              composable("job_registration?projectStartDate={projectStartDate}&projectEndDate={projectEndDate}") { navBackStackEntry ->
                val projectStartDate = navBackStackEntry.arguments?.getString("projectStartDate")
                val projectEndDate = navBackStackEntry.arguments?.getString("projectEndDate")
                JobCreationScreen(
                  onNavigateBack = { navController.popBackStack() },
                  projectStartDate = projectStartDate,
                  projectEndDate = projectEndDate
                )
              }
              /** ProjectDetailScreen 에서 일자리 등록 (재사용 포함) 버튼 클릭시 사용하는 Router */
              composable("job_registration?reuseId={reuseId}") { navBackStackEntry ->
                val reuseId = navBackStackEntry.arguments?.getString("reuseId")
                JobCreationScreen(
                  onNavigateBack = { navController.popBackStack() },
                  reuseJobPostId = reuseId
                )
              }
              /** ProjectDetailScreen 에서 기존 공고 재사용 버튼 클릭시 사용하는 Router */
              composable("previous_job_posts/{projectId}") { navBackStackEntry ->
                val projectId = navBackStackEntry.arguments?.getString("projectId") ?: ""
                PreviousJobPostsScreen(
                  navController = navController,
                  projectId = projectId,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** JobRegistrationScreen 에서 임시 버튼 클릭시 사용하는 Router */
              composable("temp_save") {
                TempSaveScreen(
                  navController = navController,
                  modifier = Modifier.padding(5.dp)
                )
              }
              /** JobRegistrationScreen 에서 기존공고 이용 버튼 클릭시 사용하는 Router */
              composable("existing_job") {
                ExistingJobScreen(
                  navController = navController,
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
      == PackageManager.PERMISSION_GRANTED
    ) {
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
      != PackageManager.PERMISSION_GRANTED
    ) {
      checkLocationPermission()
      return
    }
  }

  fun getKeyHash(context: Context): String? {
    return KakaoMapSdk.INSTANCE?.getHashKey()
  }
}


@Composable
@Preview
fun AppPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()
  Jikgong1111Theme(dynamicColor = true) {
    Surface(tonalElevation = 5.dp) {
      JikgongApp(navigator = navigator, modifier = Modifier.padding(3.dp))
    }
  }
}

@Composable
@Preview
fun DarkAppPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()
  Jikgong1111Theme(
    darkTheme = true,
    dynamicColor = true
  ) {
    Surface(tonalElevation = 5.dp) {
      JikgongApp(navigator = navigator, modifier = Modifier.padding(3.dp))
    }
  }
}