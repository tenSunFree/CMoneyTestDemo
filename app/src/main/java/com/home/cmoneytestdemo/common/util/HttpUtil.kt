package com.home.cmoneytestdemo.common.util

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * personally recommend using Retrofit OkHttp
 */
object HttpUtil {

    interface Subscribe {
        fun onSuccess(response: String)

        fun onError(e: Exception)
    }

    fun getData(httpUrl: String, listener: Subscribe) {
        var connection: HttpURLConnection? = null
        var `is`: InputStream? = null
        var br: BufferedReader? = null
        val result = StringBuilder()
        try {
            val url = URL(httpUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.connect()
            if (connection.responseCode == 200) {
                `is` = connection.inputStream
                if (`is` != null) {
                    br = BufferedReader(InputStreamReader(`is`, StandardCharsets.UTF_8))
                    var temp: String? = null
                    while ({ temp = br.readLine(); temp }() != null) {
                        result.append(temp)
                    }
                }
            }
            listener.onSuccess(result.toString())
        } catch (e: IOException) {
            listener.onError(e)
            e.printStackTrace()
        } finally {
            if (br != null) {
                try {
                    br.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            connection?.disconnect()
        }
    }
}