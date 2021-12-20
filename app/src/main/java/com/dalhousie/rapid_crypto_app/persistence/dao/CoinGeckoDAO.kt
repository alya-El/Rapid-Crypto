package com.dalhousie.rapid_crypto_app.persistence.dao

import androidx.lifecycle.LiveData
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail

interface CoinGeckoDAO {
    fun getCoinMarkets(): LiveData<Map<String, CoinGeckoCoinDetail>>
}