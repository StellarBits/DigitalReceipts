package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardInfo(
    var brand: String?,
    var last4digits: Int?
) : Parcelable