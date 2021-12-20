package com.dalhousie.rapid_crypto_app.models

class CurrencyDetail(var coinName: String,
                     var coinSymbol: String,
                     var coinImage: String,
                     var quantity: Double,
                     var soldFor: Double,
                     var ath : Double,
                     var atl : Double) {
}