package com.dalhousie.rapid_crypto_app.persistence.repositories

import androidx.annotation.WorkerThread
import com.dalhousie.rapid_crypto_app.models.WatchlistCurrency
import com.dalhousie.rapid_crypto_app.persistence.dao.WatchlistDAO

class WatchlistRepository(private val watchlistDAO: WatchlistDAO) {

    @WorkerThread
    suspend fun getWatchlistCurrencies(): List<WatchlistCurrency> {
        return watchlistDAO.getWatchlistCurrencies()
    }

    @WorkerThread
    suspend fun removeWatchlistCurrency(watchlistCurrency: WatchlistCurrency): List<WatchlistCurrency> {
        return watchlistDAO.removeWatchlistCurrency(watchlistCurrency)
    }

    @WorkerThread
    suspend fun addWatchlistCurrency(watchlistCurrency: WatchlistCurrency) {
         watchlistDAO.addWatchlistCurrency(watchlistCurrency)
    }
}