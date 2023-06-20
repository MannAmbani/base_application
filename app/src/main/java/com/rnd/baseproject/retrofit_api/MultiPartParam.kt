package com.rnd.baseproject.retrofit_api

import android.content.Context
import android.net.Uri
import com.rnd.baseproject.tools.URIPathHelper

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

///**This class will check if internet is available or not with the help of interceptor*/
//class MutriPartParam(context: Context):Interceptor {
//    private val applicationContext = context.applicationContext
//    /**Inside chain instance we have request*/
//    @Throws(NoInternetException::class)
//    override fun intercept(chain: Interceptor.Chain): Response {
//        if (!isInternetAvailavle()) {
////            throw NoInternetException("Make sure you have internet available")
//            Looper.prepare().apply {
//                toast("No Internet")
//            }
//
//
//        }
//        return chain.proceed(chain.request())
//
//    }
//
//    private fun isInternetAvailavle() :Boolean{
//        //to get network status (available or not)
//        val connectivityManager  = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        connectivityManager.activeNetworkInfo.also {
//            return it != null && it.isConnected
//        }
//    }
//    /**Now will add inteceptor to retrofit using client*/
//}
fun getImageMultipartFile(
    image_path: String,
    image_key: String = "image_file"
): MultipartBody.Part {
    val fileReqBody: RequestBody?
    return if (image_path.isNotEmpty()) {
        val file = File(image_path)
        // Create a request body with file and image media type
        fileReqBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part.createFormData(image_key, file.name, fileReqBody)
    } else {
        fileReqBody = "".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        MultipartBody.Part.createFormData(image_key, "", fileReqBody)
    }
}


fun getParamMultipart(str: Any): RequestBody {
    return ("" + str).toRequestBody("multipart/form-data".toMediaTypeOrNull())
}

fun getParamMultipart(params: HashMap<String, Any>): HashMap<String, RequestBody> {
    val hashMap = HashMap<String, RequestBody>()
    params.forEach { list ->
        hashMap[list.key] = getParamMultipart(list.value)
    }
    return hashMap
}


fun getImageFileMultipartBody(
    context: Context,
    path: Uri,
    key: String = "image_file"
): MultipartBody.Part {
    val uriPathHelper = URIPathHelper()
    var filePath = uriPathHelper.getPath(context, path)
    if (filePath == null) filePath = ""
    return getImageMultipartFile(filePath, key)
}