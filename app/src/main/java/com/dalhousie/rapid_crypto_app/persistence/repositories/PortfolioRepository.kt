package com.dalhousie.rapid_crypto_app.persistence.repositories

import androidx.annotation.WorkerThread
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.persistence.dao.PortfolioDAO

class PortfolioRepository(private val portfolioDAO: PortfolioDAO) {

    @WorkerThread
    suspend fun getPortfolioCurrencies(): List<PortfolioCurrency> {
        return portfolioDAO.getPortfolioCurrencies()
    }

    @WorkerThread
    suspend fun sellPortfolioCurrency(portfolioCurrency: PortfolioCurrency): List<PortfolioCurrency> {
        return portfolioDAO.sellPortfolioCurrency(portfolioCurrency)
    }

    @WorkerThread
    suspend fun buyPortfolioCurrency(portfolioCurrency: PortfolioCurrency){
         portfolioDAO.buyPortfolioCurrency(portfolioCurrency)
    }
}