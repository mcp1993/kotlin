package com.ym.kotlin.ui.netdemo

import android.util.Log
import okhttp3.*
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.*

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/7 14:22
 *
 *
 */
class KtHttpLogInterceptor(block:(KtHttpLogInterceptor.()->Unit)? = null ):Interceptor {
    private var logLevel:LogLevel = LogLevel.NONE//打印日期的标记
    private var colorLevel:ColorLevel = ColorLevel.DEBUG//默认是debug级别的logcat
    private var logTag:String = TAG//日志的LogCate的tag

    init {
        block?.invoke(this)
    }

    fun logLevel(level:LogLevel):KtHttpLogInterceptor{
        logLevel = level
        return this
    }

    fun colorLevel(level:ColorLevel):KtHttpLogInterceptor{
        colorLevel = level
        return this
    }

    fun logTag(tag:String):KtHttpLogInterceptor{
        logTag = tag
        return this
    }


    override fun intercept(chain: Interceptor.Chain): Response {
       //请求
        val request = chain.request()
        //响应
        return kotlin.runCatching { chain.proceed(request) }
            .onFailure {
                it.printStackTrace()
                logIt(it.message.toString(),ColorLevel.ERROR
            )
            }.onSuccess { response ->
                if (logLevel == LogLevel.NONE){
                    return response
                }
                //记录请求日志
                logRequest(request,chain.connection())
                //记录响应日志
                logResponse(response)
            }.getOrThrow()
    }

    private fun logRequest(request: Request,connection: Connection?){
        val sb = StringBuilder()
        sb.appendln("\r\n")
        sb.appendln(
            "->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->" +
                    "->->->->->->->->->->->->->->->->->->->->->->->->"
        )
        when(logLevel){
            LogLevel.NONE ->{

            }
            LogLevel.BASIC ->{
                logBasicReq(sb,request,connection)
            }
            LogLevel.HEADERS ->{
                logHeadersReq(sb,request,connection)
            }
            LogLevel.BODY ->{
                logBodyReq(sb,request,connection)
            }
        }
        sb.appendln("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->" +
                "->->->->->->->->->->->->->->->->->->->->->->->->")
        logIt(sb,ColorLevel.ERROR)
    }

    private fun logResponse(response: Response){
        val sb = StringBuffer()
        sb.appendln("\r\n")
        sb.appendln("<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<")
        when(logLevel){
            LogLevel.NONE -> {

            }
            LogLevel.BASIC -> {
                logBasicRsp(sb,response)
            }

            LogLevel.HEADERS -> {
                logHeadersRsp(response,sb)
            }

            LogLevel.BODY -> {
                logHeadersRsp(response,sb)
                //body.strin会抛IO异常
                kotlin.runCatching {
                    //peek类似于clone数据流，监视，窥探，不能直接用原来的body的string流数据作为日志，会消费掉IO，所以这里是peek，监测
                    val peekBody = response.peekBody(1024 * 1024)
                    sb.appendln(peekBody.string())
                }.getOrNull()
            }
        }

        sb.appendln("<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<-<<")
        logIt(sb,ColorLevel.ERROR)
    }

    private fun logBodyReq(
        sb:java.lang.StringBuilder,
        request: Request,
        connection: Connection?
    ){
        logHeadersReq(sb,request,connection)
        sb.appendln("RequestBody: ${request.body.toString()}")
    }

    private fun logHeadersReq(
        sb:java.lang.StringBuilder,
        request: Request,
        connection: Connection?
    ){
        logBasicReq(sb,request,connection)
        val headersStr = request.headers.joinToString("") { header ->
            "请求 Header: {${header.first}=${header.second}}\n"
        }
        sb.appendln(headersStr)
    }

    private fun logBasicReq(
        sb:java.lang.StringBuilder,
        request: Request,
        connection: Connection?
    ){
        sb.appendln("请求 method: ${request.method} url: ${decodeUrlStr(request.url.toString())}" +
                " tag: ${request.tag()} protocol: ${connection?.protocol() ?: Protocol.HTTP_1_1}  ")
    }


    private fun logHeadersRsp(response: Response,sb:StringBuffer){
        logBasicRsp(sb,response)
        val headeresStr:String = response.headers.joinToString(separator = "") { header ->
            "响应 Header: {${header.first}=${header.second}}\n"
        }
        sb.appendln(headeresStr)
    }

    private fun logBasicRsp(sb: StringBuffer,response: Response){
        sb.appendln("响应 protocol: ${response.protocol} code: ${response.code}  message: ${response.message}" )
            .appendln("响应 request Url: ${decodeUrlStr(response.request.url.toString())}")
            .appendln("响应 sentRequestTime ${toDateTimeStr(
                response.sentRequestAtMillis,
                MILLIS_PATTERN)}  receivedResponseTime: ${toDateTimeStr(response.receivedResponseAtMillis,
                MILLIS_PATTERN)}")
    }

    private fun decodeUrlStr(url:String):String?{
        return kotlin.runCatching {
            URLDecoder.decode(url,"utf-8")
        }.onFailure { it.printStackTrace() }.getOrNull()
    }

    private fun logIt(any: Any,tempLevel:ColorLevel? = null){
        when(tempLevel?:colorLevel){
            ColorLevel.VERBOSE ->Log.v(logTag,any.toString())
            ColorLevel.DEBUG ->Log.d(logTag,any.toString())
            ColorLevel.INFO ->Log.i(logTag,any.toString())
            ColorLevel.WARN ->Log.w(logTag,any.toString())
            ColorLevel.ERROR ->Log.e(logTag,any.toString())
        }
    }

    companion object{
        private const val TAG = "KtHttp"

        const val MILLIS_PATTERN = "yyy-MM-dd HH:mm:ss.SSSXXX"

        fun toDateTimeStr(millis:Long,pattern:String):String{
            return SimpleDateFormat(pattern,Locale.getDefault()).format(millis)
        }
    }

    /**
     * 打印日志的范围
     */
    enum class LogLevel{
        NONE,//不打印
        BASIC,//只打印首行，请求/响应
        HEADERS,//打印请求和响应的 所有 header
        BODY,//打印所有
    }

    enum class ColorLevel{
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }
}