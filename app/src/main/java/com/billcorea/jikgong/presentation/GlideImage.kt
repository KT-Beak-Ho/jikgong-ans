package com.billcorea.jikgong.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.exifinterface.media.ExifInterface
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


@SuppressLint("DefaultLocale")
@Composable
fun GlideImage(url: String, doRefresh: () -> Unit) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val screenWidth = config.screenWidthDp

    fun getRotation(context: Context, imagePath: String): Float {
        return try {
            val exif = ExifInterface(imagePath)
            when (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                else -> 0f
            }
        } catch (e: IOException) {
            e.printStackTrace()
            0f
        }
    }

    val bitmapFlow = remember(url) {
        callbackFlow {
            val target = object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    trySend(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            }
            val rotation = getRotation(context, url)
            val requestOptions = RequestOptions().transform(Rotate(rotation.toInt()))
            Glide.with(context)
                .asBitmap()
                .load(url)
                .override((screenWidth * .8).toInt(), (screenHeight * .6).toInt())
                .apply(requestOptions)
                .into(target)
            awaitClose { Glide.with(context).clear(target) }
        }
    }
    val bitmap by bitmapFlow.collectAsState(initial = null)

    Box (modifier = Modifier
        .fillMaxSize()
        .border(1.dp, appColorScheme.onPrimary, RoundedCornerShape(16.dp))
        .padding(5.dp),
    ) {
        bitmap?.let { it ->
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "bitMap",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .fillMaxSize()
                    .padding(5.dp)
                    .border(1.dp, appColorScheme.outline, RoundedCornerShape(16.dp))
            )

            Row ( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top){
                IconButton(onClick = {
                    doRefresh()
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = appColorScheme.onPrimary
                    )
                }
            }

        } ?: run {
            // Show a placeholder while loading
            Box(modifier = Modifier
                .fillMaxSize()
                .border(1.dp, appColorScheme.outline, RoundedCornerShape(16.dp))) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}




