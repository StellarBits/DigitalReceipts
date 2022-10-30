package com.example.digitalreceipts.ui.receiptsdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digitalreceipts.api.model.Receipt

class ReceiptsDetailsViewModel : ViewModel() {

    // Esses campos servem para fazer o DataBinding com os componentes que irão exibir os dados
    // no layout. Eles são inicializados com strings vazias e podem ser alterados no método
    // initFields(), quando é recebido um recibo.
    val merchantImageField = MutableLiveData("")
    val merchantNameField = MutableLiveData("")
    val receiptValueField = MutableLiveData(0f)
    val dateField = MutableLiveData<Long>(0)
    private val cardInfoBrandField = MutableLiveData("")
    val authenticationField = MutableLiveData("")

    private val _receipt = MutableLiveData<Receipt>()

    var receipt: LiveData<Receipt> = _receipt

    // Recebe um recibo e atribui à variável _receipt
    fun receiveReceipt(receipts: Receipt) {
        with(receipts) {
            _receipt.value = receipts
            initFields(this)
        }
    }

    // Inicializa os campos da tela de detalhes com os valores do recibo recebido.
    private fun initFields(receipts: Receipt) {
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