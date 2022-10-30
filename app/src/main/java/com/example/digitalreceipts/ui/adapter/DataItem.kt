package com.example.digitalreceipts.ui.adapter

import com.example.digitalreceipts.api.model.Receipt

sealed class DataItem {
    abstract val id: String

    /**
     * Data Class que será usada como referência para os dados do recibo na RecyclerView.
     */
    data class ReceiptsItem(val receipts: Receipt,
                            override val id: String = receipts.id) : DataItem()

    /**
     * Data Class que será usada como referência para o header (data do recibo).
     */
    data class Header(val date: String,
                      override val id: String = ""): DataItem()
}