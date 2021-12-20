package com.dalhousie.rapid_crypto_app.fragments.currencies

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dalhousie.rapid_crypto_app.databinding.FragmentCurrencyDetailBinding
import androidx.activity.OnBackPressedCallback
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.PortfolioCurrency
import com.dalhousie.rapid_crypto_app.models.Transaction
import com.dalhousie.rapid_crypto_app.models.WatchlistCurrency
import com.dalhousie.rapid_crypto_app.utils.DownloadImageFromInternet
import com.dalhousie.rapid_crypto_app.viewmodels.CurrenciesViewModel
import com.dalhousie.rapid_crypto_app.viewmodels.PortfolioViewModel
import com.dalhousie.rapid_crypto_app.viewmodels.TransactionsViewModel
import com.dalhousie.rapid_crypto_app.viewmodels.WatchlistViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.snackbar.Snackbar
import com.github.mikephil.charting.data.LineData


class CurrencyDetailFragment : Fragment() {

    private var _binding: FragmentCurrencyDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var currencyDetail : CoinGeckoCoinDetail
    private val currenciesViewModel: CurrenciesViewModel by activityViewModels()
    private lateinit var portfolioCurrency : PortfolioCurrency
    private lateinit var watchlistCurrency : WatchlistCurrency
    private lateinit var transaction : Transaction

    private val portfolioViewModel: PortfolioViewModel by activityViewModels()
    private val watchlistViewModel: WatchlistViewModel by activityViewModels()
    private val transactionsViewModel: TransactionsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyDetail = currenciesViewModel.selectedCurrency!!

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCurrencyDetailBinding.inflate(inflater, container, false)
        Log.w(ContentValues.TAG, "${currencyDetail} clicked in detail")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.currencyDetailCurrentPriceValue.text = "$"  + currencyDetail.currentPrice.toString()
        binding.currencyDetailAthValue.text = "$"  +currencyDetail.ath.toString()
        binding.currencyDetailAtlValue.text = "$"  +currencyDetail.atl.toString()
        DownloadImageFromInternet(binding.currencyDetailItemImage).execute(currencyDetail.image)
        binding.currencyDetailItemTitle.text = currencyDetail.name
        binding.currencyDetailPriceChangePercentage24.text = (Math.round(currencyDetail.priceChangePercentage24h * 100.0) / 100.0).toString() + "%"

        if (currencyDetail.priceChangePercentage24h < 0.0) {
            binding.currencyDetailPriceChangePercentage24.setTextColor(Color.parseColor("#8B0000"))
        } else {
            binding.currencyDetailPriceChangePercentage24.setTextColor(Color.parseColor("#006400"))
        }

        var dataObjects = currencyDetail.sparkline_in_7d

        val chart: LineChart = view.findViewById(R.id.chart) as LineChart

        val entries = ArrayList<Entry>()

        for ((counter, data) in dataObjects.withIndex()) {
            entries.add(Entry(counter.toFloat(), data.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Label")

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()

        binding.currencyDetailAtl.setTextColor(Color.parseColor("#79BAEC"))
        binding.currencyDetailAth.setTextColor(Color.parseColor("#79BAEC"))


        handleBuyButtonClickEvent()
        handleAddToWatchListClickEvent()

        portfolioViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                portfolioViewModel.onSnackbarShown()
            }
        }

        watchlistViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                watchlistViewModel.onSnackbarShown()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_navigation_currency_detail_to_navigation_currencies)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleBuyButtonClickEvent()
    {
        var pcurrencyBuy: Button? = view?.findViewById(R.id.currency_buy)
        pcurrencyBuy?.setOnClickListener{

            val alert: AlertDialog.Builder = AlertDialog.Builder(activity)

            alert.setTitle("Enter Quantity")
            alert.setMessage("Enter quantity of ${currencyDetail.name} you want to buy")

            val currencyBuyingDialogView =
                LayoutInflater.from(activity).inflate(R.layout.currency_buy_dialog, null)
            alert.setView(currencyBuyingDialogView)

            val editText: EditText = currencyBuyingDialogView.findViewById(R.id.buy_quantity_et)


            alert.setPositiveButton(
                "Buy"
            ) { _, _ ->
                val quantityValue = editText.text.toString()

                portfolioCurrency= PortfolioCurrency(currencyDetail.name,
                    currencyDetail.symbol,
                    currencyDetail.image,
                    quantityValue.toDouble(),
                    currencyDetail.currentPrice,
                    currencyDetail.currentPrice
                )

                //Calculating the total amount as quantity bought * current price of the coin
                val total = currencyDetail.currentPrice * quantityValue.toLong()

                transaction = Transaction(currencyDetail.name,
                currencyDetail.image,
                currencyDetail.symbol,
                    quantityValue.toLong(),
                    total.toLong(),
                    currencyDetail.currentPrice.toLong(),
                    "Buy"
                )

                portfolioViewModel.buyPortfolioCurrency(portfolioCurrency)
                transactionsViewModel.addTransaction(transaction)

            }

            alert.setNegativeButton(
                "Cancel"
            ) { _, _ -> }

            alert.show()
        }
    }

    private fun handleAddToWatchListClickEvent(){

        var addToWatchList: Button? = view?.findViewById(R.id.currency_add_to_watchlist)
        addToWatchList?.setOnClickListener{

            watchlistCurrency = WatchlistCurrency(currencyDetail.name)
            watchlistViewModel.addWatchlistCurrency(watchlistCurrency)
        }

    }


}