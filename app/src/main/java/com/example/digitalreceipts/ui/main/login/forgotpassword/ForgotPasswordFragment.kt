package com.example.digitalreceipts.ui.main.login.forgotpassword

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.digitalreceipts.api.model.ResetPassword
import com.example.digitalreceipts.databinding.ForgotPasswordFragmentBinding
import com.example.digitalreceipts.ui.custom.dialog.ProgressHUD
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : Fragment() {

    private val mViewModel: ForgotPasswordViewModel by viewModel()

    private val binding: ForgotPasswordFragmentBinding by lazy {
        ForgotPasswordFragmentBinding.inflate(layoutInflater)
    }

    private lateinit var mProgressHUD: ProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.btSendVerification.setOnClickListener {
            val resetPassword = ResetPassword(
                binding.etEmail.text.toString(),
                binding.etCpf.text.toString(),
                binding.etPhone.text.toString()
            )

            mProgressHUD = ProgressHUD.show(
                context, "Resetting password",
                cancelable = true,
                spinnerGone = false
            )

            mProgressHUD.show()

            mViewModel.sendResetEmail(resetPassword)
        }

        mViewModel.apiResponse.observe(viewLifecycleOwner) {
            Log.i("JAO", "Observer code result: $it")

            mProgressHUD.dismiss()

            mProgressHUD = ProgressHUD.show(
                context, it.resultMessage,
                cancelable = true,
                spinnerGone = true
            )

            mProgressHUD.show()

            if (it.code == 200) {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }
}