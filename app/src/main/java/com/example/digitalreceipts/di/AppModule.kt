package com.example.digitalreceipts.di

import com.example.digitalreceipts.ui.login.LoginScreenViewModel
import com.example.digitalreceipts.ui.receiptsdetails.ReceiptsDetailsViewModel
import com.example.digitalreceipts.ui.receiptslist.ReceiptsListViewModel
import com.example.digitalreceipts.usecase.ApplySearchFilterUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    //viewModel { LoginScreenViewModel() }
    viewModel { (applySearchFilterUseCase: ApplySearchFilterUseCase) ->
        ReceiptsListViewModel(
            applySearchFilterUseCase
        )
    }
    //viewModel { ReceiptsDetailsViewModel() }
}