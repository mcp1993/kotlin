package com.ym.kotlin.ui.netdemo

import java.lang.Exception

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/8 10:18
 *
 * 数据响应的封装类，密封类sealed
 * 其实可以使用kotlin标准库中的result
 */

sealed class DataResult<out R> {
    data class Success <out T>(val data:T):DataResult<T>()
    data class Error(val exception: Exception):DataResult<Nothing>()
    object Loading:DataResult<Nothing>()

    override fun toString(): String {
        return when (this){
            is Success ->"Success[data=$data]"
            is Error ->"Error[exception=$exception]"
            Loading ->"loading"
        }
    }

    /**
     * 返回结果是success类，且data非null，才认为成功
     */
    val DataResult<*>.succeeded:Boolean
        get() = this is DataResult.Success && data != null


    //region Resource形式 数据封装
    /**
     * 资源获取的状态，枚举
     */
    enum class Status{
        SUCCESS,
        ERROR,
        LOADING
    }

    /**
     * 数据封装
     */

    data class Resource<out T>(val status: Status,val data:T?,val message:String?){
        companion object{
            fun <T> success(data:T?):Resource<T>{
                return Resource(
                    Status.SUCCESS,
                    data,
                    "Resource Success"
                )
            }

            fun <T> error(msg:String,data:T?):Resource<T>{
                return Resource(
                    Status.ERROR,
                    data,
                    msg
                )
            }

            fun <T> loading(data:T?):Resource<T>{
                return Resource(
                    Status.LOADING,
                    data,
                    null
                )
            }
        }
    }

    //endregion

}