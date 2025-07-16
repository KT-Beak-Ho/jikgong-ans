// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/components/InfoHeader.kt
package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.info.data.CompanyInfoData
import com.billcorea.jikgong.presentation.company.main.info.data.CompanyInfoSampleData
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun InfoHeader(
    companyInfo: CompanyInfoData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 프로필 이미지 (플레이스홀더)
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                color = appColorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = "기업 프로필",
                    tint = Color.White,
                    modifier = Modifier.padding(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = companyInfo.companyName,
                style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onPrimaryContainer
            )

            Text(
                text = "사업자등록번호: ${companyInfo.businessNumber}",
                style = AppTypography.bodyMedium,
                color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoHeaderPreview() {
    Jikgong1111Theme {
        InfoHeader(
            companyInfo = CompanyInfoSampleData.getSampleCompanyInfo(),
            modifier = Modifier.padding(16.dp)
        )
    }
}