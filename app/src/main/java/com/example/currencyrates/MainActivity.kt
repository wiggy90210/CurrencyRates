package com.example.currencyrates

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.currencyrates.Adapter.RatesAdapter
import com.example.currencyrates.Model.Base
import com.example.currencyrates.Retrofit.CurrencyAPI
import com.example.currencyrates.Retrofit.RetrofitClient
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/** W apce nie użyłem androidx, bo miałem problemy przy uruchomieniu aplikacji dla api 28 i 29 */
class MainActivity : AppCompatActivity() {

    private lateinit var api: CurrencyAPI
    /**tworzę adapter z domyślnymi wartościami, żebym mógł zainicjalizować RecyclerView na samym początku
     a następnie w onNext() tylko podmieniać dataset w adapterze. Gdybym przypisywał adapter dopiero po pobraniu, to co sekundę
     Recycler byłby inicjalizowany od początku i wracałby na górę listy - scrollowanie byłoby mocno utrudnione
     */
    private val mAdapter = RatesAdapter(this, Base())
    /** to się przyda w onDestroy() - będzie tylko jeden obiekt typu Disposable, więc lista nie jest potrzebna */
    lateinit var mDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = RetrofitClient.instance
        api = retrofit.create(CurrencyAPI::class.java)

        rvRates.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }

        getData()
    }

    private fun getData() {

        val ratesObservable = api.getRates("EUR")
        ratesObservable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .repeatWhen { completed -> completed.delay(1, TimeUnit.SECONDS) }
            .subscribe(object : Observer<Base> {
                override fun onComplete() {
                    Log.wtf("onComplete", "called")
                }

                override fun onSubscribe(d: Disposable) {
                    Log.wtf("onSubscribe", "called")
                    mDisposable = d
                }

                override fun onNext(t: Base) {
                    displayData(t)
                    Log.wtf("onNext", "called")
                }

                override fun onError(e: Throwable) {
                    Log.wtf("onError", "called")
                }

            })
    }

    private fun displayData(rates: Base) {
        tvTitle.text = "Base currency: ${rates.baseCurrency}"
        mAdapter.setData(rates)
    }

    override fun onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed) {
            mDisposable.dispose()
        }
        super.onDestroy()
    }
}
