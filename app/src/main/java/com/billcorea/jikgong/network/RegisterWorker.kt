package com.billcorea.jikgong.network

import java.util.Dictionary

data class RegisterWorker(
    var loginId: String,
    var password: String,
    var phone: String,
    var email: String,
    var role: String,
    var privacyConsent: Boolean,
    var deviceToken: String,
    var isNotification: Boolean,
    var workerName: String,
    var birth: String,
    var gender: String,
    var nationality: String,
    var accountHolder: String,
    var account: String,
    var bank: String,
    var workerCardNumber: String,
    var hasVisa: Boolean,
    var credentialLiabilityConsent: Boolean,
    val workExperienceRequest: List<WorkExperience>,
    var address: String,
    var latitude: Double,
    var longitude: Double
)

data class WorkExperience (
    var tech: String,
    var experienceMonths: Int
)
