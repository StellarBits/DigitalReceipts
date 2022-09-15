package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenericResponse(
    var code: Int?,
    var resultMessage: String?
) : Parcelable
