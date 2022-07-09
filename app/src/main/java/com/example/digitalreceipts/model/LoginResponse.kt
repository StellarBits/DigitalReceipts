package com.example.digitalreceipts.model

data class LoginResponse(
    var id: Int,
    var username: String,
    var first_name: String,
    var last_name: String,
    var email: String,
    var role: String,
    var age: Int,
    var token: String
)