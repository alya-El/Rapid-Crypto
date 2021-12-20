package com.dalhousie.rapid_crypto_app.fragments.watchlist

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalhousie.rapid_crypto_app.adapters.WatchlistRecyclerViewAdapter
import com.dalhousie.rapid_crypto_app.databinding.FragmentWatchlistBinding
import com.dalhousie.rapid_crypto_app.viewmodels.WatchlistViewModel
import com.google.android.material.snackbar.Snackbar

class WatchlistFragment : Fragment() {

    private lateinit var watchlistViewModel: WatchlistViewModel
    private var _binding: FragmentWatchlistBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        watchlistViewModel =
            ViewModelProvider(this).get(WatchlistViewModel::class.java)

        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.watchlistRecyclerView.layoutManager = LinearLayoutManager(activity)
        val watchlistRecyclerViewAdapter = WatchlistRecyclerViewAdapter()
        binding.watchlistRecyclerView.adapter = watchlistRecyclerViewAdapter

        loadAndObserveWatchlistData(watchlistRecyclerViewAdapter)
        loadAndObserveCoinGeckoData(watchlistRecyclerViewAdapter)

        handleRemoveButtonClickEvent(watchlistRecyclerViewAdapter)

        watchlistViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                watchlistViewModel.onSnackbarShown()
            }
        }
    }

    private fun loadAndObserveWatchlistData(watchlistRecyclerViewAdapter: WatchlistRecyclerViewAdapter) {
        watchlistViewModel.loadWatchlistData()
        watchlistViewModel.watchlistCurrenciesLiveData.observe(
            viewLifecycleOwner,
            { watchlistCurrencies ->
                watchlistRecyclerViewAdapter.setWatchlistCoinDetails(watchlistCurrencies)
            }
        )
    }

    private fun loadAndObserveCoinGeckoData(watchlistRecyclerViewAdapter: WatchlistRecyclerViewAdapter) {
        watchlistViewModel.loadCoinGeckoCoinData()
        watchlistViewModel.coinGeckoCoinLiveData.observe(
            viewLifecycleOwner,
            { coinGeckoCoinMap ->
                watchlistRecyclerViewAdapter.setCoinGeckoCoins(coinGeckoCoinMap)
            }
        )
    }

    private fun handleRemoveButtonClickEvent(watchlistRecyclerViewAdapter: WatchlistRecyclerViewAdapter) {
        watchlistRecyclerViewAdapter.onWatchlistItemRemoveClick = { watchlistCurrency ->
            val alert: AlertDialog.Builder = AlertDialog.Builder(activity)

            alert.setTitle("Remove From Watchlist")
            alert.setMessage("Are you sure you want to remove ${watchlistCurrency.coinName} from watchlist?")

            alert.setPositiveButton(
                "Yes"
            ) { _, _ ->
                watchlistViewModel.removeWatchlistCurrency(watchlistCurrency)
            }

            alert.setNegativeButton(
                "No"
            ) { _, _ -> }

            alert.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}