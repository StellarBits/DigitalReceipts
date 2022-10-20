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

class ReceiptsListFragment : Fragment() {

    private lateinit var mToken: String
    private lateinit var mUserData: LoginResponse

    private val mViewModel: ReceiptsListViewModel by viewModel {
        parametersOf(ApplySearchFilterUseCase())
    }
    private lateinit var mView: View

    private val binding: ReceiptsListFragmentBinding by lazy {
        ReceiptsListFragmentBinding.inflate(layoutInflater)
    }

    private val arguments by navArgs<ReceiptsListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = mViewModel

        // Sets the adapter of the photosGrid RecyclerView
        binding.receiptsRecyclerview.setHasFixedSize(true)

        arguments.userData.let {
            mUserData = it
            mToken = "Bearer ${it.token}"

            Log.i("JAO", "arguments userData: $it")
        }

        mViewModel.receiptList.observe(viewLifecycleOwner) { list ->
            Log.i("JAO", "observe receipt: $list")
        }

        binding.srReceipts.setOnRefreshListener {
            loadReceipts()
            binding.srReceipts.isRefreshing = false
        }

        loadReceipts()
        initReceiptsList()

        return binding.root
    }

    /**
     * Observa a lista armazenada no ViewModel, instancia o Adapter e inicializa
     * a RecyclerView com um layout linear.
     * Configura os comportamentos (métodos callback) do objeto ReceiptsListener,
     * passando os respectivos parâmetros.
     */
    private fun initReceiptsList() {
        mViewModel.filteredListReceipt.observe(viewLifecycleOwner) {
            Log.i("JAO", "onSwiped - observe")

            val receiptsAdapter = ReceiptsAdapter(ReceiptsListener(
                clickListener = { receipt ->
                    navigateToReceiptsDetailsFragment(receipt)
                },
                longClickListener = { receipt ->
                    mViewModel.deleteReceipt(mToken, receipt)
                    true
                }
            ))

            binding.receiptsRecyclerview.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = receiptsAdapter
            }

            receiptsAdapter.addHeadersAndSubmitList(it)
        }

        mViewModel.deleteResult.observe(viewLifecycleOwner) {
            loadReceipts()

            Toast.makeText(
                context,
                "Recibo excluído com sucesso!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val myActionMenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = myActionMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                mViewModel.getFilter().filter(newText)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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

    private fun navigateToReceiptsDetailsFragment(receipts: Receipt) {
        val direction = ReceiptsListFragmentDirections.navigateToReceiptsDetails(receipts)
        findNavController().navigate(direction)
    }

    private fun loadReceipts() {
        mViewModel.getReceipts(mToken)
    }

    private fun createReceipts(idUser: String): List<NewReceipt> {
        return listOf(
            NewReceipt(
                idUserToSend = idUser,
                date = 1642458486,
                value = 274.89f,
                status = 0,
                paymentMethod = 1,
                cardInfoBrand = "Visa",
                merchantName = "Farmacia do Messias",
                message = "farmacia"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = 1641662706,
                value = 78.90f,
                status = 0,
                paymentMethod = 1,
                cardInfoBrand = "Visa",
                merchantName = "Venda do Zé Pinga",
                message = "venda_do_ze"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = 1636900468,
                value = 18.90f,
                status = 0,
                paymentMethod = 2,
                cardInfoBrand = "Master",
                merchantName = "Padaria do TK",
                message = "padaria_tk"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = 1634553628,
                value = 5690.90f,
                status = 0,
                paymentMethod = 1,
                cardInfoBrand = "Master",
                merchantName = "Lojas Samsung",
                message = "samsung"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = 1634575768,
                value = 119.90f,
                status = 0,
                paymentMethod = 1,
                cardInfoBrand = "Visa",
                merchantName = "Cafeteria do Marcos",
                message = "cafeteria"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = 3390,
                value = 18.90f,
                status = 0,
                paymentMethod = 3,
                cardInfoBrand = "",
                merchantName = "Locadora do Adnan",
                message = "locadora"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = 3390,
                value = 18.90f,
                status = 0,
                paymentMethod = 3,
                cardInfoBrand = "",
                merchantName = "Locadora do Adnan",
                message = "locadora"
            ),
            NewReceipt(
                idUserToSend = idUser,
                date = 3390,
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