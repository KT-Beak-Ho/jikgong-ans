package com.billcorea.jikgong.utils


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.billcorea.jikgong.network.location.AddressFindResponse
import com.billcorea.jikgong.network.location.AddressFindRoadAddress
import com.billcorea.jikgong.network.location.Coord2AddressResponse
import com.billcorea.jikgong.network.location.Coord2RoadAddress
import com.billcorea.jikgong.network.common.DefaultResponse
import com.billcorea.jikgong.network.auth.LoginData
import com.billcorea.jikgong.network.auth.LoginErrorData
import com.billcorea.jikgong.network.auth.LoginIdValidationRequest
import com.billcorea.jikgong.network.auth.LoginIdValidationResponse
import com.billcorea.jikgong.network.auth.LoginRequest
import com.billcorea.jikgong.network.auth.LoginResponse
import com.billcorea.jikgong.network.auth.PhoneValidationRequest
import com.billcorea.jikgong.network.auth.PhoneValidationResponse
import com.billcorea.jikgong.network.auth.RegisterWorker
import com.billcorea.jikgong.network.auth.RegisterWorkerResponse
import com.billcorea.jikgong.network.service.RetrofitAPI
import com.billcorea.jikgong.network.auth.SmsVerificationRequest
import com.billcorea.jikgong.network.auth.SmsVerificationResponse
import com.billcorea.jikgong.network.auth.VisaExpiryDateRequest
import com.billcorea.jikgong.network.auth.WorkExperience
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    lateinit var fusedLocationClient: FusedLocationProviderClient
    val _lat = MutableLiveData(0.0)
    val lat : LiveData<Double> = _lat
    val _lon = MutableLiveData(0.0)
    val lon : LiveData<Double> = _lon

    val _respAddress = MutableLiveData("")
    val respAddress : LiveData<String> = _respAddress

    val _authCode = MutableLiveData("1")
    val authCode : LiveData<String> = _authCode

    val _expiryMessage = MutableLiveData("")
    val expiryMessage : LiveData<String> = _expiryMessage

    val _roadAddress = MutableLiveData<List<AddressFindRoadAddress>>()
    val roadAddress : LiveData<List<AddressFindRoadAddress>> = _roadAddress

    val _roadAddress1 = MutableLiveData<List<Coord2RoadAddress>>()
    val roadAddress1 : LiveData<List<Coord2RoadAddress>> = _roadAddress1

    val _geoCoding = MutableLiveData("")
    val geoCoding : LiveData<String> = _geoCoding

    val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: MutableLiveData<LoginResponse?> = _loginResponse

    val _loginResult = MutableLiveData<LoginData?>()
    val loginResult: LiveData<LoginData?> = _loginResult

    val _loginError = MutableLiveData<String?>()
    val loginError: LiveData<String?> = _loginError

    val bankName = arrayOf("국민은행","신한은행","Kbank", "우리은행", "NH농협은행", "하나은행", "IBK기업은행","카카오뱅크",
        "IM뱅크","토스뱅크","BNK부산은행","SC제일은행","MG새마을금고","우체국","BNK경남은행","광주은행","KDB산업은행","신협","전북은행",
        "씨티뱅크","SH수협은행","제주은행")
    val bankCode = arrayOf("006","021","089","020","011","005","003","090","031","092","032","023","045","071","039","034","002","048","037","027","007","035")

    val _registerResult = MutableLiveData("")
    val registerResult: LiveData<String> get() = _registerResult

    val jobName = arrayOf("보통인부", "작업반장", "특별인부", "조력공", "비계공", "형틀목공", "철근공", "철골공", "용접공", "콘트리트공",
            "조적공", "견출공", "건축목공", "창호공", "유리공", "방수공", "미장공", "타일공", "도장공", "내장공", "도배공", "연마공", "석공",
            "줄눈공", "판넬조립공", "지붕잇기공", "조경공", "코킹공", "배관공", "보일러공", "위생공", "덕트공", "보온공", "기계설비공", "내선진공", "통신내선공", "통신설비공") // 37

    val jobCode = arrayOf("NORMAL", "FOREMAN", "SKILLED_LABORER", "HELPER", "SCAFFOLDER", "FORMWORK_CARPENTER", "REBAR_WORKER"
        , "STEEL_STRUCTURE", "WELDER", "CONCRETE_WORKER", "BRICKLAYER", "DRYWALL_FINISHER", "CONSTRUCTION_CARPENTER", "WINDOW_DOOR_INSTALLER", "GLAZIER"
        , "WATERPROOFING_WORKER", "PLASTERER", "TILE", "PAINTER", "INTERIOR_FINISHER", "WALLPAPER_INSTALLER", "POLISHER", "STONEMASON", "GROUT_WORKER"
        , "PANEL_ASSEMBLER", "ROOFER", "LANDSCAPER", "CAULKER", "PLUMBER", "BOILER_TECHNICIAN", "SANITARY_TECHNICIAN", "DUCT_INSTALLER"
        , "INSULATION_WORKER", "MECHANICAL_EQUIPMENT_TECHNICIAN", "ELECTRICIAN", "TELECOMMUNICATIONS_INSTALLER", "TELECOMMUNICATIONS_EQUIPMENT_INSTALLER")


    val _isLoginValidation = MutableLiveData("")
    val isLoginValidation: LiveData<String> = _isLoginValidation

    val _isPhoneValidation = MutableLiveData("")
    val isPhoneValidation: LiveData<String> = _isPhoneValidation

    fun doLogin(loginIdOrPhone: String, password: String, deviceToken: String) {
        val body = LoginRequest(loginIdOrPhone, password, deviceToken)
        RetrofitAPI.create().login(body).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()
                Log.d("LOGIN", "로그인 Response: ${response}")
                Log.d("LOGIN", "로그인 Response: ${loginResponse}")
                val loginDataElement = loginResponse?.data
                Log.d("LOGIN", "로그인 데이터: ${loginDataElement}")
                if (response.isSuccessful) {
                    try {
                        // LoginData는 이미 파싱된 상태
                        val loginData = loginDataElement!!
                        _loginResult.value = loginData
                        Log.d("LOGIN", "로그인 성공: ${loginData}")
                    } catch (e: Exception) {
                        _loginError.value = "로그인 실패"
                    }

                } else {
                    val loginErrorBodyString = response.errorBody()?.string()
                    Log.d("LOGIN", "에러 바디: $loginErrorBodyString")
                    try {
                        val loginErrorJson = Gson().fromJson(loginErrorBodyString, LoginResponse::class.java)
                        _loginError.value = loginErrorJson.message
                        Log.d("LOGIN", "로그인 실패: ${loginErrorJson.message}")
                    } catch (e: Exception) {
                        Log.e("LOGIN", "LoginErrorData 파싱 실패: ${e.localizedMessage}")
                        _loginError.value = "로그인 실패 (에러 파싱 실패)"
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
            }
        })
    }

    fun doKakaoGeocoding(query: String) {
        Log.e("", "doKakaoGeocoding start ... ")
        _roadAddress.value = emptyList()
        RetrofitAPI.createKakao().kakaoGeocoding(query).enqueue(object : Callback<AddressFindResponse>{
            override fun onResponse(request: Call<AddressFindResponse>, response: Response<AddressFindResponse>) {
                Log.e("", "response ${response.code()} / ${response.body()?.meta?.totalCount}")
                val recordList = mutableStateListOf<AddressFindRoadAddress>()
                for (document in response.body()?.documents!!) {
                    recordList.add(document.roadAddress!!)
                }
                _roadAddress.value = recordList.toList()
            }
            override fun onFailure(req: Call<AddressFindResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
            }
        })
    }


    fun doFindAddress(latitude: Double, longitude: Double) {
        Log.e("", "doFindAddress start ... ")
        _lat.value = latitude
        _lon.value = longitude
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
                                Log.e("", "findAddress ${document.roadAddress!!.addressName}")
                                recordList.add(document.roadAddress!!)
                            }
                            _roadAddress1.value = recordList.toList()
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
    fun setLocation(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        Log.e("", "getLastKnownLocation ...")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    _lat.value = it.latitude
                    _lon.value = it.longitude
                    Toast.makeText(context, " 위치 정보을 확인 하고 있습니다. ", Toast.LENGTH_SHORT).show()
                }
            }

        Log.e("", "setLocation ... ${lat.value} ${lon.value}")
    }

    fun doVisaExpiryDate(body : VisaExpiryDateRequest) {
        RetrofitAPI.create().visaExpiryDate(body).enqueue(object : Callback<DefaultResponse>{
            override fun onResponse(request: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                Log.e("", "response ${response.code()} / ${response.body()?.message}")
                _expiryMessage.value = response.body()?.message
            }

            override fun onFailure(req: Call<DefaultResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
            }

        })
    }

    fun doSmsVerification(phone: String) {
        val smsBody = SmsVerificationRequest(phone, "")
        RetrofitAPI.create().smsVerification(smsBody).enqueue(object:
            Callback<SmsVerificationResponse> {
            override fun onResponse(
                request: Call<SmsVerificationResponse>,
                response: Response<SmsVerificationResponse>
            ) {
                Log.e("", "response ${response.body()?.message}")
                _authCode.value = "1234" // Mock auth code for development
            }

            override fun onFailure(request: Call<SmsVerificationResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
            }

        })
    }

    fun doPhoneValidation(phone: String) {
        val phoneBody = PhoneValidationRequest(phone)
        RetrofitAPI.create().phoneValidation(phoneBody).enqueue(object:
            Callback<PhoneValidationResponse> {
            override fun onResponse(
                request: Call<PhoneValidationResponse>,
                response: Response<PhoneValidationResponse>
            ) {
                Log.e("", "response ${response.body()?.message}")

                if(response.body() == null) {
                    if(_isPhoneValidation.value == "false") {
                        _isPhoneValidation.value += "1"
                    }
                    else {
                        _isPhoneValidation.value = "false"
                    }
                }
                else {
                    if(_isPhoneValidation.value == "true") {
                        _isPhoneValidation.value += "1"
                    }
                    else {
                        _isPhoneValidation.value = "true"
                    }
                }

            }

            override fun onFailure(request: Call<PhoneValidationResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
            }
        })
    }

    fun doLoginIdValidation(id: String) {
        val loginIdBody = LoginIdValidationRequest(id)
        RetrofitAPI.create().loginIdValidation(loginIdBody).enqueue(object:
            Callback<LoginIdValidationResponse> {
            override fun onResponse(
                request: Call<LoginIdValidationResponse>,
                response: Response<LoginIdValidationResponse>
            ) {
                Log.e("", "response ${response.body()?.message}")

                if(response.body() == null) {
                    if(_isLoginValidation.value == "false") {
                        _isLoginValidation.value += "1"
                    }
                    else {
                        _isLoginValidation.value = "false"
                    }
                }
                else {
                    if(_isLoginValidation.value == "true") {
                        _isLoginValidation.value += "1"
                    }
                    else {
                        _isLoginValidation.value = "true"
                    }
                }
            }

            override fun onFailure(request: Call<LoginIdValidationResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
                _isLoginValidation.postValue("false")
            }
        })
    }

    fun doRegisterWorker(
        loginId: String,
        password: String,
        phone: String,
        email: String,
        role: String,
        privacyConsent: Boolean,
        deviceToken: String,
        isNotification: Boolean,
        workerName: String,
        birth: String,
        gender: String,
        nationality: String,
        accountHolder: String,
        account: String,
        bank: String,
        workerCardNumber: String,
        hasVisa: Boolean,
        credentialLiabilityConsent: Boolean,
        workerExperienceRequest: List<WorkExperience>,
        address: String,
        latitude: Double,
        longitude: Double,
        educationCertificate : String,
        workerCard : String,
    ) {
        val registerWorkerRequest = RegisterWorker(loginId, password, phone, email, role, privacyConsent, deviceToken, isNotification, workerName, birth, gender, nationality, accountHolder, account, bank, workerCardNumber, hasVisa, credentialLiabilityConsent, workerExperienceRequest, address, latitude, longitude)
        val gson = Gson()
        val jsonString = gson.toJson(registerWorkerRequest)
        val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaType())

        val educationCertificateImage = MultipartBody.Part.createFormData("educationCertificateImage", educationCertificate)
        val workerCardImage = MultipartBody.Part.createFormData("workerCardImage", workerCard)
        val signatureImage = MultipartBody.Part.createFormData("signatureImage", "")

        RetrofitAPI.create().registerWorker(requestBody, educationCertificateImage, workerCardImage, signatureImage).enqueue(object:
            Callback<RegisterWorkerResponse> {
            override fun onResponse(
                call: Call<RegisterWorkerResponse>,
                response: Response<RegisterWorkerResponse>
            ) {
                Log.e("", "response ${response.body()?.message}")
                _registerResult.value = response.body()?.message
            }

            override fun onFailure(call: Call<RegisterWorkerResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
            }

        })
    }

    fun clearRegisterResult() {
        _registerResult.value = "" // ← 메시지 초기화
        _isLoginValidation.value = ""
        _isPhoneValidation.value = ""
    }
}
