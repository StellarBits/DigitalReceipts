package com.example.digitalreceipts.ui.adapter

import com.example.digitalreceipts.api.model.Receipt

/**
 * Essa classe é responsável por escutar as interações com os items da recyclerView.
 * TODO entender melhor como atuam os contrutores dessa classe
 */
class ReceiptsListener(
    val clickListener: (receipts: Receipt) -> Unit, // TODO entender melhor o retorno Unit
    val longClickListener: (receipts: Receipt) -> Boolean
) {
    fun onClick(receipt: Receipt) {
        clickListener(receipt)
    }

    fun onLongClick(receipt: Receipt) : Boolean {
        longClickListener(receipt)
        return true
    }
}