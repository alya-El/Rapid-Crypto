package com.dalhousie.rapid_crypto_app.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.utils.DownloadImageFromInternet
import java.math.BigDecimal
import java.math.RoundingMode

class CurrenciesRecyclerViewAdapter : RecyclerView.Adapter<CurrenciesRecyclerViewAdapter.CurrenciesRowItem>(){

    var onCurrencyItemClick: ((CoinGeckoCoinDetail) -> Unit)? = null
    private var coinGeckoCoins = emptyList<CoinGeckoCoinDetail>()


    inner class CurrenciesRowItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var currenciesItemImage: ImageView = itemView.findViewById(R.id.currencies_item_image)
        var currenciesItemTitle: TextView = itemView.findViewById(R.id.currencies_item_title)
        var currenciesCurrentPrice: TextView = itemView.findViewById(R.id.currencies_current_price_value)
        var currenciesPriceChangePercentage24: TextView = itemView.findViewById(R.id.currenices_price_change_percentage_24)

        init {
            itemView.setOnClickListener {
                onCurrencyItemClick?.invoke(coinGeckoCoins[adapterPosition])
            }
        }
    }

    fun setCoinGeckoCoins(coinGeckoCoins: List<CoinGeckoCoinDetail>) {
        this.coinGeckoCoins = coinGeckoCoins
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesRowItem {
        val currenciesRowItemView = LayoutInflater.from(parent.context).inflate(R.layout.currencies_row_item, parent, false)
        return CurrenciesRowItem(currenciesRowItemView)
    }

    override fun onBindViewHolder(holder: CurrenciesRowItem, position: Int) {
        val coinGeckoCoin: CoinGeckoCoinDetail = coinGeckoCoins[position]

        holder.currenciesItemTitle.text = coinGeckoCoin.name + " (" + coinGeckoCoin.symbol.uppercase() + ")"

        val currentPrice = BigDecimal(coinGeckoCoin.currentPrice).setScale(2, RoundingMode.HALF_EVEN)
        holder.currenciesCurrentPrice.text = "$$currentPrice"
        holder.currenciesPriceChangePercentage24.text = (Math.round(coinGeckoCoin.priceChangePercentage24h * 100.0) / 100.0).toString() + "%"

        if (coinGeckoCoin.priceChangePercentage24h < 0.0) {
            holder.currenciesPriceChangePercentage24.setTextColor(Color.parseColor("#8B0000"))
        } else {
            holder.currenciesPriceChangePercentage24.setTextColor(Color.parseColor("#006400"))
        }
        DownloadImageFromInternet(holder.currenciesItemImage).execute(coinGeckoCoin.image)
    }

    override fun getItemCount(): Int {
        return coinGeckoCoins.size
    }
}