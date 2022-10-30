package com.example.digitalreceipts.api

import com.example.digitalreceipts.api.model.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://sidi-proj-capacitacao.herokuapp.com/api/"

/**
 * Endpoints.
 */
interface RetrofitService {
    @GET("v2/receipt")
    fun getReceipts(@Header("Authorization") token: String): Call<GetUserReceipts>

    @POST("v2/receipt")
    fun createNewReceipt(
        @Header("Authorization") token: String,
        @Body newReceipt: NewReceipt
    ): Call<NewReceiptResponse>

    @POST("v2/users/login")
    fun requestLogin(@Body loginBody: LoginBody): Call<LoginResponse>

    @POST("v2/users")
    fun createNewUser(@Body newUser: NewUser): Call<GenericResponse>

    @POST("v2/users/resetPassword")
    fun resetPassword(@Body resetPassword: ResetPassword): Call<GenericResponse>

    @DELETE("v2/receipt/{receiptId}")
    fun deleteReceipt(
        @Header("Authorization") token: String,
        @Path("receiptId") receiptNumber: String
    ): Call<GenericResponse>
}

/**
 * Singleton do retrofit.
 */
object ReceiptApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: RetrofitService = retrofit.create(RetrofitService::class.java)
}