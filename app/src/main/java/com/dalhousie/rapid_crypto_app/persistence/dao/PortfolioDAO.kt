package com.dalhousie.rapid_crypto_app.persistence.dao

import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency

interface PortfolioDAO {
    suspend fun getPortfolioCurrencies(): List<PortfolioCurrency>

    suspend fun sellPortfolioCurrency(portfolioCurrency: PortfolioCurrency): List<PortfolioCurrency>

    suspend fun buyPortfolioCurrency(portfolioCurrency: PortfolioCurrency)
}