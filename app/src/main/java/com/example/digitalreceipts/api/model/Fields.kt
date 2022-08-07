package com.example.digitalreceipts.api.model

data class Fields(
    var id: String,
    var date: Long,
    var value: Int,
    var status: Int,
    var authentication: String,
    var paymentMethod: Int,
    //var cardInfo: List<CardInfo>,
    var merchantName: String,
    var merchantId: String,
    var merchantIcon: String,
    var merchantImage: String,
    var description: String
)