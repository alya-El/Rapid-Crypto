package com.dalhousie.rapid_crypto_app.models

data class PortfolioCurrency(var coinName: String,
                             var coinSymbol: String,
                             var coinImage: String,
                             var quantity: Double,
                             var boughtFor: Double,
var currentPrice : Double) {

}