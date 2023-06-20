package com.rnd.baseproject.retrofit_api

/** Created by Mona on 10-06-2022*/
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {

        fun <T> success(msg: String,data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, msg)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

    }
}
