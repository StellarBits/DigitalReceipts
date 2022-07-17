package com.example.digitalreceipts.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.digitalreceipts.R
import com.example.digitalreceipts.adapter.ReceiptsListAdapter
import com.example.digitalreceipts.databinding.ReceiptsListFragmentBinding
import com.example.digitalreceipts.viewmodel.ReceiptsListViewModel

class ReceiptsListFragment : Fragment(), ReceiptsListAdapter.OnReceiptListener {

    private val viewModel: ReceiptsListViewModel by viewModels()
    private lateinit var mView: View

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
        binding.receiptsRecyclerview.setHasFixedSize(true)
        binding.receiptsRecyclerview.adapter = ReceiptsListAdapter(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
    }

    override fun onReceiptClick(position: Int) {
        val navController = Navigation.findNavController(mView)
        navController.navigate(R.id.action_receiptsListFragment_to_receiptsDetailsFragment)

        Log.i("JAO", "onReceiptClick: $position")
    }
}