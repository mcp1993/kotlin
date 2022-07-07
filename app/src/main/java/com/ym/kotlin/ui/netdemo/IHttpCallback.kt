package com.ym.kotlin.ui.netdemo

/**
 * description ： 网络请求的接口回调
 * author : mcp1993
 * date : 2022/7/7 09:56
 */
interface IHttpCallback {
    /**
     * 网络请求成功回调
     */
    fun onSuccess(data:Any?)

    fun onFailed(data:Any?)
}