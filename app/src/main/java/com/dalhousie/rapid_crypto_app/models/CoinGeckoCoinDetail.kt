package com.dalhousie.rapid_crypto_app.models

import java.io.Serializable
import java.lang.reflect.Array

data class CoinGeckoCoinDetail(var id: String,
                               var symbol: String,
                               var name: String,
                               var image: String,
                               var currentPrice: Double,
                               var priceChangePercentage24h : Double,
                               var ath : Double,
                               var atl : Double,
                               var sparkline_in_7d : ArrayList<Double>

                              ) : Serializable
{

}