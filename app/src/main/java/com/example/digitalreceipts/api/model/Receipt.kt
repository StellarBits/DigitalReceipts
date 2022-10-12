package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Receipt(
    var id: String,
    var idUser: String,
    var value: Float,
    var status: Int,
    var paymentMethod: Int,
    var cardInfoBrand: String,
    var merchantName: String,
    var date: Long,
    var authentication: String,
    var merchantIcon: String,
    var merchantImage: String,
    var message: String
) : Parcelable