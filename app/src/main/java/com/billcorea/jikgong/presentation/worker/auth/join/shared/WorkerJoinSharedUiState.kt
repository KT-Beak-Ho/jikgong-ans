package com.billcorea.jikgong.presentation.worker.auth.join.shared

import com.billcorea.jikgong.network.location.AddressFindRoadAddress
import com.billcorea.jikgong.network.location.Coord2RoadAddress
import com.billcorea.jikgong.network.auth.WorkExperience
import java.text.SimpleDateFormat
import java.util.Locale

data class WorkerJoinSharedUiState(
  // Page 1: 전화 번호 인증 및 중복 확인
  val phoneNumber: String = "",               //  전화번호
  val verificationCode: String = "",          //  인증코드(사용자 입력)
  val authCode: String? = "",                 //  인증코드(sms 인증으로 받아옴)
  val isValidPhoneNumber: Boolean = false,    //  전화번호 양식 일치
  val isPhoneVerified: Boolean = false,       //  전화번호 검증 완료
  val isSecurityStepActive: Boolean = false,  //  인증절차 단계
  val isWaiting: Boolean = false,             //  인증 진행중

  val validationPhoneMessage: String? = "",   //  전화번호 중복 확인 메세지
  val isNotDuplicatedPhone: Boolean = false,  //  전화번호 중복 확인 완료

  // Page 2: 기본 정보
  val name: String = "",              //  사용자 이름
  val id: String = "",                //  사용자 ID
  val email: String = "",             //  사용자 Email
  val password: String = "",          //  사용자 비밀번호
  val passwordConfirm: String = "",   //  사용자 비밀번호 재입력(검증)
  val birthday: String = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(System.currentTimeMillis()),    //  사용자 생년월일
  val gender: String = "",            //  사용자 성별
  val nationality: String = "",       //  내, 외국인

  // Page 3: 은행 정보
  val bankName: String = "",          //  은행 이름
  val accountName: String = "",       //  예금주
  val accountNumber: String = "",     //  계좌번호

  // Page 4: 주소 불러오기
  val lat: Double = 0.0,
  val lon: Double = 0.0,
  val address: String = "",
  val roadAddress: List<AddressFindRoadAddress> = emptyList(),
  val roadAddress1: List<Coord2RoadAddress> = emptyList(),
  val addressName: String = "",

  // Page 5 (자격증 등록) 관련 상태 - 수정된 버전
  val educationCertificateUri: String = "",          // 교육자격증 이미지 URI
  val workerCardUri: String = "",                    // 근로자증 이미지 URI
  val showBottomSheet: Boolean = false,              // 바텀시트 표시 여부
  val showLaterDialog: Boolean = false,              // "나중에 하기" 다이얼로그 표시 여부
  val currentPhotoPath: String = "",                 // 현재 사진 경로
  val takePicType: String = "",                      // 사진 촬영 타입 ("educationCertificate", "workerCard")
  val isGrantCamera: Boolean = false,                // 카메라 권한 승인 여부

  // Page 6 직종 및 경력 정보
  val selectedJobCode: String = "",
  val selectedJobName: String = "직종을 선택해주세요",
  val yearInput: String = "",
  val monthInput: String = "",
  val workExperienceList: List<WorkExperience> = emptyList(),
  val isRegistrationSuccess: Boolean = false,

  // 공통 상태
  val currentPage: Int = 1,
  val errorMessage: String? = null,
  val validationErrors: Map<String, String> = emptyMap(),

  ) {
  // 각 페이지 별 완료 상태 확인
  val isPage1Complete: Boolean
    get() = isPhoneVerified

  val isPage2Complete: Boolean
    get() = name.isNotEmpty() &&
      id.isNotEmpty() &&
      email.isNotEmpty() &&
      password.isNotEmpty() &&
      gender.isNotEmpty() &&
      birthday.isNotEmpty() &&
      nationality.isNotEmpty()

  val isPage3Complete: Boolean
    get() = accountName.isNotEmpty() &&
      bankName.isNotEmpty() &&
      accountNumber.isNotEmpty()

  val isPage4Complete: Boolean
    get() = addressName.isNotEmpty() &&
      address.isNotEmpty() &&
      roadAddress.isNotEmpty() &&
      lat != 0.0 &&
      lon != 0.0

  val isPage5Complete: Boolean
    get() = educationCertificateUri.isNotEmpty() ||
      workerCardUri.isNotEmpty()

  val isPage5LaterComplete: Boolean
    get() = educationCertificateUri.isEmpty() &&
      workerCardUri.isEmpty()

  val isPage6Complete: Boolean
    get() = workExperienceList.isNotEmpty()

  val isPage6LaterComplete: Boolean
    get() = workExperienceList.isEmpty()

  // 전체 완료 상태
  val isAllDataComplete: Boolean
    get() = isPage1Complete && isPage2Complete && isPage3Complete && isPage4Complete && (isPage5Complete || isPage5LaterComplete) && (isPage6Complete || isPage6LaterComplete)
}