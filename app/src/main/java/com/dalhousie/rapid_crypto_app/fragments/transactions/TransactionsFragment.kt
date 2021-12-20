package com.dalhousie.rapid_crypto_app.fragments.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalhousie.rapid_crypto_app.adapters.TransactionRecyclerViewAdapter
import com.dalhousie.rapid_crypto_app.databinding.FragmentTransactionsBinding
import com.dalhousie.rapid_crypto_app.viewmodels.TransactionsViewModel
import com.google.android.material.snackbar.Snackbar

class TransactionsFragment : Fragment() {

    private val transactionsViewModel: TransactionsViewModel by activityViewModels()
    private var _binding: FragmentTransactionsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(activity)
        val transactionRecyclerViewAdapter = TransactionRecyclerViewAdapter()
        binding.transactionRecyclerView.adapter = transactionRecyclerViewAdapter

        loadAndObserveTransactionData(transactionRecyclerViewAdapter)
        loadAndObserveCoinGeckoData(transactionRecyclerViewAdapter)


        transactionsViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                transactionsViewModel.onSnackbarShown()
            }
        }
    }

    private fun loadAndObserveTransactionData(transactionRecyclerViewAdapter: TransactionRecyclerViewAdapter) {
        transactionsViewModel.loadTransactionData()
        transactionsViewModel.transactionsLiveData.observe(
            viewLifecycleOwner,
            { transactions ->
                transactionRecyclerViewAdapter.setTransactions(transactions)
            }
        )
    }

    private fun loadAndObserveCoinGeckoData(transactionRecyclerViewAdapter: TransactionRecyclerViewAdapter) {
        transactionsViewModel.loadCoinGeckoCoinData()
        transactionsViewModel.coinGeckoCoinLiveData.observe(
            viewLifecycleOwner,
            { coinGeckoCoinMap ->
                transactionRecyclerViewAdapter.setCoinGeckoCoins(coinGeckoCoinMap)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}