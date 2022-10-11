package com.example.digitalreceipts.ui.receiptsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.digitalreceipts.databinding.ReceiptsDetailsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReceiptsDetailsFragment : Fragment() {
    private val mViewModel: ReceiptsDetailsViewModel by viewModel()

    private val binding: ReceiptsDetailsFragmentBinding by lazy {
        ReceiptsDetailsFragmentBinding.inflate(layoutInflater)
    }

    private val arguments by navArgs<ReceiptsDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /**
         * Define o campo _receivedCard no ViewModel caso receba um
         * cartão como argumento na navegação, i.e., vai editar um cartão
         * existente ao invés de criar um novo
         */
        arguments.receipts.let {
            mViewModel.receiveReceipt(it)
        }

        binding.viewModel = mViewModel

        return binding.root
    }
}