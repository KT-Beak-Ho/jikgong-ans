package com.billcorea.jikgong.network.model.worker

// RegisterWorker.kt 내용
data class RegisterWorkerRequest(
  val loginId: String,
  val password: String,
  val phone: String,
  val email: String,
  val role: String = "WORKER",
  val privacyConsent: Boolean,
  val deviceToken: String,
  val isNotification: Boolean,
  val workerName: String,
  val birth: String,
  val gender: String,
  val nationality: String,
  val accountHolder: String,
  val account: String,
  val bank: String,
  val workerCardNumber: String,
  val hasVisa: Boolean,
  val credentialLiabilityConsent: Boolean,
  val workExperienceRequest: List<WorkExperience>,
  val address: String,
  val latitude: Double,
  val longitude: Double
)

// RegisterWorkerResponse.kt 내용
data class RegisterWorkerResponse(
  val code: String,
  val message: String,
  val data: RegisterWorkerData? = null
)

// RegisterWorkerData (RegisterWorkerResponse.kt 내부에 있었을 것으로 추정)
data class RegisterWorkerData(
  val workerId: String,
  val loginId: String,
  val name: String
)

// RegisterWorkerErrorResponse.kt 내용
data class RegisterWorkerErrorResponse(
  val code: String,
  val message: String,
  val errors: List<FieldError>?
)

// FieldError (RegisterWorkerErrorResponse.kt 내부에 있었을 것으로 추정)
data class FieldError(
  val field: String,
  val message: String
)

// VisaExpiryDateRequest.kt 내용
data class VisaExpiryDateRequest(
  val workerId: String,
  val visaType: String,
  val expiryDate: String
)