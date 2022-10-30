package com.example.digitalreceipts.ui.main.login.newaccount

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

/**
 * Fragmento responsável pelo controle de UI da tela de criação de usuário.
 */
class CreateNewAccountFragment : Fragment() {

    // Instância do ViewModel através da injeção de dependência.
    private val mViewModel: CreateNewAccountViewModel by viewModel()

    // Ligando o layout com o binding para acesso dos componentes.
    private val binding: CreateNewAccountFragmentBinding by lazy {
        CreateNewAccountFragmentBinding.inflate(layoutInflater)
    }

    // Instância do dialog de loading.
    private lateinit var mProgressHUD: ProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Click do botão de criar nova conta.
        binding.btRegister.setOnClickListener {

            // Cria o body com os dados necessários para criar a nova conta.
            val newUser = NewUser(
                binding.etName.text.toString(),
                binding.etEmail.text.toString(),
                binding.etPass.text.toString(),
                binding.etCpf.text.toString(),
                binding.etPhone.text.toString(),
                binding.scAgree.isChecked
            )

            // Cria o dialog de loading.
            mProgressHUD = ProgressHUD.show(
                context, "Creating user",
                cancelable = false,
                spinnerGone = false
            )

            mProgressHUD.show()

            // Envia a requisição com o novo usuário.
            // TODO fazer o tratamento do "agree terms"
            mViewModel.createNewUser(newUser)
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
            if (it.code == 201) {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }
}