package com.example.digitalreceipts.di

import com.example.digitalreceipts.ui.home.HomeScreenViewModel
import com.example.digitalreceipts.ui.main.login.forgotpassword.ForgotPasswordViewModel
import com.example.digitalreceipts.ui.main.login.login.LoginScreenViewModel
import com.example.digitalreceipts.ui.main.login.newaccount.CreateNewAccountViewModel
import com.example.digitalreceipts.ui.receiptsdetails.ReceiptsDetailsViewModel
import com.example.digitalreceipts.ui.receiptslist.ReceiptsListViewModel
import com.example.digitalreceipts.usecase.ApplySearchFilterUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginScreenViewModel() }
    viewModel { (applySearchFilterUseCase: ApplySearchFilterUseCase) ->
        ReceiptsListViewModel(
            applySearchFilterUseCase = applySearchFilterUseCase
        )
    }
    viewModel { ReceiptsDetailsViewModel() }
    viewModel { CreateNewAccountViewModel() }
    viewModel { ForgotPasswordViewModel() }
    viewModel { HomeScreenViewModel() }
}