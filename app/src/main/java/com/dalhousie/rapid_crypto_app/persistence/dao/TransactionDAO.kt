package com.dalhousie.rapid_crypto_app.persistence.dao

import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.models.Transaction
import com.dalhousie.rapid_crypto_app.models.TransactionDetails

interface TransactionDAO {
    suspend fun getTransactions(): TransactionDetails

    suspend fun addTransaction(transaction: Transaction): TransactionDetails
}