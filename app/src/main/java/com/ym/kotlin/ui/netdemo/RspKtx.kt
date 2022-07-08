package com.ym.kotlin.ui.netdemo


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import okhttp3.Response

import java.io.IOException

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/8 15:33
 */

inline fun <reified T> okhttp3.Call.toLiveData():LiveData<T?>{
    val live = MutableLiveData<T?>()
    this.enqueue(object :okhttp3.Callback{
        override fun onFailure(call: okhttp3.Call, e: IOException) {
           live.postValue(null)
        }

        override fun onResponse(call: okhttp3.Call, response: Response) {
           if (response.isSuccessful){
               response.toEntity<T>()
           }
        }

    })

    return live
}

inline fun <reified T> Response.toEntity():T?{
    if (!isSuccessful) return null

    if (T::class.java.isAssignableFrom(String::class.java)) {
        return kotlin.runCatching {
            this.body?.string()
        }.getOrNull() as T
    }

    return kotlin.runCatching {
        Gson().fromJson(this.body?.string(),T::class.java)
    }.onFailure { e ->
        e.printStackTrace()
    }.getOrNull()
}