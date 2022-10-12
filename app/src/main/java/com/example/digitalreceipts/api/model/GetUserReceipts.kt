package com.example.digitalreceipts.api.model

data class GetUserReceipts(
    val code: Int,
    val resultMessage: String,
    val receipts: List<Receipt>
)