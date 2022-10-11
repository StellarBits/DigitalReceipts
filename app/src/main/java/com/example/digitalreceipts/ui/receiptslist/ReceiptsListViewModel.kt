package com.example.digitalreceipts.ui.receiptslist

import android.util.Log
import android.widget.Filter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalreceipts.api.ReceiptApi
import com.example.digitalreceipts.api.model.Fields
import com.example.digitalreceipts.api.model.GetUserReceipts
import com.example.digitalreceipts.api.model.NewReceipt
import com.example.digitalreceipts.api.model.NewReceiptResponse
import com.example.digitalreceipts.extension.sortedByDate
import com.example.digitalreceipts.usecase.ApplySearchFilterUseCase
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ReceiptsListViewModel(
    applySearchFilterUseCase: ApplySearchFilterUseCase,
) : ViewModel() {

    private lateinit var mFilterableText: String
    private lateinit var mValueFilter: ValueFilter
    private lateinit var mFilteredList: ArrayList<Fields>

    private var mOriginalFieldsList: List<Fields> = listOf()

    private val _fields = MutableLiveData<List<Fields>>()

    var fields: LiveData<List<Fields>> = _fields

    /**
     * Esse campo representa a query de busca. Como esse campo é manipulado
     * por um método setter ele pode ser privado.
     */

    private val _searchQuery = MutableLiveData<CharSequence>("")
    private val searchQuery: LiveData<CharSequence>
        get() = _searchQuery

    /**
     * A lista filtrada é exposta para o Fragment. Como o filtro é aplicado
     * dinamicamente e gera uma nova lista, os dados do repositório não
     * são modificados. Ao final é invocada uma função de extensão para
     * ordenar a lista em ordem alfabética.
     */
    val filteredListBusinessCard: LiveData<List<Fields>> =
        applySearchFilterUseCase.filterList(fields, searchQuery).sortedByDate()

    /**
     * Gets GetUserReceipts information from the Digital GetUserReceipts API Retrofit service and updates...
     */
    fun getReceipts(token: String) {
        viewModelScope.launch {
            try {
                ReceiptApi.retrofitService.getReceipts(token).enqueue(object :
                    Callback<GetUserReceipts> {
                    override fun onResponse(
                        call: Call<GetUserReceipts>,
                        response: Response<GetUserReceipts>
                    ) {
                        if (response.body() != null) {
                            _fields.value = response.body()?.receipts
                            mOriginalFieldsList = response.body()?.receipts!!

                            Log.i("JAO", "Success: ${_fields.value}")
                        } else {
                            Log.i("JAO", "createNewUser - Failure: response is null!")
                        }
                    }

                    override fun onFailure(call: Call<GetUserReceipts>, t: Throwable) {
                        Log.e("JAO", "onFailure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("JAO", "Failure: ${e.message}")
            }
        }
    }

    fun createReceipt(token: String, newReceipt: NewReceipt) {
        viewModelScope.launch {
            try {
                ReceiptApi.retrofitService.createNewReceipt(token, newReceipt).enqueue(object :
                    Callback<NewReceiptResponse> {
                    override fun onResponse(
                        call: Call<NewReceiptResponse>,
                        response: Response<NewReceiptResponse>
                    ) {
                        Log.i("JAO", "onResponse: $response")

                        if (response.isSuccessful) {
                            Log.i("JAO", "createReceipt: ${response.body()}")
                        }
                    }

                    override fun onFailure(call: Call<NewReceiptResponse>, t: Throwable) {
                        Log.i("JAO", "onFailure: $t")
                    }
                })
            } catch (e: Exception) {
                Log.e("JAO", "Failure: ${e.message}")
            }
        }
    }

    fun getFilter(): Filter {
        mValueFilter = ValueFilter()

        return mValueFilter
    }

    private inner class ValueFilter: Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val results = FilterResults()
            val localFieldsList = ArrayList<Fields>()

            mFilterableText = ""

            if (p0 != null) {
                for (fields in mOriginalFieldsList) {
                    val stringToFilter = p0.toString().lowercase(Locale.US)
                    mFilterableText = stringToFilter

                    if (fields.merchantName.lowercase(Locale.US).contains(stringToFilter)) {
                        localFieldsList.add(fields)
                    }
                }

                results.values = localFieldsList
                results.count = localFieldsList.size
            }

            return results
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            Log.i("JAO", "publishResults")

            @Suppress("UNCHECKED_CAST")
            mFilteredList = p1?.values as ArrayList<Fields>
            _fields.value = mFilteredList
        }
    }
}