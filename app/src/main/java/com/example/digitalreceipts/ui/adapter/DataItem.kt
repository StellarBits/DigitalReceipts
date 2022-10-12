package com.example.digitalreceipts.ui.adapter

import com.example.digitalreceipts.api.model.Receipt

sealed class DataItem {
    abstract val id: String

    data class ReceiptsItem(val receipts: Receipt,
                            override val id: String = receipts.id) : DataItem()

    data class Header(val date: String,
                      override val id: String = ""): DataItem()
}