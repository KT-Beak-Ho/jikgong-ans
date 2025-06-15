package com.billcorea.jikgong.presentation.company.auth.join.shared

import androidx.lifecycle.ViewModel
import com.billcorea.jikgong.network.RetrofitAPI
import com.billcorea.jikgong.network.SmsVerificationRequest
import com.billcorea.jikgong.network.SmsVerificationResponse
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants.TOTAL_PAGES
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyJoinSharedViewModel : ViewModel() {

  private val _uiState = MutableStateFlow(CompanyJoinSharedUiState())
  val uiState: StateFlow<CompanyJoinSharedUiState> = _uiState.asStateFlow()

  /**
   * 네비게이션 이벤트
   */
  private val _shouldNavigateToNextPage = MutableStateFlow(false)
  val shouldNavigateToNextPage: StateFlow<Boolean> = _shouldNavigateToNextPage.asStateFlow()
  private val _shouldNavigateBack = MutableStateFlow(false)
  val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack.asStateFlow()
  private val _shouldNavigateHome = MutableStateFlow(false)
  val shouldNavigateHome: StateFlow<Boolean> = _shouldNavigateHome.asStateFlow()

  /**
   * 네비게이션 이벤트 클리어
   */
  fun clearNavigationEvents() {
    _shouldNavigateToNextPage.value = false
    _shouldNavigateBack.value = false
    _shouldNavigateHome.value = false
  }

  /**
   * 다음 페이지 이동 검증 함수
   */
  private fun canNavigateToNextPage(): Boolean {
    val state = _uiState.value
    val currentPage = state.currentPage

    val pageCompletionMap = mapOf(
      1 to state.isPage1Complete,
      2 to state.isPage2Complete,
    )
    return pageCompletionMap[currentPage] ?: false
  }

  /**
   * 전화번호 양식 검증 함수
   */
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

  /**
   * 인증코드 발송 요청
   */
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

  /**
   * 인증번호 검증 함수
   */
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

  /**
   * 사용자 이름 입력 검증 함수
   */
  private fun validateUserName(name: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      name.isEmpty() -> {
        errors["name"] = "이름을 입력해주세요"
        false
      }

      else -> {
        errors.remove("name")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 아이디 입력 검증 함수
   */
  private fun validateUserId(id: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      id.isEmpty() -> {
        errors["id"] = "아이디를 입력해주세요"
        false
      }

      else -> {
        errors.remove("id")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 비밀번호 입력 검증 함수
   */
  private fun validateUserPwd(pwd: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      pwd.isEmpty() -> {
        errors["pwd"] = "비밀번호를 입력해주세요"
        false
      }

      else -> {
        errors.remove("pwd")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 사업자 이메일 입력 검증 함수
   */
  private fun validateEmail(email: String) {
    val errors = _uiState.value.validationErrors.toMutableMap()
    val isValid = when {
      email.isEmpty() -> {
        errors["email"] = "이메일을 입력해주세요"
        false
      }

      !email.matches(Regex("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) -> {
        errors["email"] = "올바른 이메일 형식이 아닙니다"
        false
      }

      else -> {
        errors.remove("email")
        true
      }
    }
    _uiState.value = _uiState.value.copy(
      validationErrors = errors,
    )
  }

  /**
   * 사업자 번호 임력 검증 함수
   */
  private fun validateBusinessNumber(businessNumber: String) {
    val errors = _uiState.value.validationErrors.toMutableMap()
    val isValid = when {
      businessNumber.isEmpty() -> {
        errors["businessNumber"] = "사업자 등록번호를 입력해주세요"
        false
      }

      !businessNumber.matches(Regex("^\\d{3}-\\d{2}-\\d{5}$")) -> {
        errors["businessNumber"] = "올바른 사업자 등록번호 형식이 아닙니다"
        false
      }

      else -> {
        errors.remove("businessNumber")
        true
      }
    }
    _uiState.value = _uiState.value.copy(
      validationErrors = errors,
    )
  }

  /**
   * 회사명 입력 검증 함수
   */
  private fun UpdateCompanyName(companyName: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      companyName.isEmpty() -> {
        errors["companyName"] = "회사명을 입력해주세요"
        false
      }

      else -> {
        errors.remove("companyName")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 전체 이벤트 처리
   */
  fun onEvent(event: CompanyJoinSharedEvent) {
    when (event) {
      /**
       * 공통 이벤트
       */
      /**
       * 페이지 뒤로 가기
       */
      is CompanyJoinSharedEvent.PreviousPage -> {
        val currentPage = _uiState.value.currentPage
        //  현재 page가 1 page 이상인 경우에만 뒤로가기 가능
        _uiState.value = _uiState.value.copy(
          currentPage = currentPage - 1
        )
        _shouldNavigateBack.value = currentPage > 0
      }
      /**
       * 페이지 다음 으로 가기
       */
      is CompanyJoinSharedEvent.NextPage -> {
        if (canNavigateToNextPage()) {
          val currentPage = _uiState.value.currentPage
          _uiState.value = _uiState.value.copy(
            currentPage = currentPage + 1
          )
          _shouldNavigateToNextPage.value = true
        }
      }
      /**
       * main 페이지로 돌아가기
       */
      is CompanyJoinSharedEvent.HomePage -> {
        val currentPage = _uiState.value.currentPage
        _shouldNavigateHome.value = currentPage == TOTAL_PAGES
      }
      /**
       * Page 1 (CompanyJoinPage1Screen) 이벤트
       */
      /**
       * 전화 번호 입력시 실시간 업데이트
       */
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
      /**
       * 전화 번호 입력 완료 후 인증번호 요청
       */
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
      /**
       * 인증번호 입력
       */
      is CompanyJoinSharedEvent.UpdateVerificationCode -> {
        _uiState.value = _uiState.value.copy(
          verificationCode = event.code
        )
        //  실시간 검증
        validateAuthCode(event.code)
      }
      /**
       * 1 Page 입력값 초기화
       */
      is CompanyJoinSharedEvent.ResetJoin1Flow -> {
        _uiState.value = _uiState.value.copy(
          phoneNumber = "",              //  전화번호
          verificationCode = "",         //  인증코드(사용자 입력)
          authCode = "",                 //  인증코드(sms 인증으로 받아옴)
          isValidPhoneNumber = false,    //  전화번호 양식 일치
          isPhoneVerified = false,       //  전화번호 검증 완료
          isSecurityStepActive = false,  //  인증절차 단계
          isWaiting = false,
          currentPage = 1                //  현재 페이지 위치 1
        )
      }
      /**
       * Page 2 (CompanyJoinPage2Screen) 이벤트
       */
      /**
       * 사업자 이름 입력
       */
      is CompanyJoinSharedEvent.UpdateUserName -> {
        _uiState.value = _uiState.value.copy(
          name = event.name
        )
        validateUserName(event.name)
      }
      /**
       * 사용자 아이디 입력
       */
      is CompanyJoinSharedEvent.UpdateUserId -> {
        _uiState.value = _uiState.value.copy(
          id = event.id
        )
        validateUserId(event.id)
      }
      /**
       * 사용자 비밀번호 입력
       */
      is CompanyJoinSharedEvent.UpdateUserPassword -> {
        _uiState.value = _uiState.value.copy(
          password = event.password
        )
        validateUserPwd(event.password)
      }
      /**
       * 사용자 비밀번호 재입력
       */
      is CompanyJoinSharedEvent.UpdateUserPasswordConfirm -> {
        _uiState.value = _uiState.value.copy(
          passwordConfirm = event.password
        )
      }
      /**
       * 사용자 Email 입력
       */
      is CompanyJoinSharedEvent.UpdateUserMail -> {
        _uiState.value = _uiState.value.copy(
          email = event.email
        )
        validateEmail(event.email)
      }
      /**
       * 사업자등록번호
       */
      is CompanyJoinSharedEvent.UpdateBusinessNumber -> {
        _uiState.value = _uiState.value.copy(
          businessNumber = event.businessNumber
        )
        //  실시간 검증
        validateBusinessNumber(event.businessNumber)
      }
      /**
       * 회사명 입력
       */
      is CompanyJoinSharedEvent.UpdateCompanyName -> {
        _uiState.value = _uiState.value.copy(
          companyName = event.companyName
        )
        UpdateCompanyName(event.companyName)
      }
      /**
       * 문의사항
       */
      is CompanyJoinSharedEvent.UpdateInquiry -> {
        _uiState.value = _uiState.value.copy(
          inquiry = event.inquiry
        )
      }
      /**
       * 2 Page 입력값 초기화
       */
      is CompanyJoinSharedEvent.ResetJoin2Flow -> {
        _uiState.value = _uiState.value.copy(
          currentPage = 1                //  현재 페이지 위치 2
        )
      }
      /**
       * Page 3 (CompanyJoinPage3Screen) 이벤트
       */
      is CompanyJoinSharedEvent.ResetJoin3Flow -> {
        _uiState.value = _uiState.value.copy(
          currentPage = 3                //  현재 페이지 위치 3
        )
      }
      /**
       * 공통
       */
      /**
       * 에러 초기화
       */
      CompanyJoinSharedEvent.ClearError -> {
        _uiState.value = _uiState.value.copy(
          validationErrors = emptyMap() //  현재 페이지의 모든 에러 제거
        )
      }
    }
  }
}