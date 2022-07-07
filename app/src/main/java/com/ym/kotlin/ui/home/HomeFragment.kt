package com.ym.kotlin.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ym.kotlin.databinding.FragmentHomeBinding
import com.ym.kotlin.ui.netdemo.HttpApi
import com.ym.kotlin.ui.netdemo.IHttpCallback
import com.ym.kotlin.ui.netdemo.OkhttpApi

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        textView.setOnClickListener { sendHttp() }
        return root
    }

    fun sendHttp(){
        val map = mapOf(
            "key" to "free"
            ,"appid" to "0"
            ,"msg" to "你好呀,我想和你做朋友，可以吗，哈哈")
        val httpApi:HttpApi = OkhttpApi()
        httpApi.get(map,"api.php",object :IHttpCallback{
            override fun onSuccess(data: Any?) {
              Log.e("success result","${data.toString()}")
                activity?.runOnUiThread{
                    val textView: TextView = binding.textHome
                    textView.text = data.toString()
                }
            }

            override fun onFailed(data: Any?) {
                Log.e("failed msg","${data.toString()}")
            }
        })


    }

    fun sendPost(){
       val httpApi:HttpApi = OkhttpApi()
        httpApi.post(LoginReq(),"",object :IHttpCallback{
            override fun onSuccess(data: Any?) {
                Log.e("success result","${data.toString()}")
                activity?.runOnUiThread{
                    val textView: TextView = binding.textHome
                    textView.text = data.toString()
                }
            }

            override fun onFailed(data: Any?) {
                Log.e("failed msg","${data.toString()}")
            }
        })
    }

    data class LoginReq( val mobi:String = "18648957777",val password:String = "cn5123456")

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}