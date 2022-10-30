package com.example.digitalreceipts.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.digitalreceipts.api.model.Receipt
import com.example.digitalreceipts.extension.toListOfDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Constantes responsáveis por indicar qual o tipo de item que será
 * retornado (header ou receipt).
 */
private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

/**
 * O adapter implementa a interface ListAdapter com DiffUtil; tanto a inflação do layout
 * quanto a vinculação de dados ocorrem na classe ViewHolder.
 * Ao invés de receber uma lista de Receipts direto, esse adapter recebe um List<DataItem>,
 * que é a superclasse dos tipos concretos de dados (Receipts ou Header).
 */
class ReceiptsAdapter(private val receiptsListener: ReceiptsListener) : ListAdapter<DataItem,
        DataItemViewHolder>(ReceiptsDiffCallback()) {
    /**
     * Escopo de corrotina específico para o Adapter. Responsável por processar a lista em
     * concorrência e otimizar o desempenho.
     */
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataItemViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> DataItemViewHolder.HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> DataItemViewHolder.ReceiptsViewHolder.from(parent)
            else -> throw ClassCastException("ViewType desconhecido $viewType")
        }
    }

    /**
     * Seleciona o binding dos dados conforme o tipo de ViewHolder.
     * Caso recibos, todos os dados. Caso header, apenas as datas dos recibos.
     */
    override fun onBindViewHolder(holder: DataItemViewHolder, position: Int) {
        when (holder) {
            is DataItemViewHolder.ReceiptsViewHolder -> {
                val item = getItem(position) as DataItem.ReceiptsItem
                holder.bind(item.receipts, receiptsListener)
            }
            is DataItemViewHolder.HeaderViewHolder -> {
                val item = getItem(position) as DataItem.Header
                holder.bind(item)
            }
        }
    }

    /**
     * A partir da posição do objeto da lista, retorna qual o tipo do ViewHolder.
     */
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ReceiptsItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    /**
     * Processa a lista recebida (seja de uma api ou de um banco de dados) para agrupar
     * os recibos conforme a data do mesmo e submete. O processamento acontece
     * no escopo específico do Adapter (thread default) para não bloquear a Thread main.
     */
    fun addHeadersAndSubmitList(list: List<Receipt>?) {

        adapterScope.launch {
            val listDataItem = list?.toListOfDataItem()
            withContext(Dispatchers.Main) {
                submitList(listDataItem)
            }
        }
    }

    /**
     * Essa classe implementa a interface DiffUtil.ItemCallback<T>. O código aqui é
     * quase boilerplate. TODO entender melhor o Diff Callback
     */
    class ReceiptsDiffCallback : DiffUtil.ItemCallback<DataItem>() {

        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}