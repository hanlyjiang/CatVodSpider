package com.github.hanlyjiang.config_tester

import com.github.hanlyjiang.config_tester.Urls.Url
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileWriter
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


class ConfigTester {
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor())
//                .cache(Cache(cacheDir, cacheSize))
        .build()

    /**
     * 测试地址集合
     * simple： https://ghfast.top/https://raw.githubusercontent.com/hanlyjiang/Tvbox1/main/cr.json
     */
    fun testMultiUrls(url: String) {
        println(message = "testMultiUrls:$url")
        requestContent(url).let { result ->
//            println("result:$result")
            val validUrls = Urls()
            val urls: Urls = Gson().fromJson(result, Urls::class.java)
            urls.urls.forEach {
                requestContent(it.url).let { content ->
                    (content != "").apply {
                        println("test:${it.name} ($this)-> ${it.url}")
                        if (this) {
                            validUrls.urls.add(it)
                        }
                    }
                }
            }
            println("valid:${validUrls.urls.size}")
            FileWriter(File("./cr.json")).apply {
                write(Gson().toJson(validUrls))
                flush()
            }.close()

        }
    }

    private fun get(url: String): Request {
        return Request.Builder().url(url).get().build()
    }

    private fun requestContent(url: String): String {
        val response: Response?
        try {
            response = client.newCall(get(url)).execute()
        } catch (e: Exception) {
            return ""
        }
        val result = if (response.code == 200) {
            response.body?.byteString()?.string(Charset.defaultCharset()) ?: ""
        } else {
            ""
        }
        response.body?.close()
        return result
    }

}