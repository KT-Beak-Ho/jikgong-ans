package com.billcorea.jikgong.presentation.worker.auth.join.shared

import com.billcorea.jikgong.network.auth.WorkExperience

sealed class WorkerJoinSharedEvent {
  // Page 1 이벤트
  data class UpdatePhoneNumber(val phoneNumber: String) : WorkerJoinSharedEvent()  //  사용자 휴대폰 번호 입력
  data class UpdateVerificationCode(val code: String) : WorkerJoinSharedEvent()    //  사용자 인증 번호 입력
  object RequestVerificationCode : WorkerJoinSharedEvent()                         //  인증번호 검증

  // Page 2 이벤트
  data class UpdateUserName(val name: String) : WorkerJoinSharedEvent()                  // 사용자 이름 입력
  data class UpdateUserId(val id:String): WorkerJoinSharedEvent()                        // 사용자 ID 입력
  data class UpdateUserPassword(val password: String) : WorkerJoinSharedEvent()          // 사용자 비밀번호 입력
  data class UpdateUserPasswordConfirm(val password: String) : WorkerJoinSharedEvent()   // 사용자 비밀번호 입력확인
  data class UpdateUserMail(val email: String) : WorkerJoinSharedEvent()                 // 사용자 Mail 입력
  data class UpdateBirthday(val birthday: String) : WorkerJoinSharedEvent()              // 사용자 생년월일
  data class UpdateGender(val gender: String) : WorkerJoinSharedEvent()                  // 성별
  data class UpdateNationality(val nationality: String) : WorkerJoinSharedEvent()        // 내, 외국인

  // Page 3 이벤트
  data class UpdateAccountName(val accountName: String) : WorkerJoinSharedEvent()
  data class UpdateBankName(val bankName: String) : WorkerJoinSharedEvent()
  data class UpdateAccountNumber(val accountNumber: String) : WorkerJoinSharedEvent()

  // Page 4 이벤트
  data class UpdateAddress(val address: String) : WorkerJoinSharedEvent()
  data class UpdateLat(val lat: Double) : WorkerJoinSharedEvent()
  data class UpdateLon(val lon: Double) : WorkerJoinSharedEvent()
  data class UpdateAddressName(val addressName: String) : WorkerJoinSharedEvent()
  data class KakaoGeocoding(val query: String) : WorkerJoinSharedEvent()

  // Page 5 이벤트
  data class UpdateEducationCertificateUri(val educationCertificateUri: String) : WorkerJoinSharedEvent()
  data class UpdateWorkerCardUri(val workerCardUri: String) : WorkerJoinSharedEvent()
  data class UpdateShowBottomSheet(val showBottomSheet: Boolean) : WorkerJoinSharedEvent()
  data class UpdateShowLaterDialog(val showLaterDialog: Boolean) : WorkerJoinSharedEvent()
  data class UpdateCurrentPhotoPath(val currentPhotoPath: String) : WorkerJoinSharedEvent()
  data class UpdateTakePicType(val takePicType: String) : WorkerJoinSharedEvent()
  data class UpdateIsGrantCamera(val isGrantCamera: Boolean) : WorkerJoinSharedEvent()

  // Page 6 이벤트
  data class AddWorkExperience(val workExperience: WorkExperience) : WorkerJoinSharedEvent()
  data class RemoveWorkExperience(val workExperience: WorkExperience) : WorkerJoinSharedEvent()
  data class UpdateSelectedJob(val jobCode: String, val jobName: String) : WorkerJoinSharedEvent()
  data class UpdateYearInput(val year: String) : WorkerJoinSharedEvent()
  data class UpdateMonthInput(val month: String) : WorkerJoinSharedEvent()
  object SubmitRegistration : WorkerJoinSharedEvent()


  // 네비게이션 이벤트
  object NextPage : WorkerJoinSharedEvent()
  object PreviousPage : WorkerJoinSharedEvent()
  object HomePage : WorkerJoinSharedEvent()
  object ClearError : WorkerJoinSharedEvent()

  // 네비게이션 이벤트 - 해당 페이지 입력 값 초기화
  object ResetJoin1Flow : WorkerJoinSharedEvent()
  object ResetJoin2Flow : WorkerJoinSharedEvent()
  object ResetJoin3Flow : WorkerJoinSharedEvent()
  object ResetJoin4Flow : WorkerJoinSharedEvent()
  object ResetJoin5Flow : WorkerJoinSharedEvent()
  object ResetJoin6Flow : WorkerJoinSharedEvent()
  object ClearJobInput : WorkerJoinSharedEvent()
}