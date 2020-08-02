package com.example.currencyrates.Retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var _instance: Retrofit? = null
    val instance: Retrofit
        get() {
            if (_instance == null) {
                _instance = Retrofit.Builder().apply {
                    baseUrl("https://hiring.revolut.codes/api/android/")
                    addConverterFactory(GsonConverterFactory.create())
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                }.build()
            }
            return _instance!!
        }
}