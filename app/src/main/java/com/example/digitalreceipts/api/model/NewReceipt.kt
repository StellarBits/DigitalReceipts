package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewReceipt(
    var value: Float,
    var status: Int,
    var paymentMethod: Int,
    var cardInfoBrand: String,
    var merchantName: String,
    var message: String,
    var date: Int,
    var idUserToSend: String
): Parcelable
