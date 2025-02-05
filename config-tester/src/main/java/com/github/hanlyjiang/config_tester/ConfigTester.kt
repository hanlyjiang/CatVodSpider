package com.github.hanlyjiang.config_tester

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.nio.charset.Charset


class ConfigTester {
    var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor())
//                .cache(Cache(cacheDir, cacheSize))
        .build()

    /**
     * 测试地址集合
     * simple： https://ghfast.top/https://raw.githubusercontent.com/hanlyjiang/Tvbox1/main/cr.json
     */
    fun testMultiUrls(url: String) {
        println(message = "testMultiUrls:$url")
        requestContent(url).let { result ->
            println("result:$result")
            val urls: Urls = Gson().fromJson(result, Urls::class.java)
            println("urls:$urls")
        }
    }

    private fun get(url: String): Request {
        return Request.Builder().url(url).get().build()
    }


    private fun requestContent(url: String): String {
        val response = client.newCall(get(url)).execute()
        return if (response.code == 200) {
            response.body?.byteString()?.string(Charset.defaultCharset()) ?: ""
        } else {
            ""
        }
    }

}