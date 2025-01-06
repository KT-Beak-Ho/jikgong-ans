package com.billcorea.jikgong.network


data class VisaExpiryDateRequest(
    var birthDate: String,
    var country: String,
    var nationality: String,
    var passportNo: String
)