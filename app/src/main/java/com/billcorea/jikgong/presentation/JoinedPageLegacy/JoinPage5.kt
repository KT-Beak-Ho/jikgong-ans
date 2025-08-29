package com.billcorea.jikgong.presentation.JoinedPageLegacy

import android.content.Context
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.FileProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.FeatureThatRequiresCameraPermission
import com.billcorea.jikgong.presentation.GlideImage
import com.billcorea.jikgong.presentation.PageIndicator
import com.billcorea.jikgong.presentation.destinations.JoinPage6Destination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
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
fun JoinPage5(
    viewModel: MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    var safeCardNumber by remember { mutableStateOf("") }
    var isSecretOk by remember { mutableStateOf(false) }
    var isGrantCamera by remember { mutableStateOf(false) }
    var isUseCamera by remember { mutableStateOf(false) }
    var isUsePic by remember { mutableStateOf(false) }
    var isOnCamera by remember { mutableStateOf(false) }
    var galleryUri by remember { mutableStateOf<Uri?>(null) }
    var takeUri by remember { mutableStateOf<Uri?>(null) }
    var _safeManagerCardUri by remember { mutableStateOf<Uri?>(null) }
    var _workerCardUri by remember { mutableStateOf<Uri?>(null) }
    var currentPhotoPath: String by remember { mutableStateOf("") }
    var takePicTy by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            galleryUri = uri
            isUsePic = true

            if (takePicTy == "safeManagerCard") {
                _safeManagerCardUri = galleryUri
            }
            if (takePicTy == "workerCard") {
                _workerCardUri = galleryUri
            }
            isSecretOk = true
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                currentPhotoPath.let {
                    val file = File(currentPhotoPath)
                    val uri = Uri.fromFile(file)
                    takeUri = uri
                    isUseCamera = true

                    if (takePicTy == "safeManagerCard") {
                        _safeManagerCardUri = takeUri
                    }
                    if (takePicTy == "workerCard") {
                        _workerCardUri = takeUri
                    }
                    isSecretOk = true
                }
            } else {
                Log.e("MainActivity", "Camera operation cancelled or failed.")
            }
        }

    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd", Locale.KOREAN).format(Date())
        val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    suspend fun captureImage(): Uri? {
        return suspendCoroutine { continuation ->
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Log.e("MainActivity", "Error occurred while creating the file")
                null
            }
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    context,
                    "com.billcorea.jikgong.fileprovider",
                    it
                )
                takePictureLauncher.launch(photoURI).apply {
                    continuation.resume(photoURI)
                }
            } ?: continuation.resume(null)
        }
    }

    LaunchedEffect(isOnCamera) {
        Log.e("", "launchEffect $isOnCamera ... ")
        if (isOnCamera) {
            val uri = captureImage()
            takeUri = uri
            isUseCamera = false
            isOnCamera = false
            Log.e("", "launchEffect $isUseCamera ... ")
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }, sheetState = sheetState, modifier = Modifier.fillMaxHeight()
        ) {
            TakePictureMethod(doSelect = { method ->
                showBottomSheet = false
                if ("camera" == method) {
                    galleryUri = null
                    takeUri = null
                    isUsePic = false
                    isUseCamera = false
                    isOnCamera = true
                }
                if ("gallery" == method) {
                    isUsePic = false
                    galleryUri = null
                    takeUri = null
                    isUseCamera = false
                    launcher.launch("image/*")
                }
            })
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
                    selectedPage = 4,
                    defaultRadius = 12.dp,
                    selectedLength = 24.dp,
                    space = 6.dp,
                    animationDurationInMillis = 1000,
                )
            }
        }, bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        MaterialDialog(context).show {
                            icon(R.drawable.ic_jikgong_white)
                            message(R.string.msgToLater)
                            positiveButton(R.string.OK) {
                                it.dismiss()
                                val editor = sp.edit()
                                editor.putString("safeManagerCardUri", "")
                                editor.putString("workerCardCardUri", "")
                                editor.apply()
                                navigator.navigate(JoinPage6Destination)
                            }
                        }
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
                TextButton(
                    onClick = {
                        val editor = sp.edit()
                        //editor.putString("myLocation", address)
                        if(isSecretOk) {
                            if(_safeManagerCardUri == null) {
                                editor.putString("safeManagerCardUri", "")
                                editor.putString("workerCardCardUri", _workerCardUri.toString())
                            }
                            else if(_workerCardUri == null) {
                                editor.putString("safeManagerCardUri", _safeManagerCardUri.toString())
                                editor.putString("workerCardCardUri", "")
                            }
                            else {
                                editor.putString("safeManagerCardUri", _safeManagerCardUri.toString())
                                editor.putString("workerCardCardUri", _workerCardUri.toString())
                            }
                            editor.apply()
                            navigator.navigate(JoinPage6Destination)
                        }
                        // 가입 완료 처리 후 ...
                    },
                    modifier = Modifier
                        .width((screenWidth * .45).dp)
                        .padding(WindowInsets.navigationBars.asPaddingValues())
                        .background(if (isSecretOk) appColorScheme.primary else appColorScheme.inversePrimary)
                ) {
                    Text(
                        text = stringResource(R.string.completed),
                        color = if (isSecretOk) appColorScheme.onPrimary else appColorScheme.surfaceDim,
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
        LazyColumn(modifier = modifier.padding(innerPadding)) {
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

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.safeManagerCard),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                FeatureThatRequiresCameraPermission(doResult = { isGrantCamera = it })
                if (isGrantCamera) {

                    if (_safeManagerCardUri != null) {
                        Card(
                            modifier = Modifier
                                .padding(3.dp)
                                .width(343.dp)
                                .height(300.dp)
                                .background(appColorScheme.onPrimary),
                        ) {
                            GlideImage(_safeManagerCardUri.toString(), doRefresh = {
                                _safeManagerCardUri = null
                                isUsePic = false
                                isUseCamera = false
                                if (_workerCardUri == null) {
                                    isSecretOk = false
                                }
                            })
                        }

                    } else {
                        IconButton(
                            onClick = {
                                takePicTy = "safeManagerCard"
                                showBottomSheet = true
                            },
                            modifier = Modifier
                                .padding(3.dp)
                                .width(343.dp)
                                .height(300.dp)
                                .background(appColorScheme.onPrimary),
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
                                    style = AppTypography.bodyMedium.copy(
                                        appColorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            if (_safeManagerCardUri != null) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = safeCardNumber,
                            onValueChange = {
                                safeCardNumber = it
                            },
                            placeholder = {
                                Text(text = stringResource(R.string.enterForNumberOnly))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {

                            }),
                            maxLines = 1,
                            modifier = Modifier
                                .width((screenWidth * .67).dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->

                                }
                        )
                        Spacer(modifier = Modifier.padding(2.dp))
                        TextButton( onClick = {
                            MaterialDialog(context).show {
                                icon(R.drawable.ic_jikgong_white)
                                message(R.string.msgCertified)
                                positiveButton(R.string.OK) {
                                    it.dismiss()
                                }
                            }
                        }, modifier = Modifier
                            .width((screenWidth * .3).dp)
                            .background(appColorScheme.primary)) {
                            Text(
                                text = stringResource(R.string.OK),
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

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.workerCard),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                FeatureThatRequiresCameraPermission(doResult = { isGrantCamera = it })
                if (isGrantCamera) {
                    if (_workerCardUri != null) {
                        Card(
                            modifier = Modifier
                                .padding(3.dp)
                                .width(343.dp)
                                .height(300.dp)
                                .background(appColorScheme.onPrimary),
                        ) {
                            GlideImage(_workerCardUri.toString(), doRefresh = {
                                _workerCardUri = null
                                isUsePic = false
                                isUseCamera = false
                                if (_safeManagerCardUri == null) {
                                    isSecretOk = false
                                }
                            })
                        }
                    } else {
                        IconButton(
                            onClick = {
                                takePicTy = "workerCard"
                                showBottomSheet = true
                            },
                            modifier = Modifier
                                .padding(3.dp)
                                .width(343.dp)
                                .height(300.dp)
                                .background(appColorScheme.onPrimary),
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
                                    style = AppTypography.bodyMedium.copy(
                                        appColorScheme.primary
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TakePictureMethod(doSelect: (method: String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = {
            doSelect("camera");
        }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_camera_v1),
                    contentDescription = "Camera"
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = stringResource(R.string.useCamera), style = AppTypography.bodyMedium)
            }
        }
        TextButton(onClick = {
            doSelect("gallery");
        }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Picture")
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = stringResource(R.string.useGallery), style = AppTypography.bodyMedium)
            }
        }
    }
}
