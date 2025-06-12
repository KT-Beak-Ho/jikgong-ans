package com.billcorea.jikgong.presentation.company.auth.join.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _navigationEvent = MutableStateFlow<CompanyJoinNavigationEvent?>(null)
    val navigationEvent: StateFlow<CompanyJoinNavigationEvent?> = _navigationEvent.asStateFlow()

    // LiveData
    private val _authCode = MutableLiveData<String?>()
    val authCode: LiveData<String?> = _authCode

    // LiveData 추적
    init {
        // authCode 변경 감지하여 isWaiting 상태 업데이트
        authCode.observeForever { code ->
            if (!code.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(isWaiting = false)
            }
        }
    }

    //  페이지 이동 검증 함수
    private fun canNavigateToNextPage(currentPage: Int): Boolean {
        val state = _uiState.value
        return when (currentPage) {
            1 -> state.isPage1Complete
            // 추후 다른 페이지들 추가
            else -> false
        }
    }

    //  전화번호 양식 검증 함수
    private fun validatePhoneNumber(phoneNumber: String) {
        val errors = _uiState.value.validationErrors.toMutableMap()
        when {
            phoneNumber.isEmpty() -> {
                errors["phoneNumber"] = "기업 전화번호를 입력해주세요"
            }
            !phoneNumber.matches(Regex("^010\\d{8}$")) -> {
                errors["phoneNumber"] = "올바른 기업 전화번호 형식이 아닙니다"
            }
            else -> {
                //  유효하면 에러 제거
                errors.remove("phoneNumber")
                //  전화번호 양식 통과
                _uiState.value = _uiState.value.copy(isValidPhoneNumber = true)
            }
        }
        _uiState.value = _uiState.value.copy(validationErrors = errors)
    }

    //  인증코드 발송 요청
    fun doSmsVerification(phone: String) {
        val smsBody = SmsVerificationRequest(phone)
        RetrofitAPI.create().smsVerification(smsBody).enqueue(object:
            Callback<SmsVerificationResponse> {
            override fun onResponse(
                call: Call<SmsVerificationResponse>,
                response: Response<SmsVerificationResponse>
            ) {
                Log.e("", "response ${response.body()?.data?.authCode}")
                _authCode.value = response.body()?.data?.authCode
                // authCode 값이 설정되면 init의 observer가 자동으로 isWaiting = false 처리
            }
            override fun onFailure(call: Call<SmsVerificationResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
                // 실패 시에도 isWaiting = false로 변경
                _uiState.value = _uiState.value.copy(isWaiting = false)
            }
        })
    }

    //  인증번호 검증 함수
    private fun validateAuthCode(verificationCode: String) {
        val errors = _uiState.value.validationErrors.toMutableMap()
        when {
            verificationCode.isEmpty() -> {
                errors["verificationCode"] = "인증번호를 입력해주세요"
                _uiState.value = _uiState.value.copy(isPhoneVerified = false)
            }
            verificationCode != _authCode.value -> {
                errors["verificationCode"] = "인증번호가 일치하지 않습니다"
                _uiState.value = _uiState.value.copy(isPhoneVerified = false)
            }
            else -> {
                //  유효하면 에러 제거
                errors.remove("verificationCode")
                //  인증번호 일치 === 휴대폰 번호 검증 완료
                _uiState.value = _uiState.value.copy(isPhoneVerified = true)
            }
        }
        _uiState.value = _uiState.value.copy(validationErrors = errors)
    }

    //  전체 이벤트 처리
    fun onEvent(event: CompanyJoinSharedEvent) {
        when (event) {
            //  페이지 뒤로 가기 이벤트
            is CompanyJoinSharedEvent.PreviousPage -> {
                val currentPage = _uiState.value.currentPage
                if (currentPage > 1) {
                    _uiState.value = _uiState.value.copy(currentPage = currentPage - 1)
                    _navigationEvent.value = CompanyJoinNavigationEvent.NavigateToPage(currentPage - 1)
                } else {
                    _navigationEvent.value = CompanyJoinNavigationEvent.NavigateBack
                }
            }
            //  페이지 다음 으로 가기 이벤트
            is CompanyJoinSharedEvent.NextPage -> {
                val currentPage = _uiState.value.currentPage
                if (canNavigateToNextPage(currentPage)) {
                    if (currentPage < 6) {
                        _uiState.value = _uiState.value.copy(currentPage = currentPage + 1)
                        _navigationEvent.value = CompanyJoinNavigationEvent.NavigateToPage(currentPage + 1)
                    }
                }
            }
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
                    isWaiting = true
                )
                // 기존에 받은 인증번호 초기화
                _authCode.value = ""
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
                    phoneNumber = "",               //  전화번호
                 verificationCode = "",          //  인증코드(사용자 입력)
                 isValidPhoneNumber = false,    //  전화번호 양식 일치
                 isPhoneVerified = false,       //  전화번호 검증 완료
                 isSecurityStepActive = false,  //  인증절차 단계
                 isWaiting = false
                )
                _authCode.value = ""
            }
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
            is CompanyJoinSharedEvent.UpdateEmail -> TODO()
            is CompanyJoinSharedEvent.UpdateEmployeeCount -> TODO()
            is CompanyJoinSharedEvent.UpdateEstablishedYear -> TODO()
            is CompanyJoinSharedEvent.UpdateMarketingAgreement -> TODO()
            is CompanyJoinSharedEvent.UpdatePrivacyAgreement -> TODO()
            is CompanyJoinSharedEvent.UpdateRepresentativeName -> TODO()
            is CompanyJoinSharedEvent.UpdateTermsAgreement -> TODO()
            is CompanyJoinSharedEvent.UpdateWebsite -> TODO()
            CompanyJoinSharedEvent.VerifyPhoneNumber -> TODO()
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
        // observeForever 해제
        authCode.removeObserver { }
    }
}