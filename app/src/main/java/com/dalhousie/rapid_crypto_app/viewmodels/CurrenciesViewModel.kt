package com.dalhousie.rapid_crypto_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.CoinGeckoDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.repositories.CoinGeckoRepository


class CurrenciesViewModel(application: Application) : AndroidViewModel(application) {

    private val coinGeckoRepository : CoinGeckoRepository
     var selectedCurrency : CoinGeckoCoinDetail? = null
    lateinit var coinGeckoCoinLiveData : LiveData<Map<String, CoinGeckoCoinDetail>>

    init {

        val coinGeckoDAO = CoinGeckoDAOImpl(application)
        coinGeckoRepository = CoinGeckoRepository(coinGeckoDAO)

        loadCoinGeckoCoinData()
    }

    fun loadCoinGeckoCoinData() {
        coinGeckoCoinLiveData = coinGeckoRepository.getCoinMarkets()
    }
}