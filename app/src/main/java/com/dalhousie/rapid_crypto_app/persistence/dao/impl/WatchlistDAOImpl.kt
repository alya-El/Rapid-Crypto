package com.dalhousie.rapid_crypto_app.persistence.dao.impl

import android.content.ContentValues
import android.util.Log
import com.dalhousie.rapid_crypto_app.models.WatchlistCurrency
import com.dalhousie.rapid_crypto_app.persistence.dao.WatchlistDAO
import com.dalhousie.rapid_crypto_app.persistence.datasource.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

class WatchlistDAOImpl : WatchlistDAO {
    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    override suspend fun getWatchlistCurrencies(): List<WatchlistCurrency> {
        val document = FirebaseUtils().fireStoreDatabase.collection("watchlists")
            .document(firebaseUser?.email.toString())
            .get().await()

        val watchlistCurrencies = ArrayList<WatchlistCurrency>()
        Log.w(ContentValues.TAG, "WatchlistDAOImpl getWatchlistCurrencies Response: ${document.data}")
        document.data?.forEach { (key, value) ->
            if ("currencies" == key) {
                value as List<*>

                for (currency in value) {
                    watchlistCurrencies.add(WatchlistCurrency(currency.toString()))
                }
            }
        }

        return watchlistCurrencies
    }

    override suspend fun addWatchlistCurrency(watchlistCurrency: WatchlistCurrency){
        val watchlistCurrencies = getWatchlistCurrencies()

        if (watchlistCurrencies.isNotEmpty()) {
            FirebaseUtils().fireStoreDatabase.collection("watchlists")
                .document(firebaseUser?.email.toString())
                .update("currencies", FieldValue.arrayUnion(watchlistCurrency.coinName))
                .await()
        } else  {
            FirebaseUtils().fireStoreDatabase.collection("watchlists")
                .document(firebaseUser?.email.toString())
                .set(mapOf("currencies" to Collections.singletonList(watchlistCurrency.coinName)))
                .await()
        }

    }

    override suspend fun removeWatchlistCurrency(watchlistCurrency: WatchlistCurrency): List<WatchlistCurrency> {
        FirebaseUtils().fireStoreDatabase.collection("watchlists")
            .document(firebaseUser?.email.toString())
            .update("currencies", FieldValue.arrayRemove(watchlistCurrency.coinName))
            .await()

        return getWatchlistCurrencies()
    }
}