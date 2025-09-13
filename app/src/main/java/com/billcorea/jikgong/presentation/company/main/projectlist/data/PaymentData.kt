package com.billcorea.jikgong.presentation.company.main.projectlist.data

data class PaymentWorker(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String, // "남", "여"
    val paymentAmount: Int, // 지급 금액 (0이면 미지급)
    val attendanceStatus: String, // "정상출근", "조퇴", "결근"
    val jobRole: String, // 직무 (예: "철근공", "형틀목공", "토공", "미장공")
    val workDescription: String, // 수행 업무 내용
    val rating: Float = 0.0f // 평점 (0~5점, 0은 미평가)
)