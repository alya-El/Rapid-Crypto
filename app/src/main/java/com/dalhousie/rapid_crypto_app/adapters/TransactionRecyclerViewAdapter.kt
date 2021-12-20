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
import com.dalhousie.rapid_crypto_app.models.Transaction
import com.dalhousie.rapid_crypto_app.utils.DownloadImageFromInternet
import java.math.BigDecimal
import java.math.RoundingMode

class TransactionRecyclerViewAdapter: RecyclerView.Adapter<TransactionRecyclerViewAdapter.TransactionRowItem>()  {

    private var transactions = emptyList<Transaction>()
    private var coinGeckoCoinMap = emptyMap<String, CoinGeckoCoinDetail>()

    inner class TransactionRowItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var transactionItemImage: ImageView = itemView.findViewById(R.id.transaction_item_image)
        var transactionItemTitle: TextView = itemView.findViewById(R.id.transaction_item_title)
        var transactionQuantity: TextView = itemView.findViewById(R.id.transaction_quantity_value)
        var transactionTotal: TextView = itemView.findViewById(R.id.transaction_total_value)
        var transactionType: TextView = itemView.findViewById(R.id.transaction_type)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionRowItem {
        val transactionRowItemView = LayoutInflater.from(parent.context).inflate(R.layout.transaction_row_item, parent, false)
        return TransactionRowItem(transactionRowItemView)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionRowItem, position: Int) {
        val transaction: Transaction = transactions[position]

        holder.transactionItemTitle.text = transaction.coinName

        holder.transactionType.text = transaction.transactionType

        holder.transactionTotal.text = "$" + transaction.total.toString()

        val quantity = BigDecimal(transaction.quantity).setScale(2, RoundingMode.HALF_EVEN)
        holder.transactionQuantity.text = quantity.toString() + " " + transaction.coinSymbol.uppercase()

        if (holder.transactionType.text != null) {
            if (transaction.transactionType == "Sell") {
                holder.transactionType.setTextColor(Color.parseColor("#8B0000"))
            } else {
                holder.transactionType.setTextColor(Color.parseColor("#006400"))
            }
        }

        DownloadImageFromInternet(holder.transactionItemImage).execute(transaction.coinImage)
    }

    fun setTransactions(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }

    fun setCoinGeckoCoins(coinGeckoCoinMap: Map<String, CoinGeckoCoinDetail>) {
        this.coinGeckoCoinMap = coinGeckoCoinMap
        notifyDataSetChanged()
    }
}