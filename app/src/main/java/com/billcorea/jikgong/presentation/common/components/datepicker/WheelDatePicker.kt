package com.billcorea.jikgong.presentation.common.components.datepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.commandiron.wheel_picker_compose.core.SelectorProperties
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WheelDatePicker(
    modifier: Modifier = Modifier,
    startDate: LocalDate = LocalDate.now(),
    minDate: LocalDate = LocalDate.MIN,
    maxDate: LocalDate = LocalDate.MAX,
    yearsRange: IntRange? = IntRange(1922, 2122),
    size: DpSize = DpSize(256.dp, 128.dp),
    rowCount: Int = 3,
    textStyle: TextStyle = AppTypography.titleMedium,
    textColor: Color = LocalContentColor.current,
    selectorProperties: SelectorProperties = WheelPickerDefaults.selectorProperties(),
    onSnappedDate : (snappedDate: LocalDate) -> Unit = {}
) {
    DefaultWheelDatePicker(
        modifier,
        startDate,
        minDate,
        maxDate,
        yearsRange,
        size,
        rowCount,
        textStyle,
        textColor,
        selectorProperties,
        onSnappedDate = { snappedDate ->
            onSnappedDate(snappedDate.snappedLocalDate)
            snappedDate.snappedIndex
        }
    )
}

@Preview
@Composable
fun JoindatePreview() {

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
    val birthday by remember { mutableStateOf(sdf.format(System.currentTimeMillis())) }
    var _birthday by remember { mutableStateOf(birthday) }

    Jikgong1111Theme {
        WheelDatePicker(
            startDate = LocalDate.parse(_birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
            rowCount = 5,
            size = DpSize(400.dp, 300.dp),
            textStyle = AppTypography.bodyMedium,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                enabled = true,
                shape = MaterialTheme.shapes.medium,
                color = appColorScheme.surfaceDim,
                border = BorderStroke(2.dp, appColorScheme.outline)
            )
        )
    }
}