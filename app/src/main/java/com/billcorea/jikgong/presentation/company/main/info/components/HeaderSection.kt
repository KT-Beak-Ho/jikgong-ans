package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.network.models.CompanyData
import com.billcorea.jikgong.network.models.CompanyType

@Composable
fun HeaderSection(
  companyData: CompanyData,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color(0xFFFAFAFA),
                Color.White
              )
            )
          )
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column {
            Text(
              text = companyData.name,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = companyData.statusText,
              fontSize = 14.sp,
              color = Color(0xFF6B7280)
            )
          }

          if (companyData.type == CompanyType.PREMIUM) {
            Surface(
              shape = RoundedCornerShape(100.dp),
              color = Color.Transparent
            ) {
              Box(
                modifier = Modifier
                  .background(
                    Brush.horizontalGradient(
                      colors = listOf(
                        Color(0xFF7C3AED),
                        Color(0xFF8B5CF6)
                      )
                    )
                  )
                  .padding(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "PREMIUM",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp,
                  color = Color.White
                )
              }
            }
          }
        }
      }
      HorizontalDivider(
        thickness = 1.dp,
        color = Color(0xFFF3F4F6)
      )
    }
  }
}