package com.dalhousie.rapid_crypto_app.models

class Transaction(var coinName : String,
var coinImage : String,
var coinSymbol : String,
var quantity : Long,
var total : Long,
var price : Long,
var transactionType : String) {
}