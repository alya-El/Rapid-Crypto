package com.dalhousie.rapid_crypto_app.persistence.dao

import com.dalhousie.rapid_crypto_app.models.WatchlistCurrency

interface WatchlistDAO {
    suspend fun getWatchlistCurrencies(): List<WatchlistCurrency>

    suspend fun removeWatchlistCurrency(watchlistCurrency: WatchlistCurrency): List<WatchlistCurrency>

    suspend fun addWatchlistCurrency(watchlistCurrency: WatchlistCurrency)
}