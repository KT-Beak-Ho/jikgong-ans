package com.billcorea.jikgong.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.destinations.WorkerLoginPageDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Composable
fun LoginBottomMiddleView(
  modifier: Modifier = Modifier,
  doCloseBottom: () -> Unit,
  doLoginPerson: () -> Unit,
  doLoginCorp: () -> Unit
) {
  val config = LocalConfiguration.current
  val screenWeight = config.screenWidthDp
  val screenHeight = config.screenHeightDp

  Box(
    modifier = modifier
      .fillMaxSize()
      .fillMaxHeight()
      .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
  ) {
    Text(
      text = stringResource(R.string.login),
      color = MaterialTheme.colorScheme.primary,
      lineHeight = 1.22.em,
      style = AppTypography.titleMedium,
      modifier = Modifier
        .align(alignment = Alignment.TopCenter)
        .offset(
          x = (-132.5).dp,
          y = 17.dp
        )
        .wrapContentHeight(align = Alignment.CenterVertically))
    Image(
      painter = painterResource(id = R.drawable.ic_line_1px),
      contentDescription = "Line_1px",
      colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
      modifier = Modifier
        .align(alignment = Alignment.TopCenter)
        .offset(
          x = 0.dp,
          y = 210.dp
        )
        .requiredWidth(width = 375.dp)
        .requiredHeight(height = 1.dp))
    IconButton(onClick = {
      doCloseBottom()
    }, modifier = Modifier
      .align(alignment = Alignment.TopStart)
      .offset(
        x = 327.dp,
        y = 16.dp
      )
    ) {
      Icon(
        imageVector = Icons.Rounded.Close,
        contentDescription = "24/close",
        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.87f),
      )
    }
    Text(
      text = stringResource(R.string.findJobPlace),
      color = MaterialTheme.colorScheme.onSurface,
      lineHeight = 1.33.em,
      style = AppTypography.titleLarge,
      modifier = Modifier
        .align(alignment = Alignment.TopStart)
        .offset(
          x = 211.dp,
          y = 80.dp
        )
        .wrapContentHeight(align = Alignment.CenterVertically))
    Text(
      text = stringResource(R.string.findWorker),
      color = MaterialTheme.colorScheme.onSurface,
      lineHeight = 1.33.em,
      style = AppTypography.titleLarge,
      modifier = Modifier
        .align(alignment = Alignment.TopStart)
        .offset(
          x = 211.dp,
          y = 254.dp
        )
        .wrapContentHeight(align = Alignment.CenterVertically))
    TextButton(
      onClick = {
        doLoginPerson()
      }, modifier = Modifier
        .align(alignment = Alignment.TopStart)
        .offset(
          x = 211.dp,
          y = 156.dp
        )
        .wrapContentHeight(align = Alignment.CenterVertically)
    ) {
      Text(
        text = stringResource(R.string.personForLogin),
        color = MaterialTheme.colorScheme.secondary,
        lineHeight = 1.22.em,
        style = AppTypography.bodyMedium,

        )
    }
    TextButton(
      onClick = {
        doLoginCorp()
      }, modifier = Modifier
        .align(alignment = Alignment.TopStart)
        .offset(
          x = 211.dp,
          y = 330.dp
        )
        .wrapContentHeight(align = Alignment.CenterVertically)
    )
    {
      Text(
        text = stringResource(R.string.corpForLogin),
        color = MaterialTheme.colorScheme.secondary,
        lineHeight = 1.22.em,
        style = AppTypography.bodyMedium,

        )
    }
    Image(
      painter = painterResource(id = R.drawable.ic_layer_1),
      contentDescription = "Corp For",
      modifier = Modifier
        .align(alignment = Alignment.TopStart)
        .offset(
          x = 40.dp,
          y = 243.dp
        )
        .requiredWidth(width = 131.dp)
        .requiredHeight(height = 111.dp)
    )
    Image(
      painter = painterResource(id = R.drawable.ic_layer_2),
      contentDescription = "Person For",
      modifier = Modifier
        .align(alignment = Alignment.TopStart)
        .offset(
          x = 42.dp,
          y = 76.dp
        )
        .requiredWidth(width = 128.dp)
        .requiredHeight(height = 102.dp)
    )
  }
}

// 프리뷰
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginBottomMiddleViewPreview() {

  val config = LocalConfiguration.current
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()
  var showBottomSheet by remember { mutableStateOf(false) }
  val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false)
  val screenHeight = config.screenHeightDp

  ModalBottomSheet(
    onDismissRequest = {
      showBottomSheet = false
    }
    , sheetState = sheetState
    , modifier = Modifier.height((screenHeight * .5).dp) // .8
  ) {

    LoginBottomMiddleView(modifier = Modifier
      .fillMaxWidth()
      .padding(5.dp),
      doCloseBottom = {
        //showBottomSheet = false
      },
      doLoginPerson = {
        // showBottomSheet = false
        navigator.navigate(WorkerLoginPageDestination)
      },
      doLoginCorp = {

      }
    )
  }
}