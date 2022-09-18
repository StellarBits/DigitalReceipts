package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResetPassword(
    var email: String?,
    var cpf: String?,
    var phonenumber: String?
) : Parcelable
