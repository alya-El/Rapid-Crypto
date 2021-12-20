package com.dalhousie.rapid_crypto_app.persistence.dao.impl

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.dalhousie.rapid_crypto_app.constants.CoinGeckoURIs
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.RequestQueueHolder
import com.dalhousie.rapid_crypto_app.persistence.dao.CoinGeckoDAO


class CoinGeckoDAOImpl(private val context: Context) : CoinGeckoDAO {
    override fun getCoinMarkets(): LiveData<Map<String, CoinGeckoCoinDetail>> {
        val coinGeckoCoinDetailsLiveData = MutableLiveData<Map<String, CoinGeckoCoinDetail>>()

        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, CoinGeckoURIs.GET_COIN_MARKET_URL, null,
            { response ->
                Log.w(ContentValues.TAG, "CoinGeckoDAOImpl getCoinMarkets Response: %s".format(response.toString()))

                val coinGeckoCoinDetails = HashMap<String, CoinGeckoCoinDetail>()
                for (coinIndex in 0 until response.length()) {
                    val coinDetail = response.getJSONObject(coinIndex)

                    val price = coinDetail.getJSONObject("sparkline_in_7d").getJSONArray("price")

                    val priceList = ArrayList<Double>()
                    for (i in 0 until price.length()) {
                        priceList.add(price.getDouble(i))
                    }

                    coinGeckoCoinDetails[coinDetail.getString("name")] = CoinGeckoCoinDetail(
                        coinDetail.getString("id"),
                        coinDetail.getString("symbol"),
                        coinDetail.getString("name"),
                        coinDetail.getString("image"),
                        coinDetail.getDouble("current_price"),
                        coinDetail.getDouble("price_change_percentage_24h"),
                        coinDetail.getDouble("ath"),
                        coinDetail.getDouble("atl"),
                        priceList
                    )
                }

                coinGeckoCoinDetailsLiveData.value = coinGeckoCoinDetails
            },
            {
                Log.w(ContentValues.TAG, "Error getting coins data from coin gecko API ${it.message}")
            }
        )

        RequestQueueHolder.getInstance(context).addToRequestQueue(jsonObjectRequest)

        return coinGeckoCoinDetailsLiveData
    }
}