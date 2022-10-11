package com.example.digitalreceipts.extension

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.digitalreceipts.api.model.Fields
import com.example.digitalreceipts.ui.adapter.DataItem
import java.time.Instant
import java.util.*

/**
 * Uma função de extensão para ordenar a lista alfabeticamente pelo
 * nome do contato.
 */
fun LiveData<List<Fields>>.sortedByDate(): LiveData<List<Fields>> =
    this.map { list ->
        list.sortedByDescending { receipts ->
            receipts.date
        }
    }

/**
 * Essa função de extensão é usada para concatenar a lista de BusinessCard
 * com os cabeçalhos gerados a partir da primeira letra do nome. Em outras
 * palavras: agrupar os contatos pela inicial.
 */
fun List<Fields>.toListOfDataItem(): List<DataItem> {

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