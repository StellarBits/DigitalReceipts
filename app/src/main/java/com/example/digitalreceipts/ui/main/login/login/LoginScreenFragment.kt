package com.example.digitalreceipts.ui.main.login.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.digitalreceipts.api.model.LoginBody
import com.example.digitalreceipts.api.model.LoginResponse
import com.example.digitalreceipts.databinding.LoginScreenFragmentBinding
import com.example.digitalreceipts.ui.custom.dialog.ProgressHUD
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragmento responsável pelo controle de UI da tela de login.
 */
class LoginScreenFragment : Fragment() {

    // Instância do ViewModel através da injeção de dependência.
    private val mViewModel: LoginScreenViewModel by viewModel()

    // Ligando o layout com o binding para acesso dos componentes.
    private val binding: LoginScreenFragmentBinding by lazy {
        LoginScreenFragmentBinding.inflate(layoutInflater)
    }

    private lateinit var mActivity: Activity
    private lateinit var mView: View
    private lateinit var mProgressHUD: ProgressHUD

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Resposta da API depois da requisição de login.
        mViewModel.apiResponse.observe(viewLifecycleOwner) {

            // Caso tiver algum dialog sendo exibido, é dispensado.
            mProgressHUD.dismiss()

            // Se o login foi realizado com sucesso, navega para a tela da lista de recibos passando
            // o corpo da resposta como parâmetro (que depois será passado como argumento).
            // Caso não, exibe um dialog informando que não foi possível realizar o login.
            if (it.code() == 200) {
                navigateToReceiptsListFragment(it.body()!!)
            } else {
                mProgressHUD = ProgressHUD.show(
                    context,
                    "Problem accessing account.\n\nPlease review your e-mail and password and try again.",
                    cancelable = true,
                    spinnerGone = true
                )

                mProgressHUD.show()
            }
        }

        // Clique do botão de login.
        binding.btLogin.setOnClickListener {

            // Cria o corpo da requisição de login.
            val loginBody = LoginBody(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )

            // Cria dialog de loading.
            mProgressHUD = ProgressHUD.show(
                context, "Accessing",
                cancelable = false,
                spinnerGone = false
            )

            mProgressHUD.show()

            // Envia a requisição para o ViewModel.
            mViewModel.login(loginBody)
        }

        // Clique do botão de criar nova conta.
        binding.btCreateNewAccount.setOnClickListener {
            val direction = LoginScreenFragmentDirections.navigateToCreateNewAccount()
            findNavController().navigate(direction)
        }

        // Clique da TextView de "esqueci minha senha".
        binding.tvForgotPassword.setOnClickListener {
            val direction = LoginScreenFragmentDirections.navigateToForgotPassword()
            findNavController().navigate(direction)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mActivity = requireActivity()
        mView = view
    }

    // Quando na tela de login, esconde a barra de navegação.
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    // Quando fora da tela de login, exibe a barra de navegação.
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    // Navega para a tela da lista de recibos.
    private fun navigateToReceiptsListFragment(userData: LoginResponse) {
        val direction = LoginScreenFragmentDirections.navigateToReceiptsList(userData)
        findNavController().navigate(direction)
    }
}