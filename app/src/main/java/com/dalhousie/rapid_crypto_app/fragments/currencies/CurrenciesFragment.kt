package com.dalhousie.rapid_crypto_app.fragments.currencies

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalhousie.rapid_crypto_app.R
import com.dalhousie.rapid_crypto_app.adapters.CurrenciesRecyclerViewAdapter
import com.dalhousie.rapid_crypto_app.databinding.FragmentCurrenciesBinding
import com.dalhousie.rapid_crypto_app.viewmodels.CurrenciesViewModel

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!


    private val currenciesViewModel: CurrenciesViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.currenciesRecyclerView.layoutManager = LinearLayoutManager(activity)
        val currenciesRecyclerViewAdapter: CurrenciesRecyclerViewAdapter =
            CurrenciesRecyclerViewAdapter()
        binding.currenciesRecyclerView.adapter = currenciesRecyclerViewAdapter

        currenciesViewModel.loadCoinGeckoCoinData()

        currenciesViewModel.coinGeckoCoinLiveData.observe(
            viewLifecycleOwner,
            { coinGeckoCoinMap ->
                currenciesRecyclerViewAdapter.setCoinGeckoCoins(coinGeckoCoinMap.values.toList())
            }
        )

        handleCurrencyItemClickEvent(currenciesRecyclerViewAdapter )
    }

    private fun handleCurrencyItemClickEvent(currenciesRecyclerViewAdapter: CurrenciesRecyclerViewAdapter) {
        currenciesRecyclerViewAdapter.onCurrencyItemClick = { coinGeckoCoinDetail ->
            Log.w(ContentValues.TAG, "${coinGeckoCoinDetail.name} clicked")

            currenciesViewModel.selectedCurrency = coinGeckoCoinDetail

            findNavController().navigate(R.id.action_navigation_currencies_to_navigation_currency_detail )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}