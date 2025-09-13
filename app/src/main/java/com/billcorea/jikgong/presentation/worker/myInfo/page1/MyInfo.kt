package com.billcorea.jikgong.presentation.worker.myInfo.page1

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun MyInfo(
  viewModel : MainViewModel = koinViewModel(),
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier
) {
  var isNightWorkEnabled by remember { mutableStateOf(true) }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .background(
              brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                colors = listOf(
                  Color(0xFF4B7BFF).copy(alpha = 0.05f),
                  Color(0xFF6B8BFF).copy(alpha = 0.03f)
                )
              )
            )
        ) {
          TopAppBar(
            title = {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(10.dp),
                  color = Color(0xFF4B7BFF).copy(alpha = 0.15f),
                  modifier = Modifier.size(32.dp)
                ) {
                  Box(
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = "👤",
                      style = AppTypography.titleMedium
                    )
                  }
                }
                Text(
                  text = "내 정보",
                  style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                  ),
                  color = Color(0xFF1A1D29)
                )
              }
            },
            colors = TopAppBarDefaults.topAppBarColors(
              containerColor = Color.Transparent
            )
          )
        }
      }
    },
    containerColor = Color.Transparent
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(
              Color(0xFFF8FAFC),
              Color(0xFFEFF3FF)
            )
          )
        )
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ) {
        // Profile Header
        item {
          ProfileHeaderCard()
        }

        // Night work notification card
        item {
          NightWorkCard()
        }

        // Settings Menu
        item {
          SettingsMenuCard(
            isNightWorkEnabled = isNightWorkEnabled,
            onNightWorkToggle = { isNightWorkEnabled = it }
          )
        }

        // Logout Button
        item {
          LogoutButtonCard()
        }

        item {
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
private fun ProfileHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E7EB)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF4B7BFF).copy(alpha = 0.1f),
                    modifier = Modifier.size(60.dp),
                    border = BorderStroke(
                        width = 2.dp,
                        color = Color(0xFF4B7BFF).copy(alpha = 0.3f)
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color(0xFF4B7BFF),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
    
                Spacer(modifier = Modifier.width(16.dp))
    
                Column {
                    Text(
                        text = "김석공",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )
                    Text(
                        text = "정보 수정 >",
                        style = AppTypography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}

@Composable
private fun NightWorkCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.dp,
            color = Color(0xFF4B7BFF).copy(alpha = 0.3f)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF4B7BFF).copy(alpha = 0.1f),
                            Color(0xFF6B8BFF).copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF4B7BFF).copy(alpha = 0.15f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🌙",
                                style = AppTypography.titleLarge
                            )
                        }
                    }
                    
                    Column {
                        Text(
                            text = "밤근 일자리 제안",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = "새로운 제안이 있습니다",
                            style = AppTypography.bodySmall,
                            color = Color(0xFF6B7280)
                        )
                    }
                }
                
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF4B7BFF)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "2",
                            style = AppTypography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsMenuCard(
    isNightWorkEnabled: Boolean,
    onNightWorkToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E7EB)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF4B7BFF).copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFF4B7BFF),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Text(
                    text = "설정 메뉴",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Settings items
            SettingItemWithSwitch(
                icon = Icons.Default.Notifications,
                title = "일자리 제안 받기",
                isChecked = isNightWorkEnabled,
                onCheckedChange = onNightWorkToggle
            )
            
            SettingItem(
                icon = Icons.Default.Person,
                title = "맞춤 정보 설정"
            )
            
            SettingItem(
                icon = Icons.Default.Bookmark,
                title = "스크랩한 일자리"
            )
            
            SettingItem(
                icon = Icons.Default.Notifications,
                title = "알림 설정"
            )
            
            SettingItem(
                icon = Icons.Default.Event,
                title = "이벤트"
            )
            
            SettingItem(
                icon = Icons.Default.Public,
                title = "공지사항"
            )
            
            SettingItem(
                icon = Icons.Default.Support,
                title = "고객센터"
            )
            
            SettingItem(
                icon = Icons.Default.School,
                title = "학습 및 정책"
            )
            
            SettingItem(
                icon = Icons.Default.Info,
                title = "앱 버전",
                subtitle = "현재 0.1.1/빌드 0.1.7"
            )
        }
    }
}

@Composable
private fun LogoutButtonCard() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Logout Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFEF4444),
                                Color(0xFFDC2626)
                            )
                        )
                    )
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "로그아웃",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
            }
        }
        
        // Bottom Links
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "로그아웃",
                style = AppTypography.bodyMedium,
                color = Color(0xFF6B7280)
            )
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Text(
                text = "회원탈퇴",
                style = AppTypography.bodyMedium,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
fun SettingItem(
  icon: ImageVector,
  title: String,
  subtitle: String? = null,
  onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF4B7BFF).copy(alpha = 0.1f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF1A1D29)
            )
            subtitle?.let {
                Text(
                    text = it,
                    style = AppTypography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun SettingItemWithSwitch(
  icon: ImageVector,
  title: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFF4B7BFF).copy(alpha = 0.1f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = title,
            style = AppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Color(0xFF1A1D29),
            modifier = Modifier.weight(1f)
        )
        
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4B7BFF),
                uncheckedThumbColor = Color(0xFF9CA3AF),
                uncheckedTrackColor = Color(0xFFE5E7EB)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyInfoPreview() {
  val viewModel = MainViewModel()
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    MyInfo(viewModel, navigator, modifier = Modifier.padding(3.dp))
  }
}
