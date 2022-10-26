package com.example.digitalreceipts.util

import android.annotation.SuppressLint
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.digitalreceipts.api.model.Receipt
import com.example.digitalreceipts.ui.adapter.DataItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

private const val IMAGES_DRAWABLE_PATH = "android.resource://com.example.digitalreceipts/drawable/"
private const val TAG = "BindingAdapters"

@BindingAdapter("bind:icon")
fun loadIcon(imageView: ImageView, imageName: String?) {
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

@SuppressLint("SetTextI18n")
@BindingAdapter("bind:value")
fun setValue(textView: TextView, receiptValue: Float?) {
    if (receiptValue != null) {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        val df = DecimalFormat("#,###,##0.00", symbols)

        //val value = BigDecimal(receiptValue.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
        val value = receiptValue.toDouble()

        textView.text =
            if (Locale.getDefault().equals(Locale("pt", "BR")))
                "R$: ${df.format(value)}"
            else
                "US$: ${df.format(value)}"
    }
}

@BindingAdapter("bind:time")
fun setTime(textView: TextView, receiptTime: Long?) {
    if (receiptTime != null) {
        val simpleDate = SimpleDateFormat("dd/MM/yyyy - hh:mm a", Locale.getDefault())
        val currentDate = simpleDate.format(Date.from(Instant.ofEpochSecond(receiptTime)))
        textView.text = currentDate
    }
}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("bind:headerDate")
fun setKey(textView: TextView, item: DataItem.Header) {
    item.let {
        Log.i(TAG, "item.date: ${item.date}")

        val receiptDate = SimpleDateFormat("dd/MM/yyyy").parse(item.date)
        Log.i(TAG, "receiptDate: $receiptDate")

        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.time = receiptDate

        val localDateTime = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            0,
            0,
            0
        )
        Log.i(TAG, "localDateTime: $localDateTime")

        val now = LocalDateTime.now()
        Log.i(TAG, "now: $now")

        val dayDiff = localDateTime.until(now, ChronoUnit.DAYS)
        Log.i(TAG, "dayDiff: $dayDiff")

        textView.text = when(dayDiff.toInt()) {
            0 -> "Today"
            1 -> "Yesterday"
            in 2..6 -> {
                val simpleDateFormat = SimpleDateFormat("EEEE")
                val dayOfTheWeek = simpleDateFormat.format(receiptDate)

                dayOfTheWeek
            }
            else -> {item.date}
        }
    }
}

@BindingAdapter("bind:card")
fun setCreditCard(textView: TextView, item: Receipt?){
    val paymentMethod = when(item?.paymentMethod ?: 6) {
        1 -> "Debit"
        2 -> "Credit"
        3 -> "Cash"
        4 -> "Payment slip"
        5 -> "Pix"
        6 -> "Other"
        else -> {"Invalid payment"}
    }

    val result =
        if (item?.cardInfoBrand != null)
            "Payment method: $paymentMethod ${item.cardInfoBrand}"
        else
            "Payment method: $paymentMethod"

    textView.text = result
}