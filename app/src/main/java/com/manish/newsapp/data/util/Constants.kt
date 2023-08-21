package com.manish.newsapp.data.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class Constants {
    companion object {
        const val BASE_URL = "https://newsapi.org/"

        fun getDate(date: String?): String? {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val output = SimpleDateFormat("yyyy-MM-dd")
            var d: Date? = null
            run {
                try {
                    d = input.parse(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            return if (d != null) {
                output.format(d)
            } else {
                date
            }
        }

        const val API_KEY = "33910e1ac46f496abf596d56d73fad40"
        const val DEFAULT_COUNTRY = "us"
        const val QUERY_PAGE_SIZE = 20
        const val STARTING_PAGE_INDEX = 1

    }



}