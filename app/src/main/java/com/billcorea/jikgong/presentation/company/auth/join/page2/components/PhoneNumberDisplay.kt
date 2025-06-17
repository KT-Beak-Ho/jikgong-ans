package com.billcorea.jikgong.presentation.company.auth.join.page2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.R
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun PhoneNumberDisplay(
    phoneNumber: String,
//    onModifyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.telnumber),
            color = appColorScheme.primary,
            lineHeight = 1.25.em,
            style = AppTypography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        //  전화번호 출력 부분
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(appColorScheme.surfaceDim)
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = phoneNumber,
                style = AppTypography.titleMedium.copy(appColorScheme.secondary)
            )
//            TextButton(onClick = onModifyClick) {
//                Text(
//                    text = stringResource(R.string.btnModify),
//                    style = AppTypography.bodyMedium.copy(appColorScheme.primary)
//                )
//            }
        }
    }
}