package com.billcorea.jikgong.presentation.company.main.scout.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.billcorea.jikgong.api.models.location.AddressFindRoadAddress
import com.billcorea.jikgong.api.models.location.Coord2RoadAddress
import com.billcorea.jikgong.utils.MainViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapLocationUiState(
    val selectedPosition: LatLng? = null,
    val isGpsLocationLoaded: Boolean = false,
    val currentLat: Double = 37.5665,
    val currentLon: Double = 126.9780,
    val searchQuery: String = "",
    val searchResults: List<AddressFindRoadAddress> = emptyList(),
    val selectedAddress: Coord2RoadAddress? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchRadius: Int = 10
)

class MapLocationViewModel(
    private val mainViewModel: MainViewModel
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MapLocationUiState())
    val uiState: StateFlow<MapLocationUiState> = _uiState.asStateFlow()
    
    private var kakaoMapInstance: KakaoMap? = null
    private val TAG = "MapLocationViewModel"
    
    init {
        observeMainViewModelData()
    }
    
    private fun observeMainViewModelData() {
        mainViewModel.lat.observeForever { lat ->
            _uiState.value = _uiState.value.copy(currentLat = lat ?: 37.5665)
        }
        
        mainViewModel.lon.observeForever { lon ->
            _uiState.value = _uiState.value.copy(currentLon = lon ?: 126.9780)
        }
        
        mainViewModel.roadAddress.observeForever { addresses ->
            _uiState.value = _uiState.value.copy(searchResults = addresses ?: emptyList())
        }
        
        mainViewModel.roadAddress1.observeForever { addresses ->
            _uiState.value = _uiState.value.copy(
                selectedAddress = addresses?.firstOrNull()
            )
        }
    }
    
    fun setKakaoMapInstance(map: KakaoMap) {
        kakaoMapInstance = map
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
    
    fun loadCurrentGpsLocation(context: Context) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                mainViewModel.setLocation(context)
                _uiState.value = _uiState.value.copy(
                    isGpsLocationLoaded = true,
                    isLoading = false
                )
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load GPS location: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    error = "위치를 가져올 수 없습니다",
                    isLoading = false
                )
            }
        }
    }
    
    fun searchAddress(query: String) {
        if (query.trim().isEmpty()) return
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                mainViewModel.doKakaoGeocoding(query.trim())
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                Log.e(TAG, "Search error: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    error = "검색 중 오류가 발생했습니다",
                    isLoading = false
                )
            }
        }
    }
    
    fun selectSearchResult(address: AddressFindRoadAddress) {
        viewModelScope.launch {
            try {
                val lat = address.y.toDoubleOrNull()
                val lon = address.x.toDoubleOrNull()
                
                if (lat != null && lon != null) {
                    val position = LatLng.from(lat, lon)
                    _uiState.value = _uiState.value.copy(
                        selectedPosition = position,
                        searchQuery = "",
                        searchResults = emptyList()
                    )
                    
                    mainViewModel.doFindAddress(lat, lon)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Address selection error: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    error = "주소 선택 중 오류가 발생했습니다"
                )
            }
        }
    }
    
    fun selectMapPosition(latLng: LatLng) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(selectedPosition = latLng)
                mainViewModel.doFindAddress(latLng.latitude, latLng.longitude)
            } catch (e: Exception) {
                Log.e(TAG, "Map position selection error: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    error = "위치 선택 중 오류가 발생했습니다"
                )
            }
        }
    }
    
    fun clearSearchResults() {
        mainViewModel._roadAddress.value = emptyList()
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResults = emptyList()
        )
    }
    
    fun clearSelectedAddress() {
        mainViewModel._roadAddress1.value = emptyList()
        _uiState.value = _uiState.value.copy(selectedAddress = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun getCenterPosition(): LatLng {
        val state = _uiState.value
        return if (state.isGpsLocationLoaded && state.currentLat != 0.0 && state.currentLon != 0.0) {
            LatLng.from(state.currentLat, state.currentLon)
        } else {
            LatLng.from(37.5665, 126.9780)
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        kakaoMapInstance = null
        Log.d(TAG, "ViewModel cleared and map instance released")
    }
}