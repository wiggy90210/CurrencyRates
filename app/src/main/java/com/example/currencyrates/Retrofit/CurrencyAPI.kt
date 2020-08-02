package com.example.currencyrates.Retrofit

import com.example.currencyrates.Model.Base
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("latest")
    fun getRates(@Query("base") base: String): Observable<Base>
}