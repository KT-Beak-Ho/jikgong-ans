package com.billcorea.jikgong.presentation.worker.projectList.page1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.BottomMiddleView
import com.billcorea.jikgong.presentation.destinations.JoinPage1Destination
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Composable
fun WorkerBottomNav(
    modifier: Modifier = Modifier,
    doWorkerProjectList: () -> Unit,
    doWorkerMyjob: () -> Unit,
    doWorkerEarning: () -> Unit,
    doWorkerProfile: () -> Unit
) {
    val config = LocalConfiguration.current
    val screenWeight = config.screenWidthDp
    val screenHeight = config.screenHeightDp

    Box(
        modifier = modifier
            .fillMaxSize()
            .height((screenHeight * .10).dp)
            .clip(shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
    ) {
        IconButton(onClick = {

        }, modifier = Modifier
            .align(alignment = Alignment.TopStart)
            .offset(
                x = 327.dp,
                y = 16.dp
            )
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "닫기",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.87f)
                )
                Text(
                    text = "닫기",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        IconButton(onClick = {

        }, modifier = Modifier
            .align(alignment = Alignment.TopStart)
            .offset(
                x = 327.dp,
                y = 16.dp
            )
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "닫기",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.87f)
                )
                Text(
                    text = "닫기",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// 프리뷰
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun WorkerBottomNavPreview() {

    val config = LocalConfiguration.current
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false)
    val screenHeight = config.screenHeightDp

    BottomMiddleView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        doCloseBottom = {
            //showBottomSheet = false
        },
        doJoinPerson = {
            // showBottomSheet = false
            navigator.navigate(JoinPage1Destination)
        },
        doJoinCorp = {

        }
    )
}
