package com.dalhousie.rapid_crypto_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.CoinGeckoDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.PortfolioDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.repositories.CoinGeckoRepository
import com.dalhousie.rapid_crypto_app.persistence.repositories.PortfolioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val portfolioRepository : PortfolioRepository
    private val coinGeckoRepository : CoinGeckoRepository
    private val _portfolioCurrenciesLiveData : MutableLiveData<List<PortfolioCurrency>> = MutableLiveData()
    private val _snackbar = MutableLiveData<String?>()
    var coinGeckoCoinLiveData : LiveData<Map<String, CoinGeckoCoinDetail>>

    val portfolioCurrenciesLiveData: LiveData<List<PortfolioCurrency>>
        get() = _portfolioCurrenciesLiveData

    val snackbar: LiveData<String?>
        get() = _snackbar

    init {
        val portfolioDAO = PortfolioDAOImpl()
        portfolioRepository = PortfolioRepository(portfolioDAO)

        val coinGeckoDAO = CoinGeckoDAOImpl(application)
        coinGeckoRepository = CoinGeckoRepository(coinGeckoDAO)

        coinGeckoCoinLiveData = MutableLiveData()
    }

    fun onSnackbarShown() {
        _snackbar.value = null
    }

    fun loadPortfolioData() {
        viewModelScope.launch(Dispatchers.Main) {
            val portfolioCurrencies = portfolioRepository.getPortfolioCurrencies()
            _portfolioCurrenciesLiveData.value = portfolioCurrencies
        }
    }

    fun loadCoinGeckoCoinData() {
        coinGeckoCoinLiveData = coinGeckoRepository.getCoinMarkets()
    }

    fun sellPortfolioCurrency(portfolioCurrency: PortfolioCurrency, quantityValue: Double) {
        viewModelScope.launch {
            portfolioCurrency.quantity -= quantityValue
            val portfolioCurrencies = portfolioRepository.sellPortfolioCurrency(portfolioCurrency)
            _portfolioCurrenciesLiveData.value = portfolioCurrencies
            _snackbar.value = "$quantityValue ${portfolioCurrency.coinName} sold successfully."
        }
    }

    fun buyPortfolioCurrency(portfolioCurrency: PortfolioCurrency){
        viewModelScope.launch {
            val portfolioCurrencies = portfolioRepository.buyPortfolioCurrency(portfolioCurrency)
            _snackbar.value = "${portfolioCurrency.quantity} ${portfolioCurrency.coinName} bought successfully."
        }
    }

}