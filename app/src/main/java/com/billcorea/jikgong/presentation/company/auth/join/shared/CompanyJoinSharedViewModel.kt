package com.billcorea.jikgong.presentation.company.auth.join.shared

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.repository.join.JoinRepository
import com.billcorea.jikgong.data.datastore.CompanyDataStore
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants.TOTAL_PAGES
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.billcorea.jikgong.api.service.RetrofitAPI
import com.billcorea.jikgong.api.models.auth.SmsVerificationRequest
import com.billcorea.jikgong.api.models.auth.SmsVerificationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompanyJoinSharedViewModel(
  private val joinRepository: JoinRepository,
  private val context: Context
) : ViewModel() {
  
  private val companyDataStore = CompanyDataStore(context)

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
   * 전화번호 양식 검증 함수 - 다양한 형식 지원
   * 지원 형식: 010-1234-5678, 01012345678, 010 1234 5678 등
   * 010으로 시작하는 번호는 반드시 11자리여야 함
   */
  private fun validatePhoneNumber(phoneNumber: String) {
    val errors = _uiState.value.validationErrors.toMutableMap()
    
    // 숫자만 추출 (하이픈, 공백, 괄호 등 모든 특수문자 제거)
    val digitsOnly = phoneNumber.replace(Regex("[^0-9]"), "")
    
    val isValid = when {
      phoneNumber.isEmpty() -> {
        errors["phoneNumber"] = "휴대폰 번호를 입력해주세요"
        false
      }
      digitsOnly.startsWith("010") && digitsOnly.length != 11 -> {
        errors["phoneNumber"] = "010 번호는 11자리여야 합니다 (현재: ${digitsOnly.length}자리)"
        false
      }
      !digitsOnly.startsWith("010") && digitsOnly.length != 10 && digitsOnly.length != 11 -> {
        errors["phoneNumber"] = "올바른 휴대폰 번호를 입력해주세요"
        false
      }
      digitsOnly.length == 11 && !digitsOnly.startsWith("01") -> {
        errors["phoneNumber"] = "올바른 휴대폰 번호 형식이 아닙니다"
        false
      }
      digitsOnly.length == 10 && !digitsOnly.startsWith("01") -> {
        errors["phoneNumber"] = "올바른 휴대폰 번호 형식이 아닙니다"
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
   * server에 해당 전화번호 존재 유뮤 체크
   */
  private fun checkPhoneNumberRegist(phone: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isWaiting = true)
      
      // 숫자만 추출하여 API 호출
      val cleanPhone = phone.replace(Regex("[^0-9]"), "")
      joinRepository.validatePhone(cleanPhone)
        .onSuccess { response ->
          // 성공 시 전화번호가 사용 가능함을 의미 (서버에 등록되지 않음)
          _uiState.value = _uiState.value.copy(
            isSecurityStepActive = true,
            isPhoneNumberAvailable = true,
            isWaiting = false,
            errorMessage = null
          )
        }
        .onError { error ->
          _uiState.value = _uiState.value.copy(
            isPhoneNumberAvailable = false,
            isWaiting = false,
            isSecurityStepActive=false,
            errorMessage = "네트워크 오류: ${error.message}"
          )
        }
        .onHttpError { code, message, errorBody ->
          val errorMessage = when (code) {
            409 -> "이미 등록된 전화번호입니다"
            400 -> "잘못된 전화번호 형식입니다"
            500 -> "서버 오류가 발생했습니다"
            else -> "HTTP $code: $message"
          }
          _uiState.value = _uiState.value.copy(
            isPhoneNumberAvailable = false,
            isWaiting = false,
            isSecurityStepActive=false,
            errorMessage = errorMessage
          )
        }
    }
  }

  /**
   * 인증코드 발송 요청 - 노동자와 동일한 방식
   */
  private fun doSmsVerification(phone: String) {
    // 숫자만 추출하여 SMS 인증 요청
    val cleanPhone = phone.replace(Regex("[^0-9]"), "")
    val smsBody = SmsVerificationRequest(cleanPhone)
    RetrofitAPI.create().smsVerification(smsBody).enqueue(object :
      Callback<SmsVerificationResponse> {
      override fun onResponse(
        call: Call<SmsVerificationResponse>,
        response: Response<SmsVerificationResponse>
      ) {
        Log.e("CompanyJoin", "SMS response ${response.body()?.data?.authCode}")
        _uiState.value = _uiState.value.copy(
          authCode = response.body()?.data?.authCode,
          isWaiting = false,
          isSecurityStepActive = true,
          errorMessage = null,
          isPhoneNumberAvailable = true
        )
      }

      override fun onFailure(call: Call<SmsVerificationResponse>, t: Throwable) {
        Log.e("CompanyJoin", "SMS error ${t.localizedMessage}")
        // 실패 시에도 isWaiting = false로 변경
        _uiState.value = _uiState.value.copy(
          isWaiting = false,
          errorMessage = "SMS 발송 실패: ${t.localizedMessage}"
        )
      }
    })
  }

  /**
   * 인증번호 검증 함수
   */
  private fun validateAuthCode(verificationCode: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()
    
    // 디버그용 로그
    Log.d("CompanyJoin", "Validating - User input: $verificationCode, Server code: ${currentState.authCode}")

    val isValid = when {
      verificationCode.isEmpty() -> {
        errors["verificationCode"] = "인증번호를 입력해주세요"
        false
      }

      verificationCode != currentState.authCode -> {
        errors["verificationCode"] = "인증번호가 일치하지 않습니다"
        Log.d("CompanyJoin", "Verification failed - codes don't match")
        false
      }

      else -> {
        errors.remove("verificationCode")
        Log.d("CompanyJoin", "Verification successful!")
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
   * server에 해당 ID 존재 유무 체크 - 실제 API 연동 완료
   */
  private fun checkIdRegist(id: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isWaiting = true)

      joinRepository.validateLoginId(id)
        .onSuccess { response ->
          // 성공(200) = ID 사용 가능
          _uiState.value = _uiState.value.copy(
            isIdAvailable = true,
            isWaiting = false,
            errorMessage = null,
            idCheckMessage = "사용 가능한 아이디입니다"
          )
        }
        .onError { error ->
          _uiState.value = _uiState.value.copy(
            isIdAvailable = false,
            isWaiting = false,
            errorMessage = "네트워크 오류: ${error.message}",
            idCheckMessage = null
          )
        }
        .onHttpError { code, message, errorBody ->
          val errorMessage = when (code) {
            409 -> "이미 사용중인 아이디입니다"
            400 -> "아이디 형식이 올바르지 않습니다"
            500 -> "서버 오류가 발생했습니다"
            else -> "오류 발생: $message"
          }
          _uiState.value = _uiState.value.copy(
            isIdAvailable = false,
            isWaiting = false,
            errorMessage = errorMessage,
            idCheckMessage = errorMessage
          )
        }
    }
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
  private fun validateUserPassword(password: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      password.isEmpty() -> {
        errors["password"] = "비밀번호를 입력해주세요"
        false
      }
      password.length < 8 -> {
        errors["password"] = "비밀번호는 8자 이상이어야 합니다"
        false
      }
      else -> {
        errors.remove("password")
        true
      }
    }
    
    // 비밀번호 확인 필드도 재검증
    if (currentState.passwordConfirm.isNotEmpty()) {
      validatePasswordConfirm(currentState.passwordConfirm)
    }
    
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }
  
  /**
   * 비밀번호 확인 검증 함수
   */
  private fun validatePasswordConfirm(passwordConfirm: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()
    
    val isValid = when {
      passwordConfirm.isEmpty() -> {
        errors["passwordConfirm"] = "비밀번호 확인을 입력해주세요"
        false
      }
      passwordConfirm != currentState.password -> {
        errors["passwordConfirm"] = "비밀번호가 일치하지 않습니다"
        false
      }
      else -> {
        errors.remove("passwordConfirm")
        true
      }
    }
    
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * server에 해당 email 존재 유무 체크 ( 수정 필요 )
   */
  private fun checkEmailRegist(email: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isWaiting = true)

      joinRepository.validateEmail(email)
        .onSuccess { response ->
          // 성공(200) = Email 사용 가능
          _uiState.value = _uiState.value.copy(
            isEmailAvailable = true,
            isWaiting = false,
            errorMessage = null,
            emailCheckMessage = "사용 가능한 이메일입니다"
          )
        }
        .onError { error ->
          _uiState.value = _uiState.value.copy(
            isEmailAvailable = false,
            isWaiting = false,
            errorMessage = "네트워크 오류: ${error.message}",
            emailCheckMessage = null
          )
        }
        .onHttpError { code, message, errorBody ->
          val errorMessage = when (code) {
            409 -> "이미 사용중인 이메일입니다"
            422 -> "유효하지 않은 이메일 형식입니다"
            400 -> "잘못된 요청입니다"
            500 -> "서버 오류가 발생했습니다"
            else -> "HTTP $code: $message"
          }
          _uiState.value = _uiState.value.copy(
            isEmailAvailable = false,
            isWaiting = false,
            errorMessage = errorMessage,
            emailCheckMessage = errorMessage
          )
        }
    }
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
   * 사업자 번호 입력 검증 함수 - 다양한 형식 지원
   * 지원 형식: 123-45-67890, 1234567890, 123 45 67890 등
   * 10자리만 확인, 체크섬 검증 없음
   */
  private fun validateBusinessNumber(businessNumber: String) {
    val errors = _uiState.value.validationErrors.toMutableMap()
    
    // 숫자만 추출 (하이픈, 공백, 대시 등 모든 특수문자 제거)
    val digitsOnly = businessNumber.replace(Regex("[^0-9]"), "")
    
    val isValid = when {
      businessNumber.isEmpty() -> {
        errors["businessNumber"] = "사업자 등록번호를 입력해주세요"
        false
      }
      digitsOnly.length != 10 -> {
        errors["businessNumber"] = "사업자 등록번호는 10자리여야 합니다 (현재: ${digitsOnly.length}자리)"
        false
      }
      else -> {
        // 10자리면 바로 통과
        errors.remove("businessNumber")
        true
      }
    }
    _uiState.value = _uiState.value.copy(
      validationErrors = errors,
    )
  }
  
  /**
   * 사업자 번호 체크섬 검증 (대한민국 사업자번호 검증 알고리즘)
   */
  private fun validateBusinessNumberChecksum(businessNumber: String): Boolean {
    if (businessNumber.length != 10) return false
    
    val checkArray = intArrayOf(1, 3, 7, 1, 3, 7, 1, 3, 5)
    var sum = 0
    
    for (i in 0..8) {
      sum += (businessNumber[i] - '0') * checkArray[i]
    }
    
    sum += ((businessNumber[8] - '0') * 5) / 10
    val checkDigit = (10 - (sum % 10)) % 10
    
    return checkDigit == (businessNumber[9] - '0')
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
   * 회원가입 최종 제출 - API 연동
   */
  private fun submitRegistration() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isWaiting = true, errorMessage = null)
      
      val state = _uiState.value
      
      // 회원가입 API 호출 - 숫자만 추출하여 전송
      joinRepository.registerCompany(
        phoneNumber = state.phoneNumber.replace(Regex("[^0-9]"), ""), // 모든 특수문자 제거
        verificationCode = state.verificationCode,
        name = state.name,
        loginId = state.id,
        password = state.password,
        email = state.email,
        businessNumber = state.businessNumber.replace(Regex("[^0-9]"), ""), // 모든 특수문자 제거
        companyName = state.companyName,
        inquiry = state.inquiry.ifEmpty { null }
      ).onSuccess { response ->
        // 회원가입 성공 - CompanyDataStore에 정보 저장
        if (response.success && response.companyId != null) {
          // 회사 정보 저장
          companyDataStore.saveCompanyInfo(
            CompanyDataStore.CompanyInfo(
              companyId = response.companyId,
              companyName = state.companyName,
              businessNumber = state.businessNumber,
              representativeName = state.name,
              businessType = "", // 추후 프로필에서 설정
              businessAddress = "", // 추후 프로필에서 설정
              email = state.email,
              phone = state.phoneNumber,
              loginId = state.id,
              membershipType = "BASIC"
            )
          )
          
          // 토큰 저장 (자동 로그인)
          if (response.accessToken != null && response.refreshToken != null) {
            companyDataStore.saveAuthTokens(
              CompanyDataStore.AuthTokens(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                isLoggedIn = true
              )
            )
          }
          
          // 성공 상태로 업데이트
          _uiState.value = _uiState.value.copy(
            isWaiting = false,
            isRegistrationComplete = true,
            currentPage = 3 // Page3로 이동
          )
          
          // Page3로 네비게이션
          _shouldNavigateToNextPage.value = true
        } else {
          _uiState.value = _uiState.value.copy(
            isWaiting = false,
            errorMessage = response.message ?: "회원가입 실패"
          )
        }
      }.onError { error ->
        _uiState.value = _uiState.value.copy(
          isWaiting = false,
          errorMessage = "네트워크 오류: ${error.message}"
        )
      }.onHttpError { code, message, errorBody ->
        val errorMessage = when (code) {
          400 -> "입력 정보를 확인해주세요"
          409 -> "이미 가입된 정보입니다"
          422 -> "유효하지 않은 정보입니다"
          500 -> "서버 오류가 발생했습니다"
          else -> "회원가입 실패: $message"
        }
        _uiState.value = _uiState.value.copy(
          isWaiting = false,
          errorMessage = errorMessage
        )
      }
    }
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
        val currentPage = _uiState.value.currentPage
        
        // Page2에서 다음 버튼 클릭 시 회원가입 제출
        if (currentPage == 2 && canNavigateToNextPage()) {
          submitRegistration()
        } else if (canNavigateToNextPage()) {
          _uiState.value = _uiState.value.copy(
            currentPage = currentPage + 1
          )
          _shouldNavigateToNextPage.value = true
        }
      }
      /**
       * 회원가입 최종 제출
       */
      is CompanyJoinSharedEvent.SubmitRegistration -> {
        if (_uiState.value.isPage2Complete) {
          submitRegistration()
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
       * 전화 번호 입력 완료 후 인증번호 요청 - 노동자와 동일
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
          verificationCode = event.code,
          isPhoneVerified = false  // 입력 중에는 인증 완료 상태 초기화
        )
      }
      
      /**
       * 인증번호 확인 버튼 클릭
       */
      is CompanyJoinSharedEvent.VerifyCode -> {
        validateAuthCode(_uiState.value.verificationCode)
      }
      /**
       * 1 Page 입력값 초기화
       */
      is CompanyJoinSharedEvent.ResetJoin1Flow -> {
        _uiState.value = _uiState.value.copy(
          phoneNumber = "",               //  전화번호
          verificationCode = "",          //  인증코드(사용자 입력)
          authCode = "",                  //  인증코드(sms 인증으로 받아옴)
          isValidPhoneNumber = false,     //  전화번호 양식 일치
          isPhoneVerified = false,        //  전화번호 검증 완료
          isSecurityStepActive = false,   //  인증절차 단계
          isWaiting = false,              //  현재 상태가 대기상태인가?
          isPhoneNumberAvailable  = false,//  전화번호가 사용가능한가?
          currentPage = 1,                //  현재 페이지 위치 1
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
       * 사용자 아이디 입력 후 등록 여부 확인 - 실제 API 연동
       */
      is CompanyJoinSharedEvent.RequestVerificationID -> {
        // ID 형식 검증 먼저 수행
        if (_uiState.value.id.isEmpty()) {
          _uiState.value = _uiState.value.copy(
            idCheckMessage = "아이디를 입력해주세요",
            isIdAvailable = false
          )
        } else if (_uiState.value.id.length < 4) {
          _uiState.value = _uiState.value.copy(
            idCheckMessage = "아이디는 4자 이상이어야 합니다",
            isIdAvailable = false
          )
        } else {
          // 서버에 중복 확인 요청
          checkIdRegist(_uiState.value.id)
        }
      }
      /**
       * 사용자 비밀번호 입력
       */
      is CompanyJoinSharedEvent.UpdateUserPassword -> {
        _uiState.value = _uiState.value.copy(
          password = event.password
        )
        validateUserPassword(event.password)
      }
      /**
       * 사용자 비밀번호 재입력
       */
      is CompanyJoinSharedEvent.UpdateUserPasswordConfirm -> {
        _uiState.value = _uiState.value.copy(
          passwordConfirm = event.password
        )
        validatePasswordConfirm(event.password)
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
       * 사용자 Email 입력 후 등록 여부 확인 
       * 서버 API 미구현 - 클라이언트 검증만 수행
       */
      is CompanyJoinSharedEvent.RequestVerificationEmail -> {
        val email = _uiState.value.email
        val emailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        
        if (email.isEmpty()) {
          _uiState.value = _uiState.value.copy(
            emailCheckMessage = "이메일을 입력해주세요",
            isEmailAvailable = false
          )
        } else if (!email.matches(emailPattern)) {
          _uiState.value = _uiState.value.copy(
            emailCheckMessage = "올바른 이메일 형식이 아닙니다",
            isEmailAvailable = false
          )
        } else {
          // 서버 API가 없으므로 형식만 검증하고 사용 가능으로 처리
          _uiState.value = _uiState.value.copy(
            emailCheckMessage = "사용 가능한 이메일입니다",
            isEmailAvailable = true
          )
        }
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
          currentPage = 2                //  현재 페이지 위치 2
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
          validationErrors = emptyMap(), //  현재 페이지의 모든 에러 제거
          errorMessage = null
        )
      }
    }
  }
}