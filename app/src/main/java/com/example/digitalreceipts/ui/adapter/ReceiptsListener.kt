package com.example.digitalreceipts.ui.adapter

import com.example.digitalreceipts.api.model.Fields

class ReceiptsListener(
    val clickListener: (receipts: Fields) -> Unit,
    //val shareBtnClickListener: (view: View) -> Unit,
    //val longClickListener: (receipts: Fields) -> Boolean
) {

    fun onClick(businessCard: Fields) {
        clickListener(businessCard)
    }

    /*fun onLongClick(businessCard: Fields) : Boolean {
        longClickListener(businessCard)
        return true
    }

    fun onShareBtnClick(view: View) {
        shareBtnClickListener(view)
    }*/
}