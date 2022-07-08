package com.ym.kotlin.ui.netdemo

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/8 10:58
 * 基础的网络返回数据结构
 */
data class NetResponse(
    val code:Int,
    val data:Any?,
    val message:String
)