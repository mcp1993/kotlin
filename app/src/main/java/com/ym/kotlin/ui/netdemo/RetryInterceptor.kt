package com.ym.kotlin.ui.netdemo

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import kotlin.math.log

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/7 14:07
 *
 *
 * 重试请求的网络拦截器
 */
class RetryInterceptor(private val maxRetry:Int = 0):Interceptor {

    private var retriedNum : Int = 0//已经重试的次数，注意，设置maxRetry重试次数，作用于重试，所以总的请求次数，就是原始的1，+maxRetry

    override fun intercept(chain: Interceptor.Chain): Response {
        val request:Request = chain.request()
        Log.e("RetryInterceptor","intercept 20行：当前retriedNum=$retriedNum")
        var response = chain.proceed(request)
        while (!response.isSuccessful && retriedNum < maxRetry){
            retriedNum++
            Log.e("RetryInterceptor","intercept 24行：当前retriedNum=$retriedNum")
            response = chain.proceed(request)
        }
        return response
    }
}