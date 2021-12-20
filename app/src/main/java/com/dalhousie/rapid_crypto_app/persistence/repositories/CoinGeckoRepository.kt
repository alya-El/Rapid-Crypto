package com.dalhousie.rapid_crypto_app.persistence.repositories

import androidx.lifecycle.LiveData
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.persistence.dao.CoinGeckoDAO

class CoinGeckoRepository(private val coinGeckoDAO: CoinGeckoDAO) {

    fun getCoinMarkets(): LiveData<Map<String, CoinGeckoCoinDetail>> {
        return coinGeckoDAO.getCoinMarkets()
    }
}