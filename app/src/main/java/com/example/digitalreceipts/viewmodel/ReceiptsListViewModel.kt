package com.example.digitalreceipts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalreceipts.R
import com.example.digitalreceipts.ReceiptApi
import com.example.digitalreceipts.adapter.ReceiptsListAdapter
import com.example.digitalreceipts.fragment.ReceiptsListFragment
import com.example.digitalreceipts.model.Fields
import com.example.digitalreceipts.model.Receipts
import kotlinx.coroutines.launch

class ReceiptsListViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    // The internal MutableLiveData that stores the status of the most recent request
    //private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the request status
    //val status: LiveData<String> = _status

    private val _fields = MutableLiveData<List<Fields>>()

    val fields: LiveData<List<Fields>> = _fields

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getReceipts()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [LiveData].
     */
    private fun getReceipts() {
        viewModelScope.launch {
            try {
                Log.i("JAO", "ViewModel#getReceipts")
                //val listResult = ReceiptApi.retrofitService.getReceipts()
                _fields.value = ReceiptApi.retrofitService.getReceipts().receipts

                //_status.value = "Success: ${listResult.receipts.map { it.merchantName }}"
            } catch (e: Exception) {
                //_status.value = "Failure: ${e.message}"
            }
        }
    }
}