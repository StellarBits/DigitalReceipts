package com.example.digitalreceipts.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.digitalreceipts.databinding.HomeScreenFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeScreenFragment : Fragment() {
    private val mViewModel: HomeScreenViewModel by viewModel()

    private val binding: HomeScreenFragmentBinding by lazy {
        HomeScreenFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }
}