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

/**
 * Fragmento responsável pelo controle de UI da tela de "esqueci minha senha".
 */
class ForgotPasswordFragment : Fragment() {

    // Instância do ViewModel através da injeção de dependência.
    private val mViewModel: ForgotPasswordViewModel by viewModel()

    // Ligando o layout com o binding para acesso dos componentes.
    private val binding: ForgotPasswordFragmentBinding by lazy {
        ForgotPasswordFragmentBinding.inflate(layoutInflater)
    }

    // Instância do dialog de loading.
    private lateinit var mProgressHUD: ProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Click do botão de enviar a verificação.
        binding.btSendVerification.setOnClickListener {

            // Cria o body que será passado para resetar a senha.
            val resetPassword = ResetPassword(
                binding.etEmail.text.toString(),
                binding.etCpf.text.toString(),
                binding.etPhone.text.toString()
            )

            // Cria o dialog de loading.
            mProgressHUD = ProgressHUD.show(
                context, "Resetting password",
                cancelable = false,
                spinnerGone = false
            )

            mProgressHUD.show()

            // Envia a requisição.
            mViewModel.sendResetEmail(resetPassword)
        }

        // Resposta da API depois da requisição.
        mViewModel.apiResponse.observe(viewLifecycleOwner) {
            Log.i("JAO", "Observer code result: $it")

            // Dispensa o dialog de loading anterior.
            mProgressHUD.dismiss()

            // Cria dialog com a reposta da requisição vinda do servidor.
            mProgressHUD = ProgressHUD.show(
                context, it.resultMessage,
                cancelable = true,
                spinnerGone = true
            )

            mProgressHUD.show()

            // Se a requisição foi realizada com sucesso, navega para a tela anterior.
            if (it.code == 200) {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }
}