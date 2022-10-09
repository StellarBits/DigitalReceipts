package com.example.digitalreceipts.util

//import com.example.digitalreceipts.ui.receiptslist.ReceiptsListAdapter
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.digitalreceipts.api.model.CardInfo
import com.example.digitalreceipts.ui.adapter.DataItem
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.util.*

/**
 * Updates the data shown in the [RecyclerView].
 */
//@BindingAdapter("listData")
//fun bindRecyclerView(recyclerView: RecyclerView, data: List<Fields>?) {
//    val adapter = recyclerView.adapter as ReceiptsListAdapter
//    adapter.submitList(data)
//}

@BindingAdapter("bind:icon")
fun loadIcon(imageView: ImageView, imageName: String?) {
    val IMAGES_DRAWABLE_PATH = "android.resource://com.example.digitalreceipts/drawable/"

    if (imageName != null) {
        val logo = imageName + "_logo"

        // Load image using Coil (Recommended from Google)
        imageView.load(
            Uri.parse(
                IMAGES_DRAWABLE_PATH + logo
                    .removeSuffix(".png")
                    .removeSuffix(".jpg")
            )
        )
    }
}

@BindingAdapter("bind:cover")
fun loadCover(imageView: ImageView, imageName: String?) {
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
fun setValue(textView: TextView, receiptValue: Float?) {
    if (receiptValue != null) {
        val value = BigDecimal(receiptValue.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
        textView.text = String.format(value.toString())
    }
}

@BindingAdapter("bind:time")
fun setTime(textView: TextView, receiptTime: Long?) {
    if (receiptTime != null) {
        /*val localDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochSecond(receiptTime), ZoneId.systemDefault())*/

        val simpleDate =
            if (false)
                SimpleDateFormat("dd/MM/yyyy hh:mm a")
            else
                SimpleDateFormat("hh:mm a")

        val currentDate = simpleDate.format(Date.from(Instant.ofEpochSecond(receiptTime)))
        textView.text = currentDate
    }
}

@BindingAdapter("bind:headerDate")
fun setKey(textView: TextView, item: DataItem.Header){
    item.let {
        textView.text = item.date
    }
}

@BindingAdapter("bind:card")
fun setCreditCard(textView: TextView, item: CardInfo?){
    val result = if (item?.brand != null) "${item.brand} ${item.last4digits}" else "Money"

    textView.text = result
}