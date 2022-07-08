package com.ym.kotlin.ui.netdemo

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/8 14:38
 */
object KtRetrofit {
    private val mOkClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)//完整请求超时时长，从发起到接收返回数据，默认值0，不限定
        .connectTimeout(10, TimeUnit.SECONDS)//与服务器建立连接的时长，默认10s
        .readTimeout(10, TimeUnit.SECONDS)//读取服务器返回数据的时长
        .writeTimeout(10, TimeUnit.SECONDS)//向服务器写入数据的时长，默认10
        .retryOnConnectionFailure(true)//重连
        .followRedirects(false)//重定向
        .cache(Cache(File("sdcard/cache","okhttp"),1024))
        .cookieJar(LocalCookieJar())
        .addNetworkInterceptor(KtHttpLogInterceptor{
            logLevel( KtHttpLogInterceptor.LogLevel.BODY)
            colorLevel( KtHttpLogInterceptor.ColorLevel.ERROR)
            logTag("HHHHH")
        })

        .build()

    private val retrofitBuilder : Retrofit.Builder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        .client(mOkClient)

    private var retrofit:Retrofit? = null

    fun initConfig(baseUrl:String,okHttpClient: OkHttpClient = mOkClient):KtRetrofit{
        retrofit = retrofitBuilder.baseUrl(baseUrl).client(okHttpClient).build()
        return this
    }

    fun <T> getService(sServiceClazz:Class<T>):T{
        if (retrofit == null){
            throw UninitializedPropertyAccessException("Retrofit必须初始化，需要配置baseURL")
        }else{
            return this.retrofit!!.create(sServiceClazz)
        }
    }


}