package com.example.digitalreceipts.ui.receiptsdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digitalreceipts.api.model.Fields

class ReceiptsDetailsViewModel : ViewModel() {

    /**
     * Esses campos servem para fazer o DataBinding bidirecional com os componentes EditText
     * do layout. Eles são inicializados com strings vazias e podem ser alterados no método
     * initFields(), quando é recebido um BusinessCard. No momento da criação do cartão
     * o campo nome passa por um teste de validação.
     */
    val merchantImageField = MutableLiveData("")
    val merchantNameField = MutableLiveData("")
    val receiptValueField = MutableLiveData(0f)
    val dateField = MutableLiveData<Long>(0)
    val cardInfoBrandField = MutableLiveData("")
    val authenticationField = MutableLiveData("")

    private val _receipts = MutableLiveData<Fields>()

    var receipts: LiveData<Fields> = _receipts

    /**
     * Recebe um BusinessCard e atribui à variável _receivedCard
     */
    fun receiveReceipt(receipts: Fields) {
        with(receipts) {
            _receipts.value = receipts
            initFields(this)
        }
    }

    /**
     * Inicializa os campos do formulário com os valores do
     * cartão recebido.
     */
    private fun initFields(receipts: Fields) {
        with(receipts) {
            merchantImageField.value = message
            merchantNameField.value = merchantName
            receiptValueField.value = value
            dateField.value = date
            cardInfoBrandField.value = cardInfoBrand
            authenticationField.value = authentication
        }
    }
}