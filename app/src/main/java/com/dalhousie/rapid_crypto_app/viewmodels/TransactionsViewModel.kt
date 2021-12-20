package com.dalhousie.rapid_crypto_app.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.models.Transaction
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.CoinGeckoDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.TransactionDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.repositories.CoinGeckoRepository
import com.dalhousie.rapid_crypto_app.persistence.repositories.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsViewModel (application: Application) : AndroidViewModel(application) {

    private val transactionRepository : TransactionRepository
    private val coinGeckoRepository : CoinGeckoRepository
    private val _transactionsLiveData : MutableLiveData<List<Transaction>> = MutableLiveData()
    private val _snackbar = MutableLiveData<String?>()
    var coinGeckoCoinLiveData : LiveData<Map<String, CoinGeckoCoinDetail>>

    val transactionsLiveData: LiveData<List<Transaction>>
        get() = _transactionsLiveData

    val snackbar: LiveData<String?>
        get() = _snackbar

    init {
        val transactionDAO = TransactionDAOImpl()
        transactionRepository = TransactionRepository(transactionDAO)

        val coinGeckoDAO = CoinGeckoDAOImpl(application)
        coinGeckoRepository = CoinGeckoRepository(coinGeckoDAO)

        coinGeckoCoinLiveData = MutableLiveData()
    }

    fun onSnackbarShown() {
        _snackbar.value = null
    }

    fun loadTransactionData() {
        viewModelScope.launch(Dispatchers.Main) {
            val transactions = transactionRepository.getTransactions().transactions
            _transactionsLiveData.value = transactions
        }
    }

    fun loadCoinGeckoCoinData() {
        coinGeckoCoinLiveData = coinGeckoRepository.getCoinMarkets()
    }

    fun addTransaction(transaction: Transaction){
        viewModelScope.launch {
            val transaction = transactionRepository.addTransaction(transaction)

        }
    }
}