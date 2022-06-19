package com.example.digitalreceipts.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.digitalreceipts.R
import com.example.digitalreceipts.viewmodel.ReceiptsListViewModel

class ReceiptsListFragment : Fragment() {

    companion object {
        fun newInstance() = ReceiptsListFragment()
    }

    private lateinit var viewModel: ReceiptsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.receipts_list_fragment, container, false)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReceiptsListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}