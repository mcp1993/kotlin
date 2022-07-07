package com.ym.kotlin.ui.netdemo

import androidx.collection.SimpleArrayMap
import com.google.gson.Gson
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/7 10:05
 */
class OkhttpApi :HttpApi {

    companion object{
        private const val TAG = "OkhttpApi"
    }

    private var baseUrl = "Http://api.qingyunke.com/"
    var maxRetry = 0 //最大重试次数

    //存储请求，用于取消
    private val callMap = SimpleArrayMap<Any,Call>()

    private val mClient = OkHttpClient.Builder()
        .callTimeout(10,TimeUnit.SECONDS)//完整请求超时时长，从发起到接收返回数据，默认值0，不限定
        .connectTimeout(10,TimeUnit.SECONDS)//与服务器建立连接的时长，默认10s
        .readTimeout(10,TimeUnit.SECONDS)//读取服务器返回数据的时长
        .writeTimeout(10,TimeUnit.SECONDS)//向服务器写入数据的时长，默认10
        .retryOnConnectionFailure(true)//重连
        .followRedirects(false)//重定向
        .cache(Cache(File("sdcard/cache","okhttp"),1024))
        .cookieJar(LocalCookieJar())
        .addNetworkInterceptor(KtHttpLogInterceptor{
           logLevel( KtHttpLogInterceptor.LogLevel.BODY)
            colorLevel( KtHttpLogInterceptor.ColorLevel.ERROR)
            logTag("HHHHH")
        })
        .addNetworkInterceptor(RetryInterceptor(maxRetry))

        .build()



    override fun get(params: Map<String, Any>, path: String, callBack: IHttpCallback) {
        val url = "$baseUrl$path"
        val urlBuilder :HttpUrl.Builder = url.toHttpUrl().newBuilder()
        params.forEach{entry ->
            urlBuilder.addEncodedQueryParameter(entry.key,entry.value.toString())
        }
        val request:Request = Request.Builder()
            .get()
            .tag(params)
            .url(urlBuilder.build())
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()
        val newCall:Call = mClient.newCall(request)
        //存储请求，用于取消
        callMap.put(request.tag(),newCall)
        newCall.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
               callBack.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                callBack.onSuccess(response.body?.string())
            }
        })

    }

    override fun post(body: Any, path: String, callBack: IHttpCallback) {
//        val url = "$baseUrl$path"
        val url = "https://testapi.cniao5.com/accounts/login"
        val request = Request.Builder()
            .post(Gson().toJson(body).toRequestBody())
            .url(url)
            .tag(body)
            .build()
        val newCall:Call = mClient.newCall(request)
        //存储请求，用于取消
        callMap.put(request.tag(),newCall)
        newCall.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
               callBack.onFailed(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
               callBack.onSuccess(response.body?.string())
            }
        })
    }

    /**
     * 取消网络请求，tag就是每次请求的id 标记，也就是请求的传参
     */
    override fun cancelRequest(tag: Any) {
       callMap.get(tag)?.cancel()
    }

    /**
     * 取消所有网络请求
     */
    override fun cancelAllRequest() {
        for (i in 0 until callMap.size()){
            callMap.get(callMap.keyAt(i))?.cancel()
        }
    }


}