package com.rnd.baseproject.retrofit_api

import android.content.Context
import com.rnd.baseproject.R
import java.io.IOException

/**
 * Created by Mona on 18-02-2023.
 */
class NoInternet(var context: Context) : IOException() {
    override val message: String
        get() {
            return context.resources.getString(R.string.no_internet_connection)
        }

}