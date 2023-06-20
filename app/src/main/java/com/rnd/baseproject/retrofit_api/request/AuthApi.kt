package com.rnd.baseproject.retrofit_api.request

import com.rnd.baseproject.retrofit_api.response.AuthResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface AuthApi {
    @FormUrlEncoded
    @POST("sms_otp_verify")
    suspend fun otpVerify(
        @FieldMap params: HashMap<String, Any>
    ): AuthResponse

    @Multipart
    @POST("create_profile")
    suspend fun createAccount(
        @PartMap params: HashMap<String, RequestBody>, @Part adhar_image: MultipartBody.Part? = null
    ): AuthResponse
}