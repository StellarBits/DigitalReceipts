package com.example.digitalreceipts.ui.main.login.forgotpassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalreceipts.api.ReceiptApi
import com.example.digitalreceipts.api.model.GenericResponse
import com.example.digitalreceipts.api.model.ResetPassword
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel : ViewModel() {

    var apiResponse = MutableLiveData<GenericResponse>()

    fun sendResetEmail(resetPassword: ResetPassword) {
        viewModelScope.launch {
            try {
                ReceiptApi.retrofitService.resetPassword(resetPassword).enqueue(object :
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
                                "sendResetEmail response: ${response.body()!!.resultMessage}"
                            )
                        } else {
                            Log.i("JAO", "sendResetEmail - Failure: response is null!")
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