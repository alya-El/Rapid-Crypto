package com.dalhousie.rapid_crypto_app.persistence.dao.impl

import android.content.ContentValues
import android.util.Log
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.persistence.dao.PortfolioDAO
import com.dalhousie.rapid_crypto_app.persistence.datasource.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class PortfolioDAOImpl : PortfolioDAO {
    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    override suspend fun getPortfolioCurrencies(): List<PortfolioCurrency> {
        val document = FirebaseUtils().fireStoreDatabase.collection("portfolios")
            .document(firebaseUser?.email.toString())
            .get().await()

        val portfolioCurrencies = ArrayList<PortfolioCurrency>()
        document.data?.forEach { (_, value) ->
            value as HashMap<String, Any>

            portfolioCurrencies.add(
                PortfolioCurrency(
                    value["coinName"].toString(),
                    value["coinSymbol"].toString(),
                    value["coinImage"].toString(),
                    value["quantity"] as Double,
                    value["boughtFor"] as Double,
                    value["boughtFor"] as Double
                )
            )
        }

        return portfolioCurrencies
    }

    override suspend fun sellPortfolioCurrency(portfolioCurrency: PortfolioCurrency): List<PortfolioCurrency> {
        FirebaseUtils().fireStoreDatabase.collection("portfolios")
            .document(firebaseUser?.email.toString())
            .update(mapOf("${portfolioCurrency.coinName}.quantity" to portfolioCurrency.quantity))
            .await()

        return getPortfolioCurrencies()
    }

    override suspend fun buyPortfolioCurrency(portfolioCurrency: PortfolioCurrency){
        val portfolioCurrencies = getPortfolioCurrencies() as ArrayList<PortfolioCurrency>

        // iterate over the portfolioCurrencies list to update the quantity of selected coin
        for (currency in portfolioCurrencies ){
            if (portfolioCurrency.coinName == currency.coinName){
                portfolioCurrency.quantity += currency.quantity
            }
        }

        val currency = hashMapOf(
            "boughtFor" to portfolioCurrency.boughtFor,
            "coinImage" to portfolioCurrency.coinImage,
            "coinName" to portfolioCurrency.coinName,
            "coinSymbol" to portfolioCurrency.coinSymbol,
            "quantity" to portfolioCurrency.quantity

        )
        val updatedCurrency = mapOf(portfolioCurrency.coinName to currency)

        FirebaseUtils().fireStoreDatabase.collection("portfolios").document(firebaseUser?.email.toString())
            .set(updatedCurrency).await()

    }
}