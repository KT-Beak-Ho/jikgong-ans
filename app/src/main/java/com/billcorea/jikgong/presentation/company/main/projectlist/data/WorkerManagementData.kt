package com.billcorea.jikgong.presentation.company.main.projectlist.data

data class WorkerAttendanceInfo(
    val hasCheckedIn: Boolean = false,
    val hasCheckedOut: Boolean = false,
    val hasPaymentRecord: Boolean = false
)

enum class DateStatus {
    PAST,    // 과거 날짜
    TODAY,   // 오늘 날짜
    FUTURE   // 미래 날짜
}