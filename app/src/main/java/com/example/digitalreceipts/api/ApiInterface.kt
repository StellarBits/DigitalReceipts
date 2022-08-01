package com.example.digitalreceipts.api

import com.example.digitalreceipts.api.model.LoginBody
import com.example.digitalreceipts.api.model.LoginResponse
import com.example.digitalreceipts.api.model.Receipts
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://shielded-crag-65206.herokuapp.com/api/"

interface ApiInterface {
    @GET("v1/receipts")
    suspend fun getReceipts(@Header("Authorization") token: String): Receipts

    @POST("v1/users/login")
    fun login(@Body loginBody: LoginBody): Call<LoginResponse>
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