package com.example.digitalreceipts.ui.receiptslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalreceipts.api.ReceiptApi
import com.example.digitalreceipts.api.model.Fields
import com.example.digitalreceipts.api.model.LoginBody
import com.example.digitalreceipts.api.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReceiptsListViewModel : ViewModel() {
    // The internal MutableLiveData that stores the status of the most recent request
    //private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the request status
    //val status: LiveData<String> = _status

    private val _fields = MutableLiveData<List<Fields>>()

    var fields: LiveData<List<Fields>> = _fields

    /**
     * Call login() on init so we can display status immediately.
     */
    init {
        val loginBody = LoginBody("m.aleixo@sidi.org.br", "Aleixo123!")
        login(loginBody)
    }

    private fun login(loginBody: LoginBody) {
        viewModelScope.launch {
            try {
                ReceiptApi.retrofitService.login(loginBody).enqueue(object: Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.body() != null) {
                            getReceipts("Bearer ${response.body()!!.token}")

                            Log.i("JAO", "Success: ${response.body()!!.token}")
                        } else {
                            Log.i("JAO", "Failure: response is null!")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e("JAO", "Failure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("JAO", "Failure: ${e.message}")
                //_status.value = "Failure: ${e.message}"
            }
        }
    }

    /**
     * Gets Receipts information from the Digital Receipts API Retrofit service and updates...
     */
    private fun getReceipts(token: String) {
        viewModelScope.launch {
            try {
                val listResult = ReceiptApi.retrofitService.getReceipts(token)
                _fields.value = ReceiptApi.retrofitService.getReceipts(token).receipts

                Log.i("JAO", "Success: ${listResult.receipts}")
                //Log.i("JAO", "Success: ${listResult.receipts.map { it.merchantName }}")
                //_status.value = "Success: ${listResult.receipts.map { it.merchantName }}"
            } catch (e: Exception) {
                Log.e("JAO", "Failure: ${e.message}")
                //_status.value = "Failure: ${e.message}"
            }
        }
    }
}