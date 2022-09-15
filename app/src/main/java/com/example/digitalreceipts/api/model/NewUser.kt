package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewUser(
    var name: String?,
    var email: String?,
    var password: String?,
    var cpf: String?,
    var phonenumber: String?,
    var acceptTerms: Boolean?
) : Parcelable