package com.example.digitalreceipts.ui.receiptslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digitalreceipts.R
import com.example.digitalreceipts.api.model.LoginResponse
import com.example.digitalreceipts.api.model.NewReceipt
import com.example.digitalreceipts.api.model.Receipt
import com.example.digitalreceipts.databinding.ReceiptsListFragmentBinding
import com.example.digitalreceipts.ui.adapter.ReceiptsAdapter
import com.example.digitalreceipts.ui.adapter.ReceiptsListener
import com.example.digitalreceipts.usecase.ApplySearchFilterUseCase
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Fragmento responsável pelo controle de UI da tela de exibição da lista de recibos.
 */
class ReceiptsListFragment : Fragment() {

    private lateinit var mToken: String
    private lateinit var mUserData: LoginResponse

    // Instância do ViewModel através da injeção de dependência passando os filtros
    // que serão usados para buscar os recibos na RecyclerView.
    private val mViewModel: ReceiptsListViewModel by viewModel {
        parametersOf(ApplySearchFilterUseCase())
    }
    private lateinit var mView: View

    // Ligando o layout com o binding para acesso dos componentes.
    private val binding: ReceiptsListFragmentBinding by lazy {
        ReceiptsListFragmentBinding.inflate(layoutInflater)
    }

    private val arguments by navArgs<ReceiptsListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        // Permite que o Data Binding observe o LiveData pelo ciclo de vida deste fragmento.
        binding.lifecycleOwner = this

        // Dá acesso do binding ao ViewModel.
        binding.viewModel = mViewModel

        // Seta o adapter do RecyclerView da lista de recibos.
        binding.receiptsRecyclerview.setHasFixedSize(true)

        // Recebe os dados do usuário como argumento (juntamente com o token) da tela de login.
        arguments.userData.let {
            mUserData = it
            mToken = "Bearer ${it.token}"

            Log.i("JAO", "arguments userData: $it")
        }

        // Recarrega os recibos na RecyclerView (buscando do servidor) quando
        // fazemos o swipe.
        binding.srReceipts.setOnRefreshListener {
            loadReceipts()
            binding.srReceipts.isRefreshing = false
        }

        // Verifica a lista de recibos do ViewModel para carregar a lista na inicialização
        // apenas uma vez (quando o ViewModel é criado).
        if (mViewModel.receiptList.value == null) {
            loadReceipts()
        }

        // Inicializa as variáveis e os observers.
        initReceiptsList()

        return binding.root
    }


    private fun initReceiptsList() {
        // Observa a lista filtrada (armazenada no ViewModel), instancia o Adapter e
        // inicializa a RecyclerView. TODO estudar mais a fundo a variável filteredListReceipt
        mViewModel.filteredListReceipt.observe(viewLifecycleOwner) {

            // Configura os listeners (métodos callback) do objeto ReceiptsListener,
            // passando os respectivos parâmetros.
            val receiptsAdapter = ReceiptsAdapter(ReceiptsListener(
                clickListener = { receipt ->
                    navigateToReceiptsDetailsFragment(receipt)
                },
                longClickListener = { receipt ->
                    mViewModel.deleteReceipt(mToken, receipt)
                    true
                }
            ))

            // Atribui o layout e o adapter para a Recyclerview.
            binding.receiptsRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = receiptsAdapter
            }

            // Submete a lista para a Recyclerview com os itens e seus respectivos headers.
            receiptsAdapter.addHeadersAndSubmitList(it)
        }

        // Observa a variável "deleteResult" para recarregar a lista de recibos e exibir o
        // toast de êxito somente quando a chamada retornar sucesso.
        mViewModel.deleteResult.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                loadReceipts()

                Toast.makeText(
                    context,
                    "Receipt deleted successfully!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Como, por enquanto, essa é a tela principal do projeto, o botão de back tem a
        // função de fechar a aplicação.
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ActivityCompat.finishAffinity(requireActivity())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
    }

    /**
     * Infla o menu de opções e prepara a SearchView para a realização das buscas
     * na RecyclerView.
     */
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val myActionMenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = myActionMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                // Dispara o filtro dos recibos que é realizado no ViewModel.
                mViewModel.getFilter().filter(newText)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
        })
    }

    /**
     * (Gambi) Método chamado quando selecionamos uma opção no menu de opções.
     */
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Cria os recibos (mocados hard coded) para o usuário logado.
            R.id.load_receipts -> {
                createReceipts(mUserData.idUser!!).forEach {
                    mViewModel.createReceipt(mToken, it)
                    Log.i("JAO", "Token: $mToken / Receipt: $it")
                }
                loadReceipts()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Navega para a tela de detalhes do recibo (chamado quando clicamos em algum
    // item na RecyclerView).
    private fun navigateToReceiptsDetailsFragment(receipts: Receipt) {
        val direction = ReceiptsListFragmentDirections.navigateToReceiptsDetails(receipts)
        findNavController().navigate(direction)
    }

    // Busca os recibos do usuário logado (através do token) no servidor.
    private fun loadReceipts() {
        mViewModel.getReceipts(mToken)
    }

    /**
     * My name is Arra... Gambi Arra
     * Cria e retorna uma lista de "NewReceipt" (body do endpoint para criar recibos).
     */
    private fun createReceipts(idUser: String): List<NewReceipt> {
        // Calcula as datas através do timestamp para adicionar uma data por recibo.
        // Isso foi feito somente para exibir os diferentes tipos de header.
        val currentTimestamp =
            LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().epochSecond.toInt()

        return listOf(
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp,
                value = 274.89f,
                status = 0,
                paymentMethod = 1,
                cardInfoBrand = "Visa",
                merchantName = "Farmacia do Messias",
                message = "farmacia"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp - (86400 * 1),
                value = 78.90f,
                status = 0,
                paymentMethod = 2,
                cardInfoBrand = "Master",
                merchantName = "Venda do Zé Pinga",
                message = "venda_do_ze"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp - (86400 * 2),
                value = 18.90f,
                status = 0,
                paymentMethod = 6,
                cardInfoBrand = "",
                merchantName = "Padaria do TK",
                message = "padaria_tk"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp - (86400 * 3),
                value = 5690.90f,
                status = 0,
                paymentMethod = 4,
                cardInfoBrand = "",
                merchantName = "Lojas Samsung",
                message = "samsung"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp - (86400 * 4),
                value = 119.90f,
                status = 0,
                paymentMethod = 5,
                cardInfoBrand = "",
                merchantName = "Cafeteria do Marcos",
                message = "cafeteria"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp - (86400 * 5),
                value = 18.90f,
                status = 0,
                paymentMethod = 3,
                cardInfoBrand = "",
                merchantName = "Locadora do Adnan",
                message = "locadora"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp - (86400 * 6),
                value = 18.90f,
                status = 0,
                paymentMethod = 3,
                cardInfoBrand = "",
                merchantName = "Locadora do Adnan",
                message = "locadora"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = currentTimestamp - (86400 * 7),
                value = 18.90f,
                status = 0,
                paymentMethod = 3,
                cardInfoBrand = "",
                merchantName = "Locadora do Adnan",
                message = "locadora"
            )
        )
    }
}