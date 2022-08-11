package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fields(
    var id: String,
    var date: Long,
    var value: Int,
    var status: Int,
    var authentication: String,
    var paymentMethod: Int,
    var cardInfo: CardInfo,
    var merchantName: String,
    var merchantId: String,
    var merchantIcon: String,
    var merchantImage: String,
    var description: String
) : Parcelable