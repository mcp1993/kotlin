package com.ym.kotlin

import android.app.Application
import android.util.Log
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/5 16:09
 */
class VmOne:ViewModel() {
    init {
        Log.e("VmOne","VmOne创建")
    }
}



class VmTwo(application: Application):AndroidViewModel(application){
    init {
        Log.e("VmTwo","VmTwo创建")
    }

//    internal fun getNum():String{
//        getApplication<Application>()
//    }

}