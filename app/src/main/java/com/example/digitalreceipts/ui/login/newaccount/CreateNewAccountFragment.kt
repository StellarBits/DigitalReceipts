package com.example.digitalreceipts.ui.login.newaccount

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.digitalreceipts.api.model.NewUser
import com.example.digitalreceipts.databinding.CreateNewAccountFragmentBinding
import com.example.digitalreceipts.ui.custom.dialog.ProgressHUD
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateNewAccountFragment : Fragment() {

    private val mViewModel: CreateNewAccountViewModel by viewModel()

    private val binding: CreateNewAccountFragmentBinding by lazy {
        CreateNewAccountFragmentBinding.inflate(layoutInflater)
    }

    private lateinit var mProgressHUD: ProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.btRegister.setOnClickListener {
            val newUser = NewUser(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPass.text.toString(),
                binding.etCpf.text.toString(),
                binding.etPhone.text.toString(),
                binding.scAgree.isChecked
            )

            mProgressHUD = ProgressHUD.show(
                context, "Loading",
                indeterminate = true,
                cancelable = true
            )

            mProgressHUD.show()

            mViewModel.createNewUser(newUser)
        }

        mViewModel.test.observe(viewLifecycleOwner) {
            Log.i("JAO", "Observer code result: $it")

            mProgressHUD.dismiss()

            if (it == 201) {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }
}