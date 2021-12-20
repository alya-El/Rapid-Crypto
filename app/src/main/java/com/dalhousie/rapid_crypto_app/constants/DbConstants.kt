package com.dalhousie.rapid_crypto_app.constants

object CoinGeckoURIs {
    private const val COIN_GECKO_BASE_URL = "https://api.coingecko.com/api/v3"
    const val GET_COIN_MARKET_URL = "$COIN_GECKO_BASE_URL/coins/markets?vs_currency=usd&per_page=250&sparkline=true"
}