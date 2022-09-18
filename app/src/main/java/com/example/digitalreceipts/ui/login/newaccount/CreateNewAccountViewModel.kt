package com.example.digitalreceipts.ui.login.newaccount

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalreceipts.api.ReceiptApi
import com.example.digitalreceipts.api.model.GenericResponse
import com.example.digitalreceipts.api.model.NewUser
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateNewAccountViewModel : ViewModel() {

    var apiResponse = MutableLiveData<GenericResponse>()

    fun createNewUser(newUser: NewUser) {
        viewModelScope.launch {
            try {
                ReceiptApi.retrofitService.createNewUser(newUser).enqueue(object :
                    Callback<GenericResponse> {
                    override fun onResponse(
                        call: Call<GenericResponse>,
                        response: Response<GenericResponse>
                    ) {
                        var responseCode = 404
                        var responseMessage = "Failure: No response from server!"

                        if (response.body() != null) {
                            responseCode = response.body()!!.code!!
                            responseMessage = response.body()!!.resultMessage!!

                            Log.i(
                                "JAO",
                                "createNewUser response: ${response.body()!!.resultMessage}"
                            )
                        } else {
                            Log.i("JAO", "createNewUser - Failure: response is null!")
                        }

                        apiResponse.postValue(GenericResponse(responseCode, responseMessage))
                    }

                    override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                        Log.e("JAO", "Failure: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("JAO", "Failure: ${e.message}")
                //_status.value = "Failure: ${e.message}"
            }
        }
    }
}