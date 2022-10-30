package com.example.digitalreceipts.ui.receiptsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.digitalreceipts.databinding.ReceiptsDetailsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragmento responsável pelo controle de UI da tela de detalhes dos recibos.
 */
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

        // Recebe os dados do recibo selecionado como argumento da lista de recibos.
        // Sei que não é uma boa prática tratar dados de UI no ViewModel, mas segundo
        // o que entendi, ecolhi alimentar os campos para exibição no ViewModel para
        // exercitar melhor o DataBinding e o LiveData/MutableLiveData, uma vez que
        // notei que as referências passadas no XML vêm sempre do ViewModel.
        arguments.receipts.let {
            mViewModel.receiveReceipt(it)
        }

        binding.viewModel = mViewModel

        return binding.root
    }
}