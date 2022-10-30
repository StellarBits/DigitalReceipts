package com.example.digitalreceipts.extension

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.digitalreceipts.api.model.Receipt
import com.example.digitalreceipts.ui.adapter.DataItem
import java.time.Instant
import java.util.*

/**
 * Função para ordenar a lista de forma descendente pela data do recibo.
 */
fun LiveData<List<Receipt>>.sortedByDate(): LiveData<List<Receipt>> =
    this.map { list ->
        list.sortedByDescending { receipts ->
            receipts.date
        }
    }

/**
 * Função para concatenar a lista de recibos com os headers gerados a partir da data dos mesmos.
 * Essa função grupa os recibos pela data.
 */
fun List<Receipt>.toListOfDataItem(): List<DataItem> {

    val grouping = this.groupBy { fields ->
        val simpleDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = simpleDate.format(Date.from(Instant.ofEpochSecond(fields.date)))
        currentDate
    }

    val listDataItem = mutableListOf<DataItem>()
    grouping.forEach { mapEntry ->
        listDataItem.add(DataItem.Header(mapEntry.key))
        listDataItem.addAll(
            mapEntry.value.map { fields ->
                DataItem.ReceiptsItem(fields)
            }
        )
    }

    return listDataItem
}