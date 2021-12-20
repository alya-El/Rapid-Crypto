package com.dalhousie.rapid_crypto_app.persistence.repositories

import androidx.annotation.WorkerThread
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.models.Transaction
import com.dalhousie.rapid_crypto_app.models.TransactionDetails
import com.dalhousie.rapid_crypto_app.persistence.dao.TransactionDAO

class TransactionRepository (private val transactionDAO: TransactionDAO){

    @WorkerThread
    suspend fun getTransactions(): TransactionDetails{
        return transactionDAO.getTransactions()
    }

    @WorkerThread
    suspend fun addTransaction(transaction: Transaction): TransactionDetails {
        return transactionDAO.addTransaction(transaction)
    }

}