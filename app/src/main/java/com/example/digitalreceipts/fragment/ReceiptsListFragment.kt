package com.example.digitalreceipts.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.digitalreceipts.adapter.ReceiptsListAdapter
import com.example.digitalreceipts.databinding.ReceiptsListFragmentBinding
import com.example.digitalreceipts.viewmodel.ReceiptsListViewModel

class ReceiptsListFragment : Fragment() {

    private val viewModel: ReceiptsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ReceiptsListFragmentBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView
        binding.receiptsRecyclerview.adapter = ReceiptsListAdapter()

        return binding.root

        // Simple test RecyclerView
        /*recyclerView = binding.receiptsRecyclerview
        recyclerAdapter = context?.let { ReceiptsListAdapter() }!!
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerAdapter

        viewModel._fieldList.value?.let { recyclerAdapter.setReceiptsListItems(it) }
        Log.i("JAO", "Fragment#onViewCreated")
        val list = listOf("Teste", "RecyclerView", "Lista", "Strings")
        recyclerAdapter.setReceiptsListItems(list)*/
    }
}