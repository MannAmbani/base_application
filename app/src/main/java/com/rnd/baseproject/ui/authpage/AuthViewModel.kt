package com.rnd.baseproject.ui.authpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rnd.baseproject.BaseViewModel
import com.rnd.baseproject.database.entity.Note
import com.rnd.baseproject.repository.AuthRepository
import com.rnd.baseproject.retrofit_api.Resource
import com.rnd.baseproject.retrofit_api.response.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository):BaseViewModel() {

    private val _otpVerifyResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val otpVerifyResponse: LiveData<Resource<AuthResponse>>
        get() = _otpVerifyResponse

    fun otpVerifyApiCall(params: HashMap<String, Any>) = viewModelScope.launch {
        _otpVerifyResponse.value = Resource.loading()
        _otpVerifyResponse.value = repository.otpVerify(params)
    }


    private val _createAccountResponse: MutableLiveData<Resource<AuthResponse>> = MutableLiveData()
    val createAccountResponse: LiveData<Resource<AuthResponse>>
        get() = _createAccountResponse

    fun createAccountApiCall(
        params: HashMap<String, RequestBody>, adhar_image: MultipartBody.Part? = null
    ) = viewModelScope.launch {
        _createAccountResponse.value = Resource.loading()
        _createAccountResponse.value = repository.createAccount(params, adhar_image)
    }

    fun addNote(note:Note) = viewModelScope.launch { repository.addNote(note) }
    fun getNotes() = viewModelScope.launch {
        repository.getNotes()
    }
    fun updateNote(note: Note) = viewModelScope.launch {
        repository.updateNote(note)
    }
    fun deleteNote(note:Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
}