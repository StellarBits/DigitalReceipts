package com.example.digitalreceipts.api

import com.example.digitalreceipts.api.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

private const val BASE_URL = "https://receipts-sidi-server.onrender.com/api/"

interface ApiInterface {
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
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ReceiptApi {
    /**
     * Build the Moshi object with Kotlin adapter factory that Retrofit will be using.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .build()
        chain.proceed(newRequest)
    }.build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    val retrofitService: ApiInterface by lazy { retrofit.create(ApiInterface::class.java) }
}