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

import kotlinx.coroutines.*
import okhttp3.ConnectionPool
import java.util.Collections
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.measureTimeMillis


class ConfigTester {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .connectionPool(
            ConnectionPool(
                maxIdleConnections = 40,
                keepAliveDuration = 4,
                timeUnit = TimeUnit.MINUTES
            )
        )
        .addInterceptor(HttpLoggingInterceptor())
//                .cache(Cache(cacheDir, cacheSize))
        .build()

    private val counter: AtomicLong = AtomicLong(1)

    /**
     * 测试地址集合
     * simple： https://ghfast.top/https://raw.githubusercontent.com/hanlyjiang/Tvbox1/main/cr.json
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun testMultiUrls(url: String) {
        runBlocking {
            println(message = "testMultiUrls:$url")
            requestContent(url).let { result ->
                val validUrls = Urls()
                // 记录执行时间
                val urls: Urls = Gson().fromJson(result, Urls::class.java)
                println(message = "testMultiUrls: totalCount=${urls.urls.size}")
                val time = measureTimeMillis {
                    val deferredResults = urls.urls.map { item ->
                        async {
                            testOneUrl2FilterUrl(item)
                        }
                    }
                    val resultList = deferredResults.awaitAll().filter {
                        it.isValid
                    }.map { it.url }
                    validUrls.urls.addAll(resultList)
                }
                println("Finished:time=${time}:${validUrls.urls.size}")
                FileWriter(File("./cr.json")).apply {
                    write(Gson().toJson(validUrls))
                    flush()
                }.close()
            }
        }
    }

    private fun testOneUrl2FilterUrl(url: Url): FilterUrl {
        requestContent(url.url).let { content ->
            return (content.isValidJson()).apply {
                println("${counter.getAndAdd(1)}: testOneUrl2FilterUrl@${Thread.currentThread().name} :${url.name} ($this)-> ${url.url}")
            }.let {
                FilterUrl(it, url)
            }
        }
    }

    private fun String.isValidJson(): Boolean {
        return startsWith("{") && endsWith("}")
    }

    private fun get(url: String): Request {
        return Request.Builder().url(url).get().build()
    }

    private fun requestContent(url: String): String {
        val response: Response?
        try {
            response = client.newCall(get(url)).execute()
            val result = if (response.code == 200) {
                response.body?.byteString()?.string(Charset.defaultCharset()) ?: ""
            } else {
                ""
            }
            response.body?.close()
            return result
        } catch (e: Exception) {
            return ""
        }
    }

}