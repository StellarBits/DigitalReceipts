package com.example.digitalreceipts

import com.example.digitalreceipts.model.Fields
import com.example.digitalreceipts.model.Receipts
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://shielded-crag-65206.herokuapp.com/api/"

/**
 * Build the Moshi object with Kotlin adapter factory that Retrofit will be using.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

var client: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
    val newRequest: Request = chain.request().newBuilder()
        .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoiMTIzIiwiaWF0IjoxNjU2ODc2OTM5LCJleHAiOjE2NTY5NjMzMzl9.udTbVTOKfyo4Y9gQlcoBquElX1b9W8mOFnPSiThJhgw")
        .build()
    chain.proceed(newRequest)
}.build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiInterface {
    @GET("v1/receipts")
    suspend fun getReceipts(): Receipts
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object ReceiptApi {
    val retrofitService: ApiInterface by lazy { retrofit.create(ApiInterface::class.java) }
}