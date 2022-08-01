package com.example.digitalreceipts.ui

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.digitalreceipts.ui.receiptslist.ReceiptsListAdapter
import com.example.digitalreceipts.api.model.Fields
import java.util.*

/**
 * Updates the data shown in the [RecyclerView].
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Fields>?) {
    val adapter = recyclerView.adapter as ReceiptsListAdapter
    adapter.submitList(data)
}

@BindingAdapter("bind:src")
fun loadImage(imageView: ImageView, imageName: String?) {
    val IMAGES_DRAWABLE_PATH = "android.resource://com.example.digitalreceipts/drawable/"

    if (imageName != null) {
        // Load image using Coil (Recommended from Google)
        imageView.load(
            Uri.parse(
                IMAGES_DRAWABLE_PATH + imageName
                    .removeSuffix(".png")
                    .removeSuffix(".jpg")
            )
        )
    }
}

@BindingAdapter("bind:value")
fun setValue(textView: TextView, receiptValue: Int?) {
    if (receiptValue != null) {
        var stringValue = receiptValue.toString()
        stringValue = StringBuffer(stringValue).insert(stringValue.length - 2, ".").toString()

        val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 0
        numberFormat.currency = Currency.getInstance(Locale.getDefault())

        val formattedValue =
            StringBuffer(numberFormat.format(stringValue.toFloat())).insert(2, " ").toString()

        textView.text = formattedValue
    }
}