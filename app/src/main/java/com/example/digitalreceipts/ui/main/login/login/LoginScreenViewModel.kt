package com.example.digitalreceipts.ui.main.login.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digitalreceipts.api.ReceiptApi
import com.example.digitalreceipts.api.model.LoginBody
import com.example.digitalreceipts.api.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginScreenViewModel : ViewModel() {

    var apiResponse = MutableLiveData<Response<LoginResponse>>()

    fun login(loginBody: LoginBody) {
        viewModelScope.launch {
            try {
                ReceiptApi.retrofitService.requestLogin(loginBody).enqueue(object :
                    Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.body() != null) {
                            Log.i("JAO", "Success: ${response.body()!!.token}")
                        } else {
                            Log.i("JAO", "Failure: response is null!")
                        }

                        apiResponse.postValue(response)
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
}