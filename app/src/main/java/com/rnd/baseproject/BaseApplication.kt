package com.rnd.baseproject

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.rnd.baseproject.tools.setMyTheme
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Mona on 07-03-2023.
 */
@HiltAndroidApp
class BaseApplication : MultiDexApplication() , LifecycleObserver {

    companion object {
        private var mInstance: BaseApplication? = null

        fun getAppContext(): Context {
            if (mInstance == null) {
                mInstance?.applicationContext
            }
            return mInstance as BaseApplication
        }

        fun getActivity(): Activity {
            return Activity()
        }
    }

    init {
        mInstance = this
    }

    override fun onCreate() {
        super.onCreate()
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        setMyTheme()
    }
}