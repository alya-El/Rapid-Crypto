package com.dalhousie.rapid_crypto_app.adapters

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.WatchlistCurrency
import com.dalhousie.rapid_crypto_app.utils.DownloadImageFromInternet
import java.math.BigDecimal
import java.math.RoundingMode

class WatchlistRecyclerViewAdapter :
    RecyclerView.Adapter<WatchlistRecyclerViewAdapter.WatchlistRowItem>() {

    var onWatchlistItemRemoveClick: ((WatchlistCurrency) -> Unit)? = null
    private var watchlistCoinDetails = emptyList<WatchlistCurrency>()
    private var coinGeckoCoinMap = emptyMap<String, CoinGeckoCoinDetail>()


    inner class WatchlistRowItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var watchlistItemImage: ImageView = itemView.findViewById(R.id.watchlist_item_image)
        var watchlistItemTitle: TextView = itemView.findViewById(R.id.watchlist_item_title)
        var watchlistCurrentPrice: TextView = itemView.findViewById(R.id.watchlist_current_price_value)
        var watchlistPriceChangePercentage24: TextView = itemView.findViewById(R.id.watchlist_price_change_percentage_24)

        init {
            itemView.findViewById<ImageButton>(R.id.watchlist_remove_button).setOnClickListener {
                onWatchlistItemRemoveClick?.invoke(watchlistCoinDetails[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistRowItem {
        val currenciesRowItemView =
            LayoutInflater.from(parent.context).inflate(R.layout.watchlist_row_item, parent, false)
        return WatchlistRowItem(currenciesRowItemView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: WatchlistRowItem, position: Int) {
        val watchlistCurrency: WatchlistCurrency = watchlistCoinDetails[position]
        val coinGeckoCoin: CoinGeckoCoinDetail = coinGeckoCoinMap.getOrDefault(
            watchlistCurrency.coinName,
            CoinGeckoCoinDetail("", "", "", "", 0.0, 0.0, 0.0, 0.0, ArrayList<Double>())
        )

        holder.watchlistItemTitle.text =
            coinGeckoCoin.name + " (" + coinGeckoCoin.symbol.uppercase() + ")"

        val currentPrice =
            BigDecimal(coinGeckoCoin.currentPrice).setScale(2, RoundingMode.HALF_EVEN)
        holder.watchlistCurrentPrice.text = "$$currentPrice"
        holder.watchlistPriceChangePercentage24.text =
            (Math.round(coinGeckoCoin.priceChangePercentage24h * 100.0) / 100.0).toString() + "%"

        if (coinGeckoCoin.priceChangePercentage24h < 0.0) {
            holder.watchlistPriceChangePercentage24.setTextColor(Color.parseColor("#8B0000"))
        } else {
            holder.watchlistPriceChangePercentage24.setTextColor(Color.parseColor("#006400"))
        }
        DownloadImageFromInternet(holder.watchlistItemImage).execute(coinGeckoCoin.image)
    }

    override fun getItemCount(): Int {
        return watchlistCoinDetails.size
    }

    fun setWatchlistCoinDetails(watchlistCoinDetails: List<WatchlistCurrency>) {
        this.watchlistCoinDetails = watchlistCoinDetails
        notifyDataSetChanged()
    }

    fun setCoinGeckoCoins(coinGeckoCoinMap: Map<String, CoinGeckoCoinDetail>) {
        this.coinGeckoCoinMap = coinGeckoCoinMap
        notifyDataSetChanged()
    }
}