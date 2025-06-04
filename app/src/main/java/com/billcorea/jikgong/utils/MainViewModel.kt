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
import com.billcorea.jikgong.network.AddressFindResponse
import com.billcorea.jikgong.network.AddressFindRoadAddress
import com.billcorea.jikgong.network.Coord2AddressResponse
import com.billcorea.jikgong.network.Coord2RoadAddress
import com.billcorea.jikgong.network.DefaultResponse
import com.billcorea.jikgong.network.LoginData
import com.billcorea.jikgong.network.LoginErrorData
import com.billcorea.jikgong.network.LoginRequest
import com.billcorea.jikgong.network.LoginResponse
import com.billcorea.jikgong.network.RetrofitAPI
import com.billcorea.jikgong.network.SmsVerificationRequest
import com.billcorea.jikgong.network.SmsVerificationResponse
import com.billcorea.jikgong.network.VisaExpiryDateRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
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
                        // 정상적인 LoginData로 파싱
                        val loginData = Gson().fromJson(loginDataElement, LoginData::class.java)
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
                        val loginErrorData = Gson().fromJson(loginErrorJson.data, LoginErrorData::class.java)
                        _loginError.value = loginErrorData.errorMessage
                        Log.d("LOGIN", "로그인 실패: ${loginErrorData.errorMessage}")
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
                    recordList.add(document.roadAddress)
                }
                _roadAddress.value = recordList.toList()
            }
            override fun onFailure(req: Call<AddressFindResponse>, t: Throwable) {
                Log.e("", "error ${t.localizedMessage}")
            }
        })
    }


    fun doFindAddress(latitude: Double, longitude: Double) {
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
                                Log.e("", "findAddress ${document.roadAddress.addressName}")
                                recordList.add(document.roadAddress)
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
        val smsBody = SmsVerificationRequest(phone)
        RetrofitAPI.create().smsVerification(smsBody).enqueue(object:
            Callback<SmsVerificationResponse> {
            override fun onResponse(
                request: Call<SmsVerificationResponse>,
                response: Response<SmsVerificationResponse>
            ) {
                Log.e("", "response ${response.body()?.data?.authCode}")
                _authCode.value = response.body()?.data?.authCode
            }

            override fun onFailure(request: Call<SmsVerificationResponse>, t: Throwable) {
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
}