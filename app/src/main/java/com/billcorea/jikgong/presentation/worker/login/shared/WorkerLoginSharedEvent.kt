package com.billcorea.jikgong.presentation.worker.login.shared

sealed class WorkerLoginSharedEvent  {
  // Login Input Event
  data class updateLoginIdOrPhone(val loginIdOrPhone: String) : WorkerLoginSharedEvent()
  data class updatePassword(val password: String) : WorkerLoginSharedEvent()
  object RequestLogin : WorkerLoginSharedEvent()

  // Login Error Event
  data class updateErrorMessage(val errorMessage: String) : WorkerLoginSharedEvent()

  // Login Success Event
  data class updateLoginToken(val accessToken: String, val refreshToken: String) : WorkerLoginSharedEvent()
  data class updateRole(val role: String) : WorkerLoginSharedEvent()

  // Navigation Event
  object toProjectListPage : WorkerLoginSharedEvent()
  object ClearError : WorkerLoginSharedEvent()

  // Init Login Page
  object InitLogin : WorkerLoginSharedEvent()

}