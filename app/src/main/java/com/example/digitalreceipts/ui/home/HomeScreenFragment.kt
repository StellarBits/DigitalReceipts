package com.example.digitalreceipts.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.digitalreceipts.databinding.HomeScreenFragmentBinding
import com.example.digitalreceipts.ui.receiptsdetails.ReceiptsDetailsFragmentArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : Fragment() {
    private val mViewModel: HomeScreenViewModel by viewModel()

    private val binding: HomeScreenFragmentBinding by lazy {
        HomeScreenFragmentBinding.inflate(layoutInflater)
    }

    private val arguments by navArgs<HomeScreenFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments.userData.let {
            if (it != null) {
                binding.tvTest.text = it.name
            }
        }

        return binding.root
    }
}