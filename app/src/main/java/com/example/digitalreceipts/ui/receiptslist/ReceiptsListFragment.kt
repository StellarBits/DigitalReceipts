package com.example.digitalreceipts.ui.receiptslist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.digitalreceipts.databinding.ReceiptsListFragmentBinding
import com.example.digitalreceipts.ui.adapter.ReceiptsAdapter
import com.example.digitalreceipts.ui.adapter.ReceiptsListener
import com.example.digitalreceipts.usecase.ApplySearchFilterUseCase
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ReceiptsListFragment : Fragment() {

    private val viewModel: ReceiptsListViewModel by viewModel {
        parametersOf(ApplySearchFilterUseCase())
    }
    private lateinit var mView: View

    private val binding: ReceiptsListFragmentBinding by lazy {
        ReceiptsListFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView
        binding.receiptsRecyclerview.setHasFixedSize(true)
        //binding.receiptsRecyclerview.adapter = ReceiptsListAdapter(this)

        binding.btSearch.setOnClickListener {
            Log.i("JAO", "Button Click!")

            if (!binding.svSearchReceipts.isVisible) {
                binding.tvWelcome.visibility = View.GONE
                binding.svSearchReceipts.visibility = View.VISIBLE
                binding.svSearchReceipts.isFocusable = true
                binding.svSearchReceipts.isIconified = false
            }
        }

        binding.svSearchReceipts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getFilter().filter(newText)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
        })

        binding.srReceipts.setOnRefreshListener {
            binding.svSearchReceipts.setQuery("", false)
            viewModel.loadData()
            binding.srReceipts.isRefreshing = false
        }

        /*viewModel.fields.observe(viewLifecycleOwner, Observer {
            Log.i("JAO", "Observer")
        })*/

        initReceiptsList()

        return binding.root
    }

    /**
     * Observa a lista armazenada no ViewModel, instancia o Adapter e inicializa
     * a RecyclerView com um layout linear.
     * Configura os comportamentos (métodos callback) do objeto BusinessCardListener,
     * passando os respectivos parâmetros.
     */
    private fun initReceiptsList() {
        viewModel.filteredListBusinessCard.observe(viewLifecycleOwner) {
            val adapter = ReceiptsAdapter(ReceiptsListener(clickListener = { receipts ->
                navigateToReceiptsDetailsFragment()
            }
            ))

            binding.receiptsRecyclerview.layoutManager = LinearLayoutManager(context)
            binding.receiptsRecyclerview.adapter = adapter
            adapter.addHeadersAndSubmitList(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.svSearchReceipts.isVisible) {
                    binding.svSearchReceipts.setQuery("", false)
                    binding.tvWelcome.visibility = View.VISIBLE
                    binding.svSearchReceipts.visibility = View.GONE
                } else {
                    ActivityCompat.finishAffinity(requireActivity())
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
    }

//    override fun onReceiptClick(position: Int) {
//        val navController = Navigation.findNavController(mView)
//        navController.navigate(R.id.navigate_to_receipts_details)
//
//        Log.i("JAO", "onReceiptClick: $position")
//    }

    private fun navigateToReceiptsDetailsFragment() {
        val direction = ReceiptsListFragmentDirections.navigateToReceiptsDetails()
        findNavController().navigate(direction)
    }
}