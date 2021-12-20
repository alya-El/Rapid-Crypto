package com.dalhousie.rapid_crypto_app.fragments.portfolio

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.adapters.PortfolioRecyclerViewAdapter
import com.dalhousie.rapid_crypto_app.constants.MessageConstants
import com.dalhousie.rapid_crypto_app.databinding.FragmentPortfolioBinding
import com.dalhousie.rapid_crypto_app.models.CoinGeckoCoinDetail
import com.dalhousie.rapid_crypto_app.models.Transaction
import com.dalhousie.rapid_crypto_app.viewmodels.PortfolioViewModel
import com.dalhousie.rapid_crypto_app.viewmodels.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.math.RoundingMode


class PortfolioFragment : Fragment() {

    private var _binding: FragmentPortfolioBinding? = null
    private val binding get() = _binding!!

    private var coinGeckoCoinMap = emptyMap<String, CoinGeckoCoinDetail>()


    private val portfolioViewModel: PortfolioViewModel by activityViewModels()
    private val transactionsViewModel: TransactionsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPortfolioBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val portfolioRecyclerViewAdapter = PortfolioRecyclerViewAdapter()
        binding.recyclerView.adapter = portfolioRecyclerViewAdapter

        loadAndObservePortfolioData(portfolioRecyclerViewAdapter)
        loadAndObserveCoinGeckoData(portfolioRecyclerViewAdapter)

        handleSellButtonClickEvent(portfolioRecyclerViewAdapter)

        portfolioViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                portfolioViewModel.onSnackbarShown()
            }
        }
    }

    private fun loadAndObservePortfolioData(portfolioRecyclerViewAdapter: PortfolioRecyclerViewAdapter) {
        portfolioViewModel.loadPortfolioData()
        portfolioViewModel.portfolioCurrenciesLiveData.observe(
            viewLifecycleOwner,
            { portfolioCurrencyList ->
                portfolioRecyclerViewAdapter.setPortfolioCurrencies(portfolioCurrencyList)
                var portfolioAmount = 0.0
                var boughtForTotal = 0.0
                for (portfolioCurrency in portfolioCurrencyList) {
                    //calculating total portfolio amount by adding all the currencies amount
                    portfolioAmount += (portfolioCurrency.quantity * portfolioCurrency.boughtFor)
                    boughtForTotal += (portfolioCurrency.quantity * portfolioCurrency.boughtFor)
                }

                val portfolioAmountRounded = BigDecimal(portfolioAmount).setScale(2, RoundingMode.HALF_EVEN)
                binding.portfolioTotalAmount.text = portfolioAmountRounded.toString()
            }
        )
    }

    private fun loadAndObserveCoinGeckoData(portfolioRecyclerViewAdapter: PortfolioRecyclerViewAdapter) {
        portfolioViewModel.loadCoinGeckoCoinData()
        portfolioViewModel.coinGeckoCoinLiveData.observe(
            viewLifecycleOwner,
            { coinGeckoCoinMap ->
                portfolioRecyclerViewAdapter.setCoinGeckoCoins(coinGeckoCoinMap)
            }
        )
    }

    private fun handleSellButtonClickEvent(portfolioRecyclerViewAdapter: PortfolioRecyclerViewAdapter) {
        portfolioRecyclerViewAdapter.onSellButtonClick = { portfolioCurrency ->

            val alert: AlertDialog.Builder = AlertDialog.Builder(activity)


            alert.setTitle("Enter Quantity")
            alert.setMessage("Enter quantity of ${portfolioCurrency.coinName} you want to sell")

            val portfolioSellingDialogView =
                LayoutInflater.from(activity).inflate(R.layout.portfolio_sell_dialog, null)
            alert.setView(portfolioSellingDialogView)

            val editText: EditText = portfolioSellingDialogView.findViewById(R.id.sell_quantity_et)

            alert.setPositiveButton(
                "Sell"
            ) { _, _ ->
                val quantityValue = editText.text.toString()
                if (portfolioCurrency.quantity < quantityValue.toDouble()) {
                    portfolioViewModel.loadPortfolioData()
                    view?.let {
                        Snackbar.make(
                            it,
                            MessageConstants.NOT_ENOUGH_QUANTITY_ERROR,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                } else {

                    //Calculating the total amount as quantity sold * current price of the coin
                    val total = portfolioCurrency.currentPrice?.toDouble()?.times(quantityValue.toDouble())

                        if (total != null) {
                           val transaction = Transaction(portfolioCurrency.coinName,
                                portfolioCurrency.coinImage,
                                portfolioCurrency.coinSymbol,
                                quantityValue.toLong(),
                                total.toLong(),
                                portfolioCurrency.currentPrice.toLong(),
                                "Sell"
                            )
                            transactionsViewModel.addTransaction(transaction)
                        }


                    portfolioViewModel.sellPortfolioCurrency(portfolioCurrency, quantityValue.toDouble())

                }
            }

            alert.setNegativeButton(
                "Cancel"
            ) { _, _ -> }

            alert.show()
        }
    }
}