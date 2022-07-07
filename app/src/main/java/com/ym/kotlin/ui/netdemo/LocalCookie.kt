package com.ym.kotlin.ui.netdemo

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * description ： TODO:类的作用
 * author : mcp1993
 * date : 2022/7/7 11:58
 *
 * 用于持久化的CookieJar实现类
 */
internal class LocalCookieJar:CookieJar {
    //cookie的本地化存储
    private val cache:MutableList<Cookie> = mutableListOf<Cookie>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        //过期的cookie
        val invalidCookie:MutableList<Cookie> = ArrayList()
        //有效的cookie
        val validCookie:MutableList<Cookie> = ArrayList()

        for (cookie in cache){
            if (cookie.expiresAt < System.currentTimeMillis()){
                //判断是否过期
                invalidCookie.add(cookie)
            }else if (cookie.matches(url)){
                //匹配cookie对应url
                validCookie.add(cookie)
            }
        }
        //缓存中移除过期的cookie
        cache.removeAll(invalidCookie)
        //返回List<Cookie>让Request进行设置
        return validCookie
    }

    /**
     * 将cookie保存
     */
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
       cache.addAll(cookies)
    }
}