package com.dalhousie.rapid_crypto_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.WatchlistCurrency
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.CoinGeckoDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.WatchlistDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.repositories.CoinGeckoRepository
import com.dalhousie.rapid_crypto_app.persistence.repositories.WatchlistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private val watchlistRepository: WatchlistRepository
    private val coinGeckoRepository: CoinGeckoRepository
    private val _watchlistCurrenciesLiveData: MutableLiveData<List<WatchlistCurrency>> = MutableLiveData()
    private val _snackbar = MutableLiveData<String?>()
    var coinGeckoCoinLiveData: LiveData<Map<String, CoinGeckoCoinDetail>>

    val watchlistCurrenciesLiveData: LiveData<List<WatchlistCurrency>>
        get() = _watchlistCurrenciesLiveData

    val snackbar: LiveData<String?>
        get() = _snackbar

    init {
        val watchlistDAO = WatchlistDAOImpl()
        watchlistRepository = WatchlistRepository(watchlistDAO)

        val coinGeckoDAO = CoinGeckoDAOImpl(application)
        coinGeckoRepository = CoinGeckoRepository(coinGeckoDAO)

        coinGeckoCoinLiveData = MutableLiveData()
    }

    fun onSnackbarShown() {
        _snackbar.value = null
    }

    fun loadWatchlistData() {
        viewModelScope.launch(Dispatchers.Main) {
            val watchlistCurrencies = watchlistRepository.getWatchlistCurrencies()
            _watchlistCurrenciesLiveData.value = watchlistCurrencies
        }
    }

    fun loadCoinGeckoCoinData() {
        coinGeckoCoinLiveData = coinGeckoRepository.getCoinMarkets()
    }

    fun addWatchlistCurrency(watchlistCurrency: WatchlistCurrency) {
        viewModelScope.launch {
           watchlistRepository.addWatchlistCurrency(watchlistCurrency)
            _snackbar.value = "${watchlistCurrency.coinName} added to watchlist."
        }
    }

    fun removeWatchlistCurrency(watchlistCurrency: WatchlistCurrency) {
        viewModelScope.launch {
            val watchlistCurrencies = watchlistRepository.removeWatchlistCurrency(watchlistCurrency)
            _watchlistCurrenciesLiveData.value = watchlistCurrencies
            _snackbar.value = "${watchlistCurrency.coinName} removed from watchlist."
        }
    }
}