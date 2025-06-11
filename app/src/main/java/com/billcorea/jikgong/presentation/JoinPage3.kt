package com.billcorea.jikgong.presentation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.runtime.getValue
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
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.destinations.JoinPage4Destination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun JoinPage3(
    viewModel : MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val config = LocalConfiguration.current
    val screenWeight = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    var isSecretOk by remember { mutableStateOf(false) }
    val name by remember { mutableStateOf("") }
    var _name by remember { mutableStateOf(name) }
    var _bankCode by remember { mutableStateOf("") }
    var _accountNumber by remember { mutableStateOf("") }
    var _bankName by remember { mutableStateOf(context.getString(R.string.msgSelectBank)) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false)
    var showBottomSheet by remember { mutableStateOf(false) }

    fun validationData() {
        if (_name.isNotEmpty() && _bankCode.isNotEmpty() && _accountNumber.isNotEmpty()) {
            isSecretOk = true
        } else {
            isSecretOk = false
        }
    }

    if (_bankCode.isNotEmpty()) {
        _bankName = viewModel.bankName[viewModel.bankCode.indexOf(_bankCode)]
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            }
            , sheetState = sheetState
            , modifier = Modifier.fillMaxHeight()
        ) {
            BankSelectList(
                doBankSelect = { bankCode ->
                    Log.e(",", "bankCode = $bankCode")
                    _bankName = viewModel.bankName[viewModel.bankCode.indexOf(bankCode)]
                    _bankCode = bankCode
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
                    viewModel.clearRegisterResult()
                    navigator.navigateUp()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Arrow Back"
                    )
                }
                PageIndicator(
                    numberOfPages = 6,
                    selectedPage = 2,
                    defaultRadius = 12.dp,
                    selectedLength = 24.dp,
                    space = 6.dp,
                    animationDurationInMillis = 1000,
                )
            }
        }, bottomBar = {
            TextButton(
                onClick = {
                    val editor = sp.edit()
                    editor.putString("accountName", _name)
                    editor.putString("bankName", _bankName)
                    editor.putString("accountNumber", _accountNumber)
                    editor.apply()
                    if (isSecretOk) {
                        navigator.navigate(JoinPage4Destination)
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
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            item {
                Text(
                    text = stringResource(R.string.enterBankAccountInfo),
                    color = appColorScheme.primary,
                    lineHeight = 1.33.em,
                    style = AppTypography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.padding(5.dp))

                Image(
                    painter = painterResource(id = R.drawable.bank_description),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.accountName),
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
                        validationData()
                    }),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocus ->
                            validationData()
                        }
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.bankName),
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
                            showBottomSheet = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = _bankName,
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
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = stringResource(R.string.accountNumber),
                    color = appColorScheme.primary,
                    lineHeight = 1.25.em,
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.padding(5.dp))
                OutlinedTextField(
                    value = _accountNumber,
                    onValueChange = {
                        _accountNumber = it
                    },
                    placeholder = {
                        Text(text = stringResource(R.string.enterAccountNumber))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        validationData()
                    }),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocus ->
                            validationData()
                        }
                )
            }
        }
    }
}

@Composable
fun BankSelectList(
    doBankSelect:(bankCode : String) -> Unit,
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
                text = stringResource(R.string.selectBank),
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
                doBankSelect("006")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_kb_v2),
                        contentDescription = "KB",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.kbbank), style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doBankSelect("021")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_sinhan_v2),
                        contentDescription = "SinHan",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.shinhan), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("089")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_kbank_v2),
                        contentDescription = "K Bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.kbank), style = AppTypography.bodyMedium)
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
                doBankSelect("020")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_woori_v2),
                        contentDescription = "WooRi",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.wooriBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("011")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_nhbank_v2),
                        contentDescription = "NongHyub",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.nhBank), style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doBankSelect("005")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_hana_v2),
                        contentDescription = "Hana Bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.hanaBank), style = AppTypography.bodyMedium)
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
                doBankSelect("003")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_ibk_v2),
                        contentDescription = "ibk",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.ibkBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("090")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_kakao_v2),
                        contentDescription = "kakao bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.kakaoBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("031")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_imbank_v2),
                        contentDescription = "IM Bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.imBank), style = AppTypography.bodyMedium)
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
                doBankSelect("092")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_toss_v2),
                        contentDescription = "toss",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.tossBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("032")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_bnk_v2),
                        contentDescription = "bnk busan bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.bankBusan), style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doBankSelect("023")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_sc_jeil_v2),
                        contentDescription = "SC Jeil Bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.scJeil), style = AppTypography.bodyMedium)
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
                doBankSelect("045")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_mg_saemaeul_v2),
                        contentDescription = "MG Sae ma eul",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.mgSaemaeul), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("071")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_post_bank_v2),
                        contentDescription = "post bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.postBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("039")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_bnk_v2),
                        contentDescription = "BNK GN Bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.gyungNamBank), style = AppTypography.bodyMedium)
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
                doBankSelect("034")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_gwangju_v2),
                        contentDescription = "GwangJu bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.gwangJuBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("002")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_kdb_sanup_v2),
                        contentDescription = "KDB Sanup bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.kdbBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("048")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_cu_v2),
                        contentDescription = "CU",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.cu), style = AppTypography.bodyMedium)
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
                doBankSelect("037")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_gwangju_v2),
                        contentDescription = "JB bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.jeunBukBank), style = AppTypography.bodyMedium)
                }

            }
            IconButton(onClick = {
                doBankSelect("027")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_citibank_v2),
                        contentDescription = "citi bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.citiBank), style = AppTypography.bodyMedium)
                }
            }
            IconButton(onClick = {
                doBankSelect("007")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_suhyub_v2),
                        contentDescription = "SH",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.shBank), style = AppTypography.bodyMedium)
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                doBankSelect("035")
            }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_sinhan_v2),
                        contentDescription = "Jeju bank",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text= stringResource(R.string.jejuBank), style = AppTypography.bodyMedium)
                }
            }
        }
    }
}

@Preview
@Composable
fun JoinPage3Preview() {
    val fakeViewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        JoinPage3(fakeViewModel, navigator, modifier = Modifier.padding(3.dp))
    }
}


