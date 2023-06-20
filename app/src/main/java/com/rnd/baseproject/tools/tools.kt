package com.rnd.baseproject.tools

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.rnd.baseproject.BaseApplication
import com.rnd.baseproject.retrofit_api.Resource
import com.rnd.baseproject.retrofit_api.Status

import com.rnd.baseproject.activity.MainActivity
import com.rnd.baseproject.retrofit_api.response.User
import java.io.File
import java.io.Serializable
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Mona on 09-03-2023.
 */
fun getTwoDigits(digit: Int): String {
    return if (digit < 10) "0$digit" else "$digit"
}

fun toast(message: Any) =
    Toast.makeText(BaseApplication.getAppContext(), "" + message, Toast.LENGTH_SHORT).show()

fun setMyTheme() {
    if (Preferences.getInt(
            Preferences.KEY_CURRENT_THEME, 0
        ) == 0
    ) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
}


@Suppress("DEPRECATION")
fun isOnline(context: Context): Boolean {
    var result = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}


fun getToken(): String {
    val token = if (Gson().fromJson(
            Preferences.getString(Preferences.KEY_USER_DATA), User::class.java
        ) != null
    ) {
        getUserData().token
    } else {
        ""
    }
    return token
}

fun logOutUser(context: Context= BaseApplication.getAppContext()) {
    Preferences.deleteAllPreferences()
    Handler(Looper.getMainLooper()).post {
       context.deleteDatabase(ConstValue.APP_DATABASE_NAME)
//        LocalDataSource.deleteDatabase()
    }
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)

}

fun saveUserData(user: User) {
    Preferences.setString(Preferences.KEY_USER_DATA, Gson().toJson(user))
    setCrashReportData()
}

fun getUserData(): User {
    return Gson().fromJson(Preferences.getString(Preferences.KEY_USER_DATA), User::class.java)
}

fun setCrashReportData() {
    val crashlytics = FirebaseCrashlytics.getInstance()
    val user = getUserData()
    crashlytics.setCustomKey("user_id", user.id)
    crashlytics.setCustomKey("user_name", "${user.first_name} ${user.last_name}")
    crashlytics.setCustomKey("mobile number", user.phone_number)
    crashlytics.setCustomKey("email id", user.email)
}

const val osType = "Android"

fun deviceName(): String {
    return "${android.os.Build.MANUFACTURER}(${android.os.Build.MODEL})"
}

fun fetchFcmId() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val fcmId = task.result!!
            Preferences.setString(Preferences.KEY_FCM_ID, fcmId)
        }
    }
}

fun utcTime(): Long {
    return System.currentTimeMillis() / 1000
}

fun dateFormat(inputFormat: String, date: Long): String {
    return if (date > 0) SimpleDateFormat(inputFormat, Locale.ENGLISH).format(date * 1000) else ""
}

fun time24Hour(time: String, inputFormat: String, outputFormat: String): String {
    val output = SimpleDateFormat(outputFormat, Locale.ENGLISH)
    val input = SimpleDateFormat(inputFormat, Locale.ENGLISH)
    return output.format(input.parse(time)!!)
}

fun timeInMillis(dateTime: String, pattern: String): Long {
    val date = SimpleDateFormat(pattern, Locale.ENGLISH).parse(dateTime)
    if (date != null) {
        return date.time / 1000
    }
    return 0
}

fun <T> Context.handleResponse(showLoading: Boolean, it: Resource<T>, success: () -> Unit) {
    when (it.status) {
        Status.LOADING -> {
//            if (showLoading) DialogUtils.loadingDialog(this)
            showLoadingDialog.value = showLoading
        }
        Status.SUCCESS -> {
//            DialogUtils.cancel()
            success.invoke()
            showLoadingDialog.value = false
        }
        Status.ERROR -> {
//            DialogUtils.cancel()
            showLoadingDialog.value = false
            it.message?.let { it1 ->
                toast(it1)
            }
        }
    }
}

fun <T> Context.handleResponse(
    showLoading: Boolean, cancelDialog: Boolean, it: Resource<T>, success: () -> Unit
) {
    when (it.status) {
        Status.LOADING -> {
            if (showLoading) DialogUtils.loadingDialog(this)
        }
        Status.SUCCESS -> {
            if (cancelDialog) DialogUtils.cancel()
            success.invoke()
        }
        Status.ERROR -> {
            DialogUtils.cancel()
            it.message?.let { it1 ->
                toast(it1)
            }
        }
    }
}

fun twoPointAfterDecimalPattern(longVal: Double): String {
    return DecimalFormat("########.##").format(longVal)
}

fun numberFormat(longVal: Double): String {
    return DecimalFormat("#,##,##,###.##").format(longVal)
}

fun redirectToMapApp(v: View, lat: Any, long: Any, name: String = "") {
    val uri = "http://maps.google.com/maps?q=loc:$lat,$long ($name)"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    v.context.startActivity(intent)
}

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}


fun getDeviceIpAddress(): String? {
    try {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLinkLocalAddress && !address.isLoopbackAddress && address is Inet4Address) {
                    return address.getHostAddress()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun getImageUri(path: String?): Uri? {
    return Uri.fromFile(path?.let { File(it) })
}

private fun getCustomDaysDate(amount: Int): Long {
    val yesterday = Calendar.getInstance()
    yesterday.add(Calendar.DAY_OF_MONTH, amount)
    return yesterday.timeInMillis / 1000
}

fun getYesterdayDate(): Long {
    return getCustomDaysDate(-1)
}

fun getLast7DaysDate(): Long {
    return getCustomDaysDate(-7)
}

fun getLast30DaysDate(): Long {
    return getCustomDaysDate(-30)
}