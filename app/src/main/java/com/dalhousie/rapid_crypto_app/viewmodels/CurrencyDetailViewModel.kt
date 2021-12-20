package com.dalhousie.rapid_crypto_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.CoinGeckoDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.repositories.CoinGeckoRepository


class CurrencyDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val coinGeckoRepository : CoinGeckoRepository
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