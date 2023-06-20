package com.rnd.baseproject.retrofit_api

import com.google.gson.Gson
import com.rnd.baseproject.tools.Debug
import com.rnd.baseproject.tools.logOutUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

suspend fun <T> safeApiCall(call: suspend () -> T): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = call.invoke()
            val jsonObject = JSONObject(Gson().toJson(response))
            when (jsonObject.optInt("status")) {
                1 ->
                    Resource.success(jsonObject.optString("message"), response)
                0 -> {
                    Resource.error(jsonObject.optString("message"), null)
                }
                -1 -> {
                    logOutUser()
                    Resource.error(jsonObject.optString("message"), null)
                }
                else -> Resource.error("Something went wrong!", null)
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    Debug.trace("Test ")
                    throwable.printStackTrace()
                    Resource.error(throwable.response()?.message().toString(), null)
                }
                else -> {
                    throwable.printStackTrace()
                    Resource.error(throwable.message!!, null)
                }
            }
        }
    }
}

suspend fun <T> downloadApiCall(call: suspend () -> T): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = call.invoke()
            Resource.success("",response)
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    throwable.printStackTrace()
                    Resource.error(throwable.response()?.message().toString(), null)
                }
                else -> {
                    throwable.printStackTrace()
                    Resource.error(throwable.message!!, null)
                }
            }
        }
    }
}
