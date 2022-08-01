package com.example.digitalreceipts.ui.receiptsdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.digitalreceipts.R

class ReceiptsDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ReceiptsDetailsFragment()
    }

    private lateinit var viewModel: ReceiptsDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.receipts_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[ReceiptsDetailsViewModel::class.java]
        // TODO: Use the ViewModel
    }

}