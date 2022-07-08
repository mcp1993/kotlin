package com.ym.kotlin.ui.netdemo

import retrofit2.Response


/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/8 10:39
 *
 *
 * 密封类形式的，网络数据返回封装类
 */
sealed class ApiResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body: T? = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                }else{
                    ApiSuccessResponse(body)
                }
            }else{
                ApiErrorResponse(
                    response.code(),
                    response.errorBody()?.toString() ?: response.message()
                )
            }
        }

        fun <T> create(errorCode:Int,error:Throwable):ApiResponse<T>{
            return ApiErrorResponse(
                errorCode,
                error.message ?: "Unknown Error"
            )
        }
    }
}

    class ApiEmptyResponse<T>:ApiResponse<T>()

    data class ApiErrorResponse<T>(val errorCode:Int,val errorMessage:String):ApiResponse<T>()

    data class ApiSuccessResponse<T>(val body:T):ApiResponse<T>()

    internal const val UNKNOWN_ERROR_CODE = -1 //未知错误码