package com.billcorea.jikgong.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.PageIndicator
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

class AddressFindActivity : ComponentActivity() {


    inner class MyJavaScriptInterface {

        @JavascriptInterface
        fun processDATA(data: String?) {
            Log.e("TAG", "processDATA ${data}")

            val intent = Intent()
            intent.putExtra("data", data)
            setResult(AppCompatActivity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jikgong1111Theme(dynamicColor = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp), topBar = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    Log.e("", "backArrow")
                                    finish()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close"
                                    )
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                                Text(
                                    text = stringResource(R.string.findedAddress),
                                    color = appColorScheme.primary,
                                    lineHeight = 1.33.em,
                                    style = AppTypography.titleLarge,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                )
                            }
                        }, bottomBar = {

                        }
                    ) { innerPadding ->
                        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {

                            WebViewForAddress(this@AddressFindActivity,
                                doFinish = {
                                    val intent = Intent()
                                    setResult(AppCompatActivity.RESULT_CANCELED, intent)
                                    finish()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
@Composable
fun WebViewForAddress(addressFindActivity: AddressFindActivity, doFinish:() -> Unit) {

    val blogspot = "https://billcoreatech.blogspot.com/2022/06/blog-post.html"

    val webViewState =
        rememberWebViewState(
            url = blogspot,
            additionalHttpHeaders = emptyMap()
        )
    val webChromeClient = AccompanistWebChromeClient()
    val webViewNavigator = rememberWebViewNavigator()

    WebView(
        state = webViewState,
        client = object : AccompanistWebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view?.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        },
        chromeClient = webChromeClient,
        navigator = webViewNavigator,
        onCreated = { webView ->
            with(webView) {
                settings.run {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    javaScriptCanOpenWindowsAutomatically = true
                }
                addJavascriptInterface(addressFindActivity.MyJavaScriptInterface(), "Android")
            }
        }
    )

    BackHandler(enabled = true) {
        if (webViewNavigator.canGoBack) {
            webViewNavigator.navigateBack()
        } else {
            doFinish()
        }
    }
}
