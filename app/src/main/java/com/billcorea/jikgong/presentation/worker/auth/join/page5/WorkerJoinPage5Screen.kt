package com.billcorea.jikgong.presentation.worker.auth.join.page5

// import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage6ScreenDestination
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.FeatureThatRequiresCameraPermission
import com.billcorea.jikgong.presentation.GlideImage
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage4ScreenDestination
import com.billcorea.jikgong.presentation.destinations.WorkerJoinPage6ScreenDestination
import com.billcorea.jikgong.presentation.worker.auth.common.components.CommonWorkerTopBar
import com.billcorea.jikgong.presentation.worker.auth.common.constants.WorkerJoinConstants
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedEvent
import com.billcorea.jikgong.presentation.worker.auth.join.shared.WorkerJoinSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun WorkerJoinPage5Screen(
  workerJoinViewModel: WorkerJoinSharedViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  val uiState by workerJoinViewModel.uiState.collectAsStateWithLifecycle()
  val shouldNavigateToNextPage by workerJoinViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
  val shouldNavigateBack by workerJoinViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

  val context = LocalContext.current
  val config = LocalConfiguration.current
  val screenWidth = config.screenWidthDp

  // 기존 로컬 상태들은 이제 uiState에서 가져옴
  // var showBottomSheet by remember { mutableStateOf(false) } -> uiState.showBottomSheet
  // var showLaterDialog by remember { mutableStateOf(false) } -> uiState.showLaterDialog
  // var currentPhotoPath by remember { mutableStateOf("") } -> uiState.currentPhotoPath
  // var takePicType by remember { mutableStateOf("") } -> uiState.takePicType
  // var isGrantCamera by remember { mutableStateOf(false) } -> uiState.isGrantCamera

  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

  // 갤러리 런처
  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
  ) { uri: Uri? ->
    uri?.let {
      when (uiState.takePicType) {
        "educationCertificate" -> {
          workerJoinViewModel.onEvent(
            WorkerJoinSharedEvent.UpdateEducationCertificateUri(it.toString())
          )
        }
        "workerCard" -> {
          workerJoinViewModel.onEvent(
            WorkerJoinSharedEvent.UpdateWorkerCardUri(it.toString())
          )
        }
      }
    }
  }

  // 카메라 런처
  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
  ) { success: Boolean ->
    if (success && uiState.currentPhotoPath.isNotEmpty()) {
      val uri = Uri.fromFile(File(uiState.currentPhotoPath))
      when (uiState.takePicType) {
        "educationCertificate" -> {
          workerJoinViewModel.onEvent(
            WorkerJoinSharedEvent.UpdateEducationCertificateUri(uri.toString())
          )
        }
        "workerCard" -> {
          workerJoinViewModel.onEvent(
            WorkerJoinSharedEvent.UpdateWorkerCardUri(uri.toString())
          )
        }
      }
    }
  }

  // 이미지 파일 생성 함수
  fun createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
      workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateCurrentPhotoPath(absolutePath))
    }
  }

  // 카메라 촬영 함수
  fun takePhoto() {
    try {
      val photoFile = createImageFile()
      val photoURI = FileProvider.getUriForFile(
        context,
        "com.billcorea.jikgong.fileprovider",
        photoFile
      )
      cameraLauncher.launch(photoURI)
    } catch (ex: IOException) {
      Log.e("WorkerJoinPage5", "Error occurred while creating the file: ${ex.message}")
    }
  }

  // 페이지 실행 시 초기화
  LaunchedEffect(Unit) {
    workerJoinViewModel.onEvent(WorkerJoinSharedEvent.ResetJoin5Flow)
  }

  // 네비게이션 이벤트 처리 - 다음페이지
  LaunchedEffect(shouldNavigateToNextPage) {
    if (shouldNavigateToNextPage) {
      navigator.navigate(WorkerJoinPage6ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  // 네비게이션 이벤트 처리 - 이전페이지
  LaunchedEffect(shouldNavigateBack) {
    if (shouldNavigateBack) {
      navigator.navigateUp()
      navigator.navigate(WorkerJoinPage4ScreenDestination)
      workerJoinViewModel.clearNavigationEvents()
    }
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .padding(top = 20.dp),
    topBar = {
      CommonWorkerTopBar(
        currentPage = uiState.currentPage,
        totalPages = WorkerJoinConstants.TOTAL_PAGES,
        onBackClick = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.PreviousPage)
        }
      )
    },
    bottomBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 나중에 하기 버튼
        TextButton(
          onClick = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowLaterDialog(true))
          },
          modifier = Modifier
            .width((screenWidth * .45).dp)
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(appColorScheme.onPrimary)
        ) {
          Text(
            text = stringResource(R.string.toLater),
            color = appColorScheme.primary,
            lineHeight = 1.25.em,
            style = AppTypography.labelMedium,
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentWidth(Alignment.CenterHorizontally)
          )
        }

        Spacer(modifier = Modifier.padding(5.dp))

        // 완료 버튼
        TextButton(
          onClick = {
            if (uiState.isPage5Complete) {
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.NextPage)
            }
          },
          modifier = Modifier
            .width((screenWidth * .45).dp)
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .background(
              if (uiState.isPage5Complete) appColorScheme.primary
              else appColorScheme.inversePrimary
            )
        ) {
          Text(
            text = stringResource(R.string.completed),
            color = if (uiState.isPage5Complete) appColorScheme.onPrimary
            else appColorScheme.surfaceDim,
            lineHeight = 1.25.em,
            style = AppTypography.labelMedium,
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentWidth(Alignment.CenterHorizontally)
          )
        }
      }
    }
  ) { innerPadding ->
    LazyColumn(
      verticalArrangement = Arrangement.spacedBy(16.dp),
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      // 헤더
      item {
        Text(
          text = stringResource(R.string.lastTwoRegister),
          color = appColorScheme.primary,
          lineHeight = 1.33.em,
          style = AppTypography.titleLarge,
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.padding(5.dp))

        Image(
          painter = painterResource(id = R.drawable.joinpage5_guide_v1),
          contentDescription = null,
          modifier = Modifier.fillMaxWidth()
        )
      }

      // 교육자격증 섹션
      item {
        Text(
          text = stringResource(R.string.safeManagerCard),
          color = appColorScheme.primary,
          lineHeight = 1.25.em,
          style = AppTypography.bodyMedium,
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        FeatureThatRequiresCameraPermission(
          doResult = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateIsGrantCamera(it))
          }
        )

        if (uiState.isGrantCamera) {
          if (uiState.educationCertificateUri.isNotEmpty()) {
            // 이미지가 있을 때
            Card(
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary)
            ) {
              GlideImage(
                url = uiState.educationCertificateUri,
                doRefresh = {
                  workerJoinViewModel.onEvent(
                    WorkerJoinSharedEvent.UpdateEducationCertificateUri("")
                  )
                }
              )
            }
          } else {
            // 이미지가 없을 때
            IconButton(
              onClick = {
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateTakePicType("educationCertificate"))
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowBottomSheet(true))
              },
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary)
            ) {
              Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_camera_v1),
                  contentDescription = null,
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                  text = stringResource(R.string.msgImageSet),
                  style = AppTypography.bodyMedium.copy(appColorScheme.primary)
                )
              }
            }
          }
        }
      }

      // 근로자증 섹션
      item {
        Text(
          text = stringResource(R.string.workerCard),
          color = appColorScheme.primary,
          lineHeight = 1.25.em,
          style = AppTypography.bodyMedium,
          modifier = Modifier.fillMaxWidth()
        )
      }

      item {
        FeatureThatRequiresCameraPermission(
          doResult = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateIsGrantCamera(it))
          }
        )

        if (uiState.isGrantCamera) {
          if (uiState.workerCardUri.isNotEmpty()) {
            // 이미지가 있을 때
            Card(
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary)
            ) {
              GlideImage(
                url = uiState.workerCardUri,
                doRefresh = {
                  workerJoinViewModel.onEvent(
                    WorkerJoinSharedEvent.UpdateWorkerCardUri("")
                  )
                }
              )
            }
          } else {
            // 이미지가 없을 때
            IconButton(
              onClick = {
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateTakePicType("workerCard"))
                workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowBottomSheet(true))
              },
              modifier = Modifier
                .padding(3.dp)
                .width(343.dp)
                .height(300.dp)
                .background(appColorScheme.onPrimary)
            ) {
              Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_camera_v1),
                  contentDescription = null,
                  modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                  text = stringResource(R.string.msgImageSet),
                  style = AppTypography.bodyMedium.copy(appColorScheme.primary)
                )
              }
            }
          }
        }
      }
    }

    // 사진 촬영 방법 선택 바텀시트
    if (uiState.showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowBottomSheet(false))
        },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight()
      ) {
        TakePictureMethodBottomSheet(
          onCameraSelected = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowBottomSheet(false))
            takePhoto()
          },
          onGallerySelected = {
            workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowBottomSheet(false))
            galleryLauncher.launch("image/*")
          }
        )
      }
    }

    // 나중에 하기 확인 다이얼로그
    if (uiState.showLaterDialog) {
      AlertDialog(
        onDismissRequest = {
          workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowLaterDialog(false))
        },
        title = { Text(text = "알림") },
        text = { Text(text = stringResource(R.string.msgToLater)) },
        confirmButton = {
          TextButton(
            onClick = {
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowLaterDialog(false))
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.NextPage)
            }
          ) {
            Text(stringResource(R.string.OK))
          }
        },
        dismissButton = {
          TextButton(
            onClick = {
              workerJoinViewModel.onEvent(WorkerJoinSharedEvent.UpdateShowLaterDialog(false))
            }
          ) {
            Text(stringResource(android.R.string.cancel))
          }
        }
      )
    }
  }
}

@Composable
fun TakePictureMethodBottomSheet(
  onCameraSelected: () -> Unit,
  onGallerySelected: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp)
  ) {
    Text(
      text = "사진 등록 방법을 선택해주세요",
      style = AppTypography.titleMedium,
      modifier = Modifier.padding(bottom = 16.dp)
    )

    TextButton(
      onClick = onCameraSelected,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          painter = painterResource(R.drawable.ic_camera_v1),
          contentDescription = "Camera"
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
          text = stringResource(R.string.useCamera),
          style = AppTypography.bodyMedium
        )
      }
    }

    TextButton(
      onClick = onGallerySelected,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "Gallery"
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
          text = stringResource(R.string.useGallery),
          style = AppTypography.bodyMedium
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun WorkerJoinPage5ScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    WorkerJoinPage5Screen(
      workerJoinViewModel = WorkerJoinSharedViewModel(),
      navigator = navigator,
      modifier = Modifier.padding(3.dp)
    )
  }
}