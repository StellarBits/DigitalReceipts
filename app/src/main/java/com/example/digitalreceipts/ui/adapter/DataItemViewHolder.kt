package com.example.digitalreceipts.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.digitalreceipts.api.model.Receipt
import com.example.digitalreceipts.databinding.ReceiptsListHeaderBinding
import com.example.digitalreceipts.databinding.ReceiptsListItemBinding

/**
 * Essa classe é responsável por manter os dois ViewHolders (recibo e cabeçalho).
 * A classe selada serve para limitar a extensão da mesma. TODO estudar mais classes seladas
 */
sealed class DataItemViewHolder(open val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * ViewHolder foi implementado como uma classe aninhada pois é uma boa prática
     * realizar a inflação do ViewHolder a partir dele mesmo mesmo.
     * O binding dos dados e métodos também são responsabilidade da classe ViewHolder.
     * Também é necessário um binding object definido no XML do item.
     * TODO entender melhor a constante sobrescrita
     */
    class ReceiptsViewHolder(override val binding: ReceiptsListItemBinding) :
        DataItemViewHolder(binding) {

        /**
         * Esse método faz a vinculação dos dados com os componentes visuais usando
         * BindingAdapters.
         */
        fun bind(item: Receipt, cardListener: ReceiptsListener) {
            with(binding) {
                receipt = item
                listener = cardListener
                executePendingBindings()
            }
        }

        /**
         * Esse companion object permite que o ListAdapter obtenha uma instância
         * do ViewHolder passando apenas o parent. TODO estudar mais esse companion object
         */
        companion object {
            fun from(parent: ViewGroup): ReceiptsViewHolder {
                val binding: ReceiptsListItemBinding = ReceiptsListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ReceiptsViewHolder(binding)
            }
        }
    }

    /**
     * Essa classe representa um ViewHolder para o header da lista
     */
    class HeaderViewHolder(override val binding: ReceiptsListHeaderBinding) :
        DataItemViewHolder(binding) {

        fun bind(item: DataItem.Header) {
            with(binding) {
                header = item
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val binding: ReceiptsListHeaderBinding = ReceiptsListHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return HeaderViewHolder(binding)
            }
        }
    }
}