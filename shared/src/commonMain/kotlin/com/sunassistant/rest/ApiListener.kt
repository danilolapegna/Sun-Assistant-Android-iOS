package com.sunassistant.rest

interface ApiListener<T> {

    fun onLoadStart()

    fun onLoadEnd()

    fun onException(e: Exception)

    fun onDataLoaded(data: T)
}