package com.rnd.baseproject.retrofit_api

import androidx.multidex.BuildConfig
import com.rnd.baseproject.BaseApplication
import com.rnd.baseproject.tools.getToken
import com.rnd.baseproject.tools.isOnline
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/** Created by Mona on 09-06-2022*/
class RemoteDataSource @Inject constructor() {

    private val SERVER_URL = "https://associate.oobsmarthome.com/api/"
    val PDF_URL =  ""//"https://web.oobsmarthome.com/leads/"

    private fun getOkHttpClient(): OkHttpClient {
        // val okHttpClient = //getUnsafeOkHttpClient();
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                if (!isOnline(BaseApplication.getAppContext())) {
                    throw NoInternet(BaseApplication.getAppContext())
                }
                chain.proceed(chain.request().newBuilder().also {
                    /**Pass header here */
                    it.header("Accept", "application/json")
                    it.header(
//                        "Authorization", "Bearer " + (chain.request().header("token") ?: getToken())
                        "Authorization", chain.request().header("Authorization") ?: getToken()
                    )
                }.build())
            }.also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
                        .also { it.level = HttpLoggingInterceptor.Level.BODY }
                    client.addInterceptor(logging)
                }
            }
            .build()
    }

    fun <T> buildApi(api: Class<T>, serverUrl: String = SERVER_URL): T {
        return Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(serverUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
            .build().create(api)
    }

}