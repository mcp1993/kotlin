package com.ym.kotlin.ui.netdemo

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/8 14:48
 */
class LiveDataCallAdapterFactory: Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
       if (getRawType(returnType) != LiveData::class.java){
           return null
       }
        val observableTyp:Type = getParameterUpperBound(0,returnType as ParameterizedType)
        val rawObservableType:Class<*> = getRawType(observableTyp)
        if (rawObservableType != ApiResponse::class.java){
            throw IllegalArgumentException("type must be a resource")
        }
        if (observableTyp !is ParameterizedType){
            throw IllegalArgumentException("resource must be parameterized")
        }
        val bodyType:Type = getParameterUpperBound(0,observableTyp)
        return LiveDataCallAdapter<Any>(bodyType)
    }
}