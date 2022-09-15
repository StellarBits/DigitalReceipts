package com.example.digitalreceipts.ui.login.newaccount

import android.util.Log
import androidx.lifecycle.LiveData
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

    var test = MutableLiveData<Int>()

    fun createNewUser(newUser: NewUser) {
        viewModelScope.launch {
            try {
                ReceiptApi.retrofitService.createNewUser(newUser).enqueue(object :
                    Callback<GenericResponse> {
                    override fun onResponse(
                        call: Call<GenericResponse>,
                        response: Response<GenericResponse>
                    ) {
                        var resultCode = 401

                        if (response.body() != null) {
                            resultCode = response.body()!!.code!!

                            if (response.body()!!.code == 201) {
                                Log.i("JAO", "createNewUser - Success: ${response.body()!!.resultMessage}")
                            }
                        } else {
                            Log.i("JAO", "createNewUser - Failure: response is null!")
                        }

                        test.postValue(resultCode)
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