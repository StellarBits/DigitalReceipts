package com.example.digitalreceipts.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    var token: String?,
    var idUser: String?,
    var name: String?,
    var email: String?,
    var cpf: String?,
    var avatar: String?,
) : Parcelable