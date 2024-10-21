package com.sunassistant.rest

open class BaseViewModel<T> {
    var listener: ApiListener<T>? = null

    protected suspend fun fetchData(
        loadData: suspend () -> T
    ) {
        listener?.onLoadStart()
        try {
            val data = loadData()
            listener?.onDataLoaded(data)
        } catch (e: Exception) {
            listener?.onException(e)
        } finally {
            listener?.onLoadEnd()
        }
    }
}