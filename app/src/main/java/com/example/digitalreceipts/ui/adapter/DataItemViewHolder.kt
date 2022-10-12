package com.example.digitalreceipts.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.digitalreceipts.api.model.Receipt
import com.example.digitalreceipts.databinding.ReceiptsListHeaderBinding
import com.example.digitalreceipts.databinding.ReceiptsListItemBinding

/**
 * Essa classe mantém os diferentes ViewHolders. Optei por usar uma classe selada para limitar
 * a extensão dessa classe.
 */
sealed class DataItemViewHolder(open val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /**
     * Implementei o ViewHolder como uma classe aninhada para conseguir adotar a boa prática
     * de fazer a inflação do ViewHolder a partir de si mesmo. O binding dos dados e métodos
     * também é responsabilidade da classe ViewHolder. Necessita também um binding object
     * definido no XML do item.
     */
    class ReceiptsViewHolder(override val binding: ReceiptsListItemBinding) :
        DataItemViewHolder(binding) {

        /**
         * Esse método faz a vinculação dos dados com os componentes visuais. É possível fazer
         * tudo manualmente aqui na classe ViewHolder, mas preferi adotar BindingAdapters para
         * deixar a solução mais flexível e fácil de manter.
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
         * do ViewHolder passando apenas o parent.
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