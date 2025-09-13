package com.billcorea.jikgong.presentation.company.main.projectlist.data

data class AttendanceWorker(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String, // "남", "여"
    val phoneNumber: String,
    var attendanceStatus: AttendanceStatus = AttendanceStatus.NONE,
    var statusChangeTime: String? = null // 출근/결근 상태 변경 시간
)

enum class AttendanceStatus {
    NONE,        // 아무것도 선택안됨 (출근전)
    NOT_ARRIVED, // 결근
    ARRIVED      // 출근
}

data class CheckoutWorker(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String, // "남", "여"
    var checkoutStatus: CheckoutStatus = CheckoutStatus.NONE
)

enum class CheckoutStatus {
    NONE,        // 아무것도 선택안됨
    EARLY_LEAVE, // 조퇴
    NORMAL_LEAVE // 정상퇴근
}