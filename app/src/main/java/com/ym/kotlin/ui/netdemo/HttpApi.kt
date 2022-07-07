package com.ym.kotlin.ui.netdemo

/**
 * description ：网络请求统一接口类
 * author : mcp1993
 * date : 2022/7/7 09:52
 */
interface HttpApi {

    /**
     * 抽象的http的get请求封装,异步
     */
    fun get(params:Map<String,Any>,path:String,callBack:IHttpCallback)

    /**
     * 抽象的http同步的get请求
     */
    fun getSync(params:Map<String,Any>,path:String):Any?{
        return Any()
    }

    /**
     * 抽象的http的post请求封装,异步
     */
    fun post(body:Any,path:String,callBack: IHttpCallback)

    /**
     * 抽象的http同步的post请求
     */
    fun postSync(body:Any,path:String):Any? = Any()

    fun cancelRequest(tag:Any)

    fun cancelAllRequest()

}