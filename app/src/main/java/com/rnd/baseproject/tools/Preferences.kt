package com.rnd.baseproject.tools

import android.content.SharedPreferences
import androidx.multidex.BuildConfig
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.rnd.baseproject.BaseApplication

object Preferences {

    private const val PREFS_NAME = BuildConfig.APPLICATION_ID
    const val KEY_FCM_ID = "key_fcm_id"

    const val KEY_USER_DATA = "key_user_data"
    const val KEY_DASHBOARD_REACHED = "key_dashboard_reached"
    const val KEY_ACCOUNT_VERIFICATION_PAGE_REACHED = "key_account_verification_page_reached"
    const val KEY_CURRENT_THEME = "key_current_theme"

    const val KEY_CURRENT_TIME = "key_current_time"
    const val KEY_CHECK_IN = "key_check_in"
    const val KEY_LATEST_DATE_NOTIFICATION = "key_latest_date_notification"


    private fun get(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            BaseApplication.getAppContext(),
            PREFS_NAME,
            MasterKey.Builder(BaseApplication.getAppContext())
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
//        return BaseApplication.getAppContext().getSharedPreferences(
//            PREFS_NAME,
//            Context.MODE_PRIVATE
//        )
    }

    fun setString(key: String?, value: String?) {
        val editor = get().edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun setInt(key: String?, value: Int) {
        val editor: SharedPreferences.Editor = get().edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun setLong(key: String, value: Long) {
        get().edit().putLong(key, value).apply()
    }

    fun setBool(key: String?, value: Boolean) {
        get().edit().putBoolean(key, value).apply()
    }

    fun getString(key: String): String {
        return "" + get().getString(key, "")
    }

    fun getInt(key: String?, defaultValue: Int): Int {
        return if (get().getInt(key, defaultValue) == -1) {
            0
        } else {
            get().getInt(key, defaultValue)
        }
    }

    fun getLong(key: String): Long {
        return if (get().getLong(key, 0L) == -1L) {
            0L
        } else {
            get().getLong(key, 0L)
        }
    }

    fun getBool(key: String?, default_value: Boolean): Boolean {
        return get().getBoolean(key, default_value)
    }

    fun deleteSharedPreferences(key: String?) {
        get().edit().remove(key).apply()
    }

    fun deleteAllPreferences() {
        get().edit().clear().apply()
    }
}