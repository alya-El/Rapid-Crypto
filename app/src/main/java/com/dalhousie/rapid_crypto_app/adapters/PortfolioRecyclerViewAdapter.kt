package com.dalhousie.rapid_crypto_app.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.utils.DownloadImageFromInternet
import java.math.BigDecimal
import java.math.RoundingMode

class PortfolioRecyclerViewAdapter : RecyclerView.Adapter<PortfolioRecyclerViewAdapter.PortfolioRowItem>() {

    var onSellButtonClick: ((PortfolioCurrency) -> Unit)? = null
    private var portfolioCurrencies = emptyList<PortfolioCurrency>()
    private var coinGeckoCoinMap = emptyMap<String, CoinGeckoCoinDetail>()

    inner class PortfolioRowItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var portfolioItemImage: ImageView = itemView.findViewById(R.id.portfolio_item_image)
        var portfolioItemTitle: TextView = itemView.findViewById(R.id.portfolio_item_title)
        var portfolioCurrentPrice: TextView = itemView.findViewById(R.id.portfolio_current_price_value)
        var portfolioQuantity: TextView = itemView.findViewById(R.id.portfolio_quantity_value)
        var portfolioBoughtFor: TextView = itemView.findViewById(R.id.portfolio_bought_for_value)
        var portfolioSell: Button = itemView.findViewById(R.id.portfolio_sell)

        init {
            portfolioSell.setOnClickListener {
                onSellButtonClick?.invoke(portfolioCurrencies[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioRowItem {
        val portfolioRowItemView = LayoutInflater.from(parent.context).inflate(R.layout.portfolio_row_item, parent, false)
        return PortfolioRowItem(portfolioRowItemView)
    }

    override fun getItemCount(): Int {
        return portfolioCurrencies.size
    }

    override fun onBindViewHolder(holder: PortfolioRowItem, position: Int) {
        val portfolioCurrency: PortfolioCurrency = portfolioCurrencies[position]

        holder.portfolioItemTitle.text = portfolioCurrency.coinName

        val currentPrice = coinGeckoCoinMap[portfolioCurrency.coinName]?.currentPrice?.let {
            BigDecimal(
                it
            ).setScale(2, RoundingMode.HALF_EVEN)
        }

        if (currentPrice != null) {
            portfolioCurrency.currentPrice = currentPrice.toDouble()
        }

        holder.portfolioCurrentPrice.text = "$$currentPrice"

        val quantity = BigDecimal(portfolioCurrency.quantity).setScale(2, RoundingMode.HALF_EVEN)
        holder.portfolioQuantity.text = quantity.toString()

        holder.portfolioBoughtFor.text = "$" + portfolioCurrency.boughtFor.toString()
        if (currentPrice != null) {
            if (portfolioCurrency.boughtFor < currentPrice.toDouble()) {
                holder.portfolioBoughtFor.setTextColor(Color.parseColor("#8B0000"))
            } else {
                holder.portfolioBoughtFor.setTextColor(Color.parseColor("#006400"))
            }
        }

        // get coin image from internet by using the image link
        DownloadImageFromInternet(holder.portfolioItemImage).execute(portfolioCurrency.coinImage)
    }

    fun setPortfolioCurrencies(portfolioCurrencies: List<PortfolioCurrency>) {
        this.portfolioCurrencies = portfolioCurrencies
        notifyDataSetChanged()
    }

    fun setCoinGeckoCoins(coinGeckoCoinMap: Map<String, CoinGeckoCoinDetail>) {
        this.coinGeckoCoinMap = coinGeckoCoinMap
        notifyDataSetChanged()
    }
}