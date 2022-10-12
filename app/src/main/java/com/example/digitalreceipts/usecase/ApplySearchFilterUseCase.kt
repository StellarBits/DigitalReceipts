package com.example.digitalreceipts.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.example.digitalreceipts.api.model.Receipt

/**
 * Esse use case filtra a lista conforme a string searchQuery, usando a função
 * Transformations.switchMap { }. Ele observa mudanças no valor da searchQuery
 * e atualiza a lista, emitindo-a como uma LiveData<List<Receipt>>. O filtro
 * busca por matches em 3 campos: nome, email e empresa.
 */
class ApplySearchFilterUseCase {
    fun filterList(
        list: LiveData<List<Receipt>>,
        searchQuery: LiveData<CharSequence>
    ): LiveData<List<Receipt>> =
        Transformations.switchMap(searchQuery) {
            list.map { list ->
                searchQuery.value?.let {
                    list.filter { receipts ->
                        val query = searchQuery.value.toString()
                        with(receipts) {
                            applyQuery(query)
                        }
                    }
                }
            }
        }

    private fun Receipt.applyQuery(query: String) =
        merchantName.contains(query, true) /*||
                nome.contains(query, true) ||
                email.contains(query, true)*/
}