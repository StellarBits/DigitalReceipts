package com.example.digitalreceipts.api.model

data class NewReceiptResponse(
    val code: Int,
    val resultMessage: String,
    val receipt: Receipt
)
