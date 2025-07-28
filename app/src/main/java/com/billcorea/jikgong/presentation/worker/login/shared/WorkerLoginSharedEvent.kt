package com.billcorea.jikgong.presentation.worker.login.shared

sealed class WorkerLoginSharedEvent  {
  // Login Event
  data class updateLoginIdOrPhone(val loginIdOrPhone: String) : WorkerLoginSharedEvent()
  data class updatePassword(val password: String) : WorkerLoginSharedEvent()

  data class RequestLogin(val loginIdOrPhone: String, val password: String, val deviceToken: String) : WorkerLoginSharedEvent()

  // Login Request Event
  data class updateErrorMessage(val errorMessage: String) : WorkerLoginSharedEvent()
}