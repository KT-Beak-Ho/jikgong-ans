package com.billcorea.jikgong.presentation.worker.auth.join.shared

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.billcorea.jikgong.network.AddressFindResponse
import com.billcorea.jikgong.network.AddressFindRoadAddress
import com.billcorea.jikgong.network.Coord2AddressResponse
import com.billcorea.jikgong.network.Coord2RoadAddress
import com.billcorea.jikgong.network.PhoneValidationRequest
import com.billcorea.jikgong.network.PhoneValidationResponse
import com.billcorea.jikgong.network.RetrofitAPI
import com.billcorea.jikgong.network.SmsVerificationRequest
import com.billcorea.jikgong.network.SmsVerificationResponse
import com.billcorea.jikgong.presentation.worker.auth.common.constants.WorkerJoinConstants.TOTAL_PAGES
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class WorkerJoinSharedViewModel : ViewModel() {
  private val _uiState = MutableStateFlow(WorkerJoinSharedUiState())
  val uiState: StateFlow<WorkerJoinSharedUiState> = _uiState.asStateFlow()

  /**
   * 네비게이션 이벤트
   */
  private val _shouldNavigateToNextPage = MutableStateFlow(false)
  val shouldNavigateToNextPage: StateFlow<Boolean> = _shouldNavigateToNextPage.asStateFlow()
  private val _shouldNavigateBack = MutableStateFlow(false)
  val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack.asStateFlow()
  private val _shouldNavigateHome = MutableStateFlow(false)
  val shouldNavigateHome: StateFlow<Boolean> = _shouldNavigateHome.asStateFlow()

  val bankName = arrayOf("국민은행","신한은행","Kbank", "우리은행", "NH농협은행", "하나은행", "IBK기업은행","카카오뱅크",
    "IM뱅크","토스뱅크","BNK부산은행","SC제일은행","MG새마을금고","우체국","BNK경남은행","광주은행","KDB산업은행","신협","전북은행",
    "씨티뱅크","SH수협은행","제주은행")
  val bankCode = arrayOf("006","021","089","020","011","005","003","090","031","092","032","023","045","071","039","034","002","048","037","027","007","035")

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
      3 to state.isPage3Complete
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
   * 전화번호 중복 확인 요청
   */
  private fun doPhoneValidation(phone: String) {
    val phoneBody = PhoneValidationRequest(phone)
    RetrofitAPI.create().phoneValidation(phoneBody).enqueue(object:
      Callback<PhoneValidationResponse> {
      override fun onResponse(
        request: Call<PhoneValidationResponse>,
        response: Response<PhoneValidationResponse>
      ) {
        Log.e("", "response ${response.body()?.message}")
        if(response.body()?.data == null) {
          _uiState.value = _uiState.value.copy(
            validationPhoneMessage = response.body()?.data?.errorMessage,
            isNotDuplicatedPhone = false,
            isWaiting = false
          )
        }
        else {
          _uiState.value = _uiState.value.copy(
          validationPhoneMessage = response.body()?.message,
          isNotDuplicatedPhone = true,
          isWaiting = false
          )
        }
      }

      override fun onFailure(request: Call<PhoneValidationResponse>, t: Throwable) {
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
   * 생일 선택 확인
   */
  private fun validateBirthday(birthday: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      birthday.isEmpty() -> {
        errors["birthday"] = "생일을 선택해주세요"
        false
      }

      else -> {
        errors.remove("birthday")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 성별 선택 검증
   */
  private fun validateGender(gender: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      gender.isEmpty() -> {
        errors["gender"] = "성별을 선택해주세요"
        false
      }

      else -> {
        errors.remove("gender")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 내, 외국인 선택 검증
   */
  private fun validateNationality(nationality: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      nationality.isEmpty() -> {
        errors["nationality"] = "국적을 선택해주세요"
        false
      }

      else -> {
        errors.remove("nationality")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 예금주 입력 검증
   */
  private fun validateAccountName(accountName: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      accountName.isEmpty() -> {
        errors["accountName"] = "예금주를 입력해주세요"
        false
      }

      else -> {
        errors.remove("accountName")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 은행 선택 검증
   */
  private fun validateBankName(bankName: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      bankName.isEmpty() -> {
        errors["bankName"] = "예금주를 입력해주세요"
        false
      }

      else -> {
        errors.remove("bankName")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  /**
   * 계좌번호 입력 검증
   */
  private fun validateAccountNumber(accountNumber: String) {
    val currentState = _uiState.value
    val errors = currentState.validationErrors.toMutableMap()

    val isValid = when {
      accountNumber.isEmpty() -> {
        errors["accountNumber"] = "예금주를 입력해주세요"
        false
      }

      else -> {
        errors.remove("accountNumber")
        true
      }
    }
    _uiState.value = currentState.copy(
      validationErrors = errors,
    )
  }

  fun doKakaoGeocoding(query: String) {
    Log.e("", "doKakaoGeocoding start ... ")
    _uiState.value = _uiState.value.copy(
      roadAddress = emptyList()
    )
    RetrofitAPI.createKakao().kakaoGeocoding(query).enqueue(object : Callback<AddressFindResponse>{
      override fun onResponse(request: Call<AddressFindResponse>, response: Response<AddressFindResponse>) {
        Log.e("", "response ${response.code()} / ${response.body()?.meta?.totalCount}")
        val recordList = mutableStateListOf<AddressFindRoadAddress>()
        for (document in response.body()?.documents!!) {
          recordList.add(document.roadAddress)
        }
        _uiState.value = _uiState.value.copy(
          roadAddress = recordList.toList()
        )
      }
      override fun onFailure(req: Call<AddressFindResponse>, t: Throwable) {
        Log.e("", "error ${t.localizedMessage}")
      }
    })
  }


  fun doFindAddress(latitude: Double, longitude: Double) {
    Log.e("", "doFindAddress start ... ")
    RetrofitAPI.createKakao().findAddress(latitude, longitude)
      .enqueue(object : Callback<Coord2AddressResponse> {
        override fun onResponse(
          call: Call<Coord2AddressResponse>,
          resp: Response<Coord2AddressResponse>
        ) {
          if (resp.isSuccessful) {
            val recordList = mutableStateListOf<Coord2RoadAddress>()
            try {
              for (document in resp.body()?.documents!!) {
                Log.e("", "findAddress ${document.roadAddress.addressName}")
                recordList.add(document.roadAddress)
              }
              _uiState.value = _uiState.value.copy(
                roadAddress1 = recordList.toList()
              )

            } catch (e: Exception) {
              Log.e("", "error ${e.localizedMessage}")
            }
          }
        }
        override fun onFailure(call: Call<Coord2AddressResponse>, t: Throwable) {
          Log.e("", "error ${t.localizedMessage}")
        }
      })
  }

  fun getJobNameByCode(jobCode: String): String {
    val jobNames = arrayOf("보통인부", "작업반장", "특별인부", "조력공", "비계공", "형틀목공", "철근공",
      "철골공", "용접공", "콘크리트공", "조적공", "견출공", "건축목공", "창호공", "유리공",
      "방수공", "미장공", "타일공", "도장공", "내장공", "도배공", "연마공", "석공",
      "줄눈공", "판넬조립공", "지붕잇기공", "조경공", "코킹공", "배관공", "보일러공",
      "위생공", "덕트공", "보온공", "기계설비공", "내선진공", "통신내선공", "통신설비공")

    val jobCodes = arrayOf("NORMAL", "FOREMAN", "SKILLED_LABORER", "HELPER", "SCAFFOLDER",
      "FORMWORK_CARPENTER", "REBAR_WORKER", "STEEL_STRUCTURE", "WELDER",
      "CONCRETE_WORKER", "BRICKLAYER", "DRYWALL_FINISHER", "CONSTRUCTION_CARPENTER",
      "WINDOW_DOOR_INSTALLER", "GLAZIER", "WATERPROOFING_WORKER", "PLASTERER",
      "TILE", "PAINTER", "INTERIOR_FINISHER", "WALLPAPER_INSTALLER", "POLISHER",
      "STONEMASON", "GROUT_WORKER", "PANEL_ASSEMBLER", "ROOFER", "LANDSCAPER",
      "CAULKER", "PLUMBER", "BOILER_TECHNICIAN", "SANITARY_TECHNICIAN",
      "DUCT_INSTALLER", "INSULATION_WORKER", "MECHANICAL_EQUIPMENT_TECHNICIAN",
      "ELECTRICIAN", "TELECOMMUNICATIONS_INSTALLER", "TELECOMMUNICATIONS_EQUIPMENT_INSTALLER")

    val index = jobCodes.indexOf(jobCode)
    return if (index != -1) jobNames[index] else "알 수 없는 직종"
  }

  fun submitWorkerRegistration() {
    val state = _uiState.value
    // MainViewModel의 doRegisterWorker 메서드 호출 로직 구현
    // 성공 시 홈으로 이동
    _shouldNavigateHome.value = true
  }






  /**
   * 전체 이벤트 처리
   */
  fun onEvent(event: WorkerJoinSharedEvent) {
    when (event) {
      /**
       * 공통 이벤트
       */
      /**
       * 페이지 뒤로 가기
       */
      is WorkerJoinSharedEvent.PreviousPage -> {
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
      is WorkerJoinSharedEvent.NextPage -> {
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
      is WorkerJoinSharedEvent.HomePage -> {
        val currentPage = _uiState.value.currentPage
        _shouldNavigateHome.value = currentPage == TOTAL_PAGES
      }
      /**
       * Page 1 (CompanyJoinPage1Screen) 이벤트
       */
      /**
       * 전화 번호 입력시 실시간 업데이트
       */
      is WorkerJoinSharedEvent.UpdatePhoneNumber -> {
        _uiState.value = _uiState.value.copy(
          phoneNumber = event.phoneNumber,
          isValidPhoneNumber = false,        // 번호 양식 일치 초기화
          isPhoneVerified = false,           // 번호 변경 시 인증 초기화
          isSecurityStepActive = false,      // 인증 단계 비활성화
          verificationCode = "",             // 기존 인증번호 초기화
          validationPhoneMessage = "",       // 전화번호 중복 확인 메세지 초기화
          isNotDuplicatedPhone = false       // 전화번호 중복 확인 초기화
        )
        // 실시간 유효성 검증
        validatePhoneNumber(event.phoneNumber)
      }
      /**
       * 전화 번호 입력 완료 후 인증번호 요청
       */
      is WorkerJoinSharedEvent.RequestVerificationCode -> {
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
      is WorkerJoinSharedEvent.UpdateVerificationCode -> {
        _uiState.value = _uiState.value.copy(
          verificationCode = event.code
        )
        //  실시간 검증
        validateAuthCode(event.code)
      }
      /**
       * 1 Page 입력값 초기화
       */
      is WorkerJoinSharedEvent.ResetJoin1Flow -> {
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
      is WorkerJoinSharedEvent.UpdateUserName -> {
        _uiState.value = _uiState.value.copy(
          name = event.name
        )
        validateUserName(event.name)
      }
      /**
       * 사용자 아이디 입력
       */
      is WorkerJoinSharedEvent.UpdateUserId -> {
        _uiState.value = _uiState.value.copy(
          id = event.id
        )
        validateUserId(event.id)
      }
      /**
       * 사용자 비밀번호 입력
       */
      is WorkerJoinSharedEvent.UpdateUserPassword -> {
        _uiState.value = _uiState.value.copy(
          password = event.password
        )
        validateUserPwd(event.password)
      }
      /**
       * 사용자 비밀번호 재입력
       */
      is WorkerJoinSharedEvent.UpdateUserPasswordConfirm -> {
        _uiState.value = _uiState.value.copy(
          passwordConfirm = event.password
        )
      }
      /**
       * 사용자 Email 입력
       */
      is WorkerJoinSharedEvent.UpdateUserMail -> {
        _uiState.value = _uiState.value.copy(
          email = event.email
        )
        validateEmail(event.email)
      }
      /**
       * 사용자 생일
       */
      is WorkerJoinSharedEvent.UpdateBirthday -> {
        _uiState.value = _uiState.value.copy(
          birthday = event.birthday
        )
        validateBirthday(event.birthday)
      }
      /**
       * 성별 입력
       */
      is WorkerJoinSharedEvent.UpdateGender -> {
        _uiState.value = _uiState.value.copy(
          gender = event.gender
        )
        validateGender(event.gender)
      }
      /**
       * 내, 외국인 확인
       */
      is WorkerJoinSharedEvent.UpdateNationality -> {
        _uiState.value = _uiState.value.copy(
          nationality = event.nationality
        )
        validateNationality(event.nationality)
      }
      /**
       * 2 Page 입력값 초기화
       */
      is WorkerJoinSharedEvent.ResetJoin2Flow -> {
        _uiState.value = _uiState.value.copy(
          id = "",
          password = "",
          passwordConfirm = "",
          email = "",
          name = "",
          gender = "",
          nationality = "",
          birthday = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(System.currentTimeMillis()),
          currentPage = 2                //  현재 페이지 위치 2
        )
      }
      /**
       * Page 3 (CompanyJoinPage3Screen) 이벤트
       */
      is WorkerJoinSharedEvent.ResetJoin3Flow -> {
        _uiState.value = _uiState.value.copy(
          accountName = "",
          bankName = "",
          accountNumber = "",
          currentPage = 3                //  현재 페이지 위치 3
        )
      }
      /**
       * 예금주 입력
       */
      is WorkerJoinSharedEvent.UpdateAccountName -> {
        _uiState.value = _uiState.value.copy(
          accountName = event.accountName
        )
        validateAccountName(event.accountName)
      }
      /**
       * 은행 선택
       */
      is WorkerJoinSharedEvent.UpdateBankName -> {
        _uiState.value = _uiState.value.copy(
          bankName = event.bankName
        )
        validateBankName(event.bankName)
      }
      /**
       * 계좌번호 입력
       */
      is WorkerJoinSharedEvent.UpdateAccountNumber -> {
        _uiState.value = _uiState.value.copy(
          accountNumber = event.accountNumber
        )
        validateAccountNumber(event.accountNumber)
      }

      /**
       * Page 4: 주소
       */
      is WorkerJoinSharedEvent.UpdateAddress -> {
        _uiState.value = _uiState.value.copy(
          address = event.address
        )
      }

      /**
       * Page 4: 주소 이름
       */
      is WorkerJoinSharedEvent.UpdateAddressName -> {
        _uiState.value = _uiState.value.copy(
          addressName = event.addressName
        )
      }

      /**
       * Page 4: 응답 주소
       */
      is WorkerJoinSharedEvent.UpdateRespAddress -> {
        _uiState.value = _uiState.value.copy(
          respAddress = event.respAddress
        )
      }

      /**
       * lat
       */
      is WorkerJoinSharedEvent.UpdateLat -> {
        _uiState.value = _uiState.value.copy(
          lat = event.lat
        )
      }

      /**
       * lon
       */
      is WorkerJoinSharedEvent.UpdateLon -> {
        _uiState.value = _uiState.value.copy(
          lon = event.lon
        )
      }

      /**
       * 카카오 api 실행
       */
      is WorkerJoinSharedEvent.KakaoGeocoding -> {
        /* _uiState.value = _uiState.value.copy(
        ) */
        doKakaoGeocoding(_uiState.value.addressName)
      }

      /**
       * 4 Page 입력값 초기화
       */
      is WorkerJoinSharedEvent.ResetJoin4Flow -> {
        _uiState.value = _uiState.value.copy(
          address = "",              //  주소
          addressName = "",          //  입력값
          roadAddress = emptyList(),  // 도로명 주소 정보 리스트
          lat = 0.0,                 //  위도
          lon = 0.0,                 //  경도
          currentPage = 4                //  현재 페이지 위치 1
        )
      }

      /**
       * Page 5 입력값 초기화 - 수정된 버전
       */
      is WorkerJoinSharedEvent.ResetJoin5Flow -> {
        _uiState.value = _uiState.value.copy(
          educationCertificateUri = "",
          workerCardUri = "",
          showBottomSheet = false,
          showLaterDialog = false,
          currentPhotoPath = "",
          takePicType = "",
          isGrantCamera = false,
          currentPage = 5
        )
      }

      /**
       * 교육자격증 URI 업데이트
       */
      is WorkerJoinSharedEvent.UpdateEducationCertificateUri -> {
        _uiState.value = _uiState.value.copy(
          educationCertificateUri = event.educationCertificateUri
        )
      }

      /**
       * 근로자증 URI 업데이트
       */
      is WorkerJoinSharedEvent.UpdateWorkerCardUri -> {
        _uiState.value = _uiState.value.copy(
          workerCardUri = event.workerCardUri
        )
      }

      /**
       * 바텀시트 표시 상태 업데이트
       */
      is WorkerJoinSharedEvent.UpdateShowBottomSheet -> {
        _uiState.value = _uiState.value.copy(
          showBottomSheet = event.showBottomSheet
        )
      }

      /**
       * "나중에 하기" 다이얼로그 표시 상태 업데이트
       */
      is WorkerJoinSharedEvent.UpdateShowLaterDialog -> {
        _uiState.value = _uiState.value.copy(
          showLaterDialog = event.showLaterDialog
        )
      }

      /**
       * 현재 사진 경로 업데이트
       */
      is WorkerJoinSharedEvent.UpdateCurrentPhotoPath -> {
        _uiState.value = _uiState.value.copy(
          currentPhotoPath = event.currentPhotoPath
        )
      }

      /**
       * 사진 촬영 타입 업데이트
       */
      is WorkerJoinSharedEvent.UpdateTakePicType -> {
        _uiState.value = _uiState.value.copy(
          takePicType = event.takePicType
        )
      }

      /**
       * 카메라 권한 승인 상태 업데이트
       */
      is WorkerJoinSharedEvent.UpdateIsGrantCamera -> {
        _uiState.value = _uiState.value.copy(
          isGrantCamera = event.isGrantCamera
        )
      }

      // Page 6 이벤트 처리
      is WorkerJoinSharedEvent.UpdateSelectedJob -> {
        _uiState.value = _uiState.value.copy(
          selectedJobCode = event.jobCode,
          selectedJobName = event.jobName
        )
      }

      is WorkerJoinSharedEvent.UpdateYearInput -> {
        _uiState.value = _uiState.value.copy(
          yearInput = event.year
        )
      }

      is WorkerJoinSharedEvent.UpdateMonthInput -> {
        _uiState.value = _uiState.value.copy(
          monthInput = event.month
        )
      }

      is WorkerJoinSharedEvent.AddWorkExperience -> {
        val currentList = _uiState.value.workExperienceList.toMutableList()
        val existingIndex = currentList.indexOfFirst { it.tech == event.workExperience.tech }

        if (existingIndex != -1) {
          // 기존 tech가 있으면 experienceMonths를 더함
          val existingExperience = currentList[existingIndex]
          val updatedExperience = existingExperience.copy(
            experienceMonths = existingExperience.experienceMonths + event.workExperience.experienceMonths
          )
          currentList[existingIndex] = updatedExperience
        } else {
          // 없으면 새로 추가
          currentList.add(event.workExperience)
        }

        _uiState.value = _uiState.value.copy(
          workExperienceList = currentList
        )
      }

      is WorkerJoinSharedEvent.RemoveWorkExperience -> {
        val currentList = _uiState.value.workExperienceList.toMutableList()
        currentList.remove(event.workExperience)
        _uiState.value = _uiState.value.copy(
          workExperienceList = currentList
        )
      }

      is WorkerJoinSharedEvent.ClearJobInput -> {
        _uiState.value = _uiState.value.copy(
          selectedJobCode = "",
          selectedJobName = "직종을 선택해주세요",
          yearInput = "",
          monthInput = ""
        )
      }

      is WorkerJoinSharedEvent.ResetJoin6Flow -> {
        _uiState.value = _uiState.value.copy(
          yearInput = "",
          monthInput = "",
          selectedJobCode = "",
          selectedJobName = "직종을 선택해주세요",
          workExperienceList = emptyList(),
          isRegistrationInProgress = false,
          currentPage = 6
        )
      }

      is WorkerJoinSharedEvent.SubmitRegistration -> {
        _uiState.value = _uiState.value.copy(
          isRegistrationInProgress = true
        )
        // 실제 회원가입 API 호출
        submitWorkerRegistration()
      }

      /**
       * 공통
       */
      /**
       * 에러 초기화
       */
      WorkerJoinSharedEvent.ClearError -> {
        _uiState.value = _uiState.value.copy(
          validationErrors = emptyMap() //  현재 페이지의 모든 에러 제거
        )
      }
    }
  }
}

