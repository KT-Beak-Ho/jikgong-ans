package com.billcorea.jikgong.presentation.company.auth.join.shared

import androidx.lifecycle.ViewModel
import com.billcorea.jikgong.network.RetrofitAPI
import com.billcorea.jikgong.network.SmsVerificationRequest
import com.billcorea.jikgong.network.SmsVerificationResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyJoinSharedViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyJoinSharedUiState())
  val uiState: StateFlow<CompanyJoinSharedUiState> = _uiState.asStateFlow()

  // 네비게이션 이벤트
  private val _shouldNavigateToNextPage = MutableStateFlow(false)
  val shouldNavigateToNextPage: StateFlow<Boolean> = _shouldNavigateToNextPage.asStateFlow()
  private val _shouldNavigateBack = MutableStateFlow(false)
  val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack.asStateFlow()

  //  페이지 이동 검증 함수
  private fun canNavigateToNextPage(): Boolean {
    return _uiState.value.isPage1Complete
  }

  //  전화번호 양식 검증 함수
  private fun validatePhoneNumber(phoneNumber: String) {
    val errors = _uiState.value.validationErrors.toMutableMap()
    val isValid = when {
      phoneNumber.isEmpty() -> {
        errors["phoneNumber"] = "기업 전화번호를 입력해주세요"
        false
      }

      !phoneNumber.matches(Regex("^010\\d{8}$")) -> {
        errors["phoneNumber"] = "올바른 기업 전화번호 형식이 아닙니다"
        false
      }

      else -> {
        errors.remove("phoneNumber")
        true
      }
    }

    _uiState.value = _uiState.value.copy(
      validationErrors = errors,
      isValidPhoneNumber = isValid
    )
  }

  //  인증코드 발송 요청
  private fun doSmsVerification(phone: String) {
    val smsBody = SmsVerificationRequest(phone)
    RetrofitAPI.create().smsVerification(smsBody).enqueue(object :
      Callback<SmsVerificationResponse> {
      override fun onResponse(
        call: Call<SmsVerificationResponse>,
        response: Response<SmsVerificationResponse>
      ) {
        //  Log.e("", "response ${response.body()?.data?.authCode}")
        _uiState.value = _uiState.value.copy(
          authCode = response.body()?.data?.authCode,
          isWaiting = false
        )
      }

      override fun onFailure(call: Call<SmsVerificationResponse>, t: Throwable) {
        // Log.e("", "error ${t.localizedMessage}")
        // 실패 시에도 isWaiting = false로 변경
        _uiState.value = _uiState.value.copy(isWaiting = false)
      }
    })
  }

  //  인증번호 검증 함수
  private fun validateAuthCode(verificationCode: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      verificationCode.isEmpty() -> {
        errors["verificationCode"] = "인증번호를 입력해주세요"
        false
      }

      verificationCode != currentState.authCode -> {
        errors["verificationCode"] = "인증번호가 일치하지 않습니다"
        false
      }

      else -> {
        errors.remove("verificationCode")
        true
      }
    }

    _uiState.value = currentState.copy(
      validationErrors = errors,
      isPhoneVerified = isValid
    )
  }

  //  전체 이벤트 처리
  fun onEvent(event: CompanyJoinSharedEvent) {
    when (event) {
      /**
       * 공통 이벤트
       */
      //  페이지 뒤로 가기
      is CompanyJoinSharedEvent.PreviousPage -> {
//                val currentPage = _uiState.value.currentPage
//                if (currentPage > 1) {
//                    _uiState.value = _uiState.value.copy(currentPage = currentPage - 1)
//                    _navigationEvent.value =
//                        CompanyJoinNavigationEvent.NavigateToPage(currentPage - 1)
//                } else {
//                    _navigationEvent.value = CompanyJoinNavigationEvent.NavigateBack
//                }
        _shouldNavigateBack.value = true
      }
      //  페이지 다음 으로 가기
      is CompanyJoinSharedEvent.NextPage -> {
//                val currentPage = _uiState.value.currentPage
//                if (canNavigateToNextPage(currentPage)) {
//                    if (currentPage < 6) {
//                        _uiState.value = _uiState.value.copy(currentPage = currentPage + 1)
//                        _navigationEvent.value =
//                            CompanyJoinNavigationEvent.NavigateToPage(currentPage + 1)
//                    }
//                }
        if (canNavigateToNextPage()) {
          _shouldNavigateToNextPage.value = true
        }
      }
      /**
       * Page 1 (CompanyJoinPage1Screen) 이벤트
       */
      //  전화 번호 입력시 실시간 업데이트
      is CompanyJoinSharedEvent.UpdatePhoneNumber -> {
        _uiState.value = _uiState.value.copy(
          phoneNumber = event.phoneNumber,
          isValidPhoneNumber = false,        // 번호 양식 일치 초기화
          isPhoneVerified = false,           // 번호 변경 시 인증 초기화
          isSecurityStepActive = false,      // 인증 단계 비활성화
          verificationCode = ""              // 기존 인증번호 초기화
        )
        // 실시간 유효성 검증
        validatePhoneNumber(event.phoneNumber)
      }
      //  전화 번호 입력 완료 후 인증번호 요청
      is CompanyJoinSharedEvent.RequestVerificationCode -> {
        // 인증 절차넘어감 + 인증번호 받기버튼 로딩상태 변경
        _uiState.value = _uiState.value.copy(
          isSecurityStepActive = true,
          isWaiting = true,
          authCode = "" // 기존에 받은 인증번호 초기화
        )
        // 인증번호 요청
        doSmsVerification(_uiState.value.phoneNumber)
      }
      //  인증번호 입력
      is CompanyJoinSharedEvent.UpdateVerificationCode -> {
        _uiState.value = _uiState.value.copy(
          verificationCode = event.code
        )
        //  실시간 검증
        validateAuthCode(event.code)
      }
      //  1 Page 입력값 초기화
      is CompanyJoinSharedEvent.ResetJoin1Flow -> {
        _uiState.value = _uiState.value.copy(
          phoneNumber = "",              //  전화번호
          verificationCode = "",         //  인증코드(사용자 입력)
          authCode = "",                 //  인증코드(sms 인증으로 받아옴)
          isValidPhoneNumber = false,    //  전화번호 양식 일치
          isPhoneVerified = false,       //  전화번호 검증 완료
          isSecurityStepActive = false,  //  인증절차 단계
          isWaiting = false
        )
      }
      /**
       * Page 2 (CompanyJoinPage2Screen) 이벤트
       */
      //  사업자 이름 입력
      is CompanyJoinSharedEvent.UpdateUserName -> {
        _uiState.value = _uiState.value.copy(
          name = event.name
        )
      }
      //  사용자 아이디 입력
      is CompanyJoinSharedEvent.UpdateUserId -> {
        _uiState.value = _uiState.value.copy(
          id = event.id
        )
      }
      //  사용자 비밀번호 입력
      is CompanyJoinSharedEvent.UpdateUserPassword -> {
        _uiState.value = _uiState.value.copy(
          password = event.pwd
        )
      }
      //  사용자 비밀번호 입력 검증
      is CompanyJoinSharedEvent.UpdateUserPasswordConfirm -> {
        _uiState.value = _uiState.value.copy(
          passwordConfirm = event.pwd
        )
      }
      //  사용자 Email 입력
      is CompanyJoinSharedEvent.UpdateUserMail -> {
        _uiState.value = _uiState.value.copy(
          email = event.email
        )
      }
      //  사업자등록번호
      is CompanyJoinSharedEvent.UpdateBusinessNumber -> {
        _uiState.value = _uiState.value.copy(
          businessNumber = event.businessNumber
        )
      }
      //  회사명 입력
      is CompanyJoinSharedEvent.UpdateCompanyName -> {
        _uiState.value = _uiState.value.copy(
          companyName = event.companyName
        )
      }
      //  문의사항
      is CompanyJoinSharedEvent.UpdateInquiry -> {
        _uiState.value = _uiState.value.copy(
          inquiry = event.inquiry
        )
      }
      //  2 Page 입력값 초기화
      is CompanyJoinSharedEvent.ResetJoin2Flow -> {}
      /**
       * Page 3 (CompanyJoinPage2Screen) 이벤트
       */
      /**
       * Page 4 (CompanyJoinPage2Screen) 이벤트
       */
      /**
       * Page 5 (CompanyJoinPage2Screen) 이벤트
       */
      /**
       * Page 6 (CompanyJoinPage2Screen) 이벤트
       */
      CompanyJoinSharedEvent.ClearError -> TODO()
      is CompanyJoinSharedEvent.NavigateToPage -> TODO()
      CompanyJoinSharedEvent.SearchCompanyAddress -> TODO()
      CompanyJoinSharedEvent.SubmitCompanyJoinData -> TODO()
      is CompanyJoinSharedEvent.UpdateBusinessNumber -> TODO()
      is CompanyJoinSharedEvent.UpdateBusinessType -> TODO()
      is CompanyJoinSharedEvent.UpdateCompanyAddress -> TODO()
      is CompanyJoinSharedEvent.UpdateCompanyDescription -> TODO()
      is CompanyJoinSharedEvent.UpdateCompanyName -> TODO()
      is CompanyJoinSharedEvent.UpdateDetailAddress -> TODO()
      is CompanyJoinSharedEvent.UpdateEmployeeCount -> TODO()
      is CompanyJoinSharedEvent.UpdateEstablishedYear -> TODO()
      is CompanyJoinSharedEvent.UpdateMarketingAgreement -> TODO()
      is CompanyJoinSharedEvent.UpdatePrivacyAgreement -> TODO()
      is CompanyJoinSharedEvent.UpdateTermsAgreement -> TODO()
      is CompanyJoinSharedEvent.UpdateWebsite -> TODO()
      CompanyJoinSharedEvent.VerifyPhoneNumber -> TODO()
    }
  }

  // 네비게이션 이벤트 클리어
  fun clearNavigationEvents() {
    _shouldNavigateToNextPage.value = false
    _shouldNavigateBack.value = false
  }
}