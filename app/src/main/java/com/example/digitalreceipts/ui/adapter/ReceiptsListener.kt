package com.example.digitalreceipts.ui.adapter

import com.example.digitalreceipts.api.model.Receipt

class ReceiptsListener(
    val clickListener: (receipts: Receipt) -> Unit,
    //val shareBtnClickListener: (view: View) -> Unit,
    //val longClickListener: (receipts: Receipt) -> Boolean
) {

    fun onClick(receipt: Receipt) {
        clickListener(receipt)
    }

    /*fun onLongClick(receipt: Receipt) : Boolean {
        longClickListener(receipt)
        return true
    }

    fun onShareBtnClick(view: View) {
        shareBtnClickListener(view)
    }*/
}