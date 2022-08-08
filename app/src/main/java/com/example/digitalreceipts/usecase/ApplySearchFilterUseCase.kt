package com.example.digitalreceipts.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.example.digitalreceipts.api.model.Fields

/**
 * Esse use case filtra a lista conforme a string searchQuery, usando a função
 * Transformations.switchMap { }. Ele observa mudanças no valor da searchQuery
 * e atualiza a lista, emitindo-a como uma LiveData<List<BusinessCard>>. O filtro
 * busca por matches em 3 campos: nome, email e empresa.
 */
class ApplySearchFilterUseCase {
    fun filterList(
        list: LiveData<List<Fields>>,
        searchQuery: LiveData<CharSequence>
    ): LiveData<List<Fields>> =
        Transformations.switchMap(searchQuery) {
            list.map { list ->
                searchQuery.value?.let {
                    list.filter { businessCard ->
                        val query = searchQuery.value.toString()
                        with(businessCard) {
                            applyQuery(query)
                        }
                    }
                }
            }
        }

    private fun Fields.applyQuery(query: String) =
        merchantName.contains(query, true) /*||
                nome.contains(query, true) ||
                email.contains(query, true)*/
}