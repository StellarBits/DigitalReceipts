package com.example.digitalreceipts.ui.receiptslist

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import android.widget.SearchView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.digitalreceipts.R
import com.example.digitalreceipts.databinding.ReceiptsListFragmentBinding
import org.w3c.dom.Text

class ReceiptsListFragment : Fragment(), ReceiptsListAdapter.OnReceiptListener {

    private val viewModel: ReceiptsListViewModel by viewModels()
    private lateinit var mView: View
    private lateinit var mSearchView: SearchView
    private lateinit var mWelcomeTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ReceiptsListFragmentBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView
        binding.receiptsRecyclerview.setHasFixedSize(true)
        binding.receiptsRecyclerview.adapter = ReceiptsListAdapter(this)

        mWelcomeTextView = binding.tvWelcome
        mSearchView = binding.svSearchReceipts

        binding.btSearch.setOnClickListener {
            Log.i("JAO", "Button Click!")

            if (!binding.svSearchReceipts.isVisible) {
                mWelcomeTextView.visibility = View.GONE
                mSearchView.visibility = View.VISIBLE
            }
        }

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getFilter().filter(newText)
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }
        })

        /*viewModel.fields.observe(viewLifecycleOwner, Observer {
            Log.i("JAO", "Observer")
        })*/

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mSearchView.isVisible) {
                    mSearchView.setQuery("", false)
                    mWelcomeTextView.visibility = View.VISIBLE
                    mSearchView.visibility = View.GONE
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

    override fun onReceiptClick(position: Int) {
        val navController = Navigation.findNavController(mView)
        navController.navigate(R.id.action_receiptsListFragment_to_receiptsDetailsFragment)

        Log.i("JAO", "onReceiptClick: $position")
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.dismissKeyboardShortcutsHelper() }
    }
}