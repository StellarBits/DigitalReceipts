package com.example.digitalreceipts

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalreceipts.adapter.ReceiptsListAdapter
import com.example.digitalreceipts.model.Fields
import com.example.digitalreceipts.model.Receipts

/**
 * Updates the data shown in the [RecyclerView].
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Fields>?) {
    val adapter = recyclerView.adapter as ReceiptsListAdapter
    adapter.submitList(data)
}