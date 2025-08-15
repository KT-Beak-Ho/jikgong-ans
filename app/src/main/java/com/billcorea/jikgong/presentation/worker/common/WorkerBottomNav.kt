package com.billcorea.jikgong.presentation.worker.common

// 필요한 imports
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Composable
fun WorkerBottomNav(
    modifier: Modifier = Modifier,
    doWorkerProjectList: () -> Unit,
    doWorkerMyjob: () -> Unit,
    doWorkerEarning: () -> Unit,
    doWorkerProfile: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 일자리 목록
        BottomNavItem(
            icon = Icons.Default.Assignment,
            label = "일자리 목록",
            isSelected = selectedTab == 0,
            onClick = {
                selectedTab = 0
                doWorkerProjectList()
            }
        )

        // 내 일자리
        BottomNavItem(
            icon = Icons.Default.Schedule,
            label = "내 일자리",
            isSelected = selectedTab == 1,
            onClick = {
                selectedTab = 1
                doWorkerMyjob()
            }
        )

        // 지급 관리
        BottomNavItem(
            icon = Icons.Default.AccountBalanceWallet,
            label = "수익 관리",
            isSelected = selectedTab == 2,
            onClick = {
                selectedTab = 2
                doWorkerEarning()
            }
        )

        // 사업자 정보
        BottomNavItem(
            icon = Icons.Default.Person,
            label = "내 정보",
            isSelected = selectedTab == 3,
            onClick = {
                selectedTab = 3
                doWorkerProfile()
            }
        )
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color.Black else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = if (isSelected) Color.Black else Color.Gray,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

