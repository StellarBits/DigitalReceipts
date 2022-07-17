package com.example.digitalreceipts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.digitalreceipts.databinding.ReceiptsListItemBinding
import com.example.digitalreceipts.model.Fields

class ReceiptsListAdapter(
    private val listener: OnReceiptListener
) :
    ListAdapter<Fields, ReceiptsListAdapter.ReceiptsViewHolder>(DiffCallback) {

    inner class ReceiptsViewHolder(
        private var binding: ReceiptsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(fields: Fields) {
            binding.fields = fields

            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }

        override fun onClick(view: View) {
            listener.onReceiptClick(adapterPosition)
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Fields>() {
        override fun areItemsTheSame(oldItem: Fields, newItem: Fields): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Fields, newItem: Fields): Boolean {
            return oldItem.merchantName == newItem.merchantName
        }
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ReceiptsViewHolder {
        return ReceiptsViewHolder(
            ReceiptsListItemBinding.inflate(LayoutInflater.from(viewGroup.context))
        )
    }

    override fun onBindViewHolder(holder: ReceiptsViewHolder, position: Int) {
        val fields = getItem(position)
        holder.bind(fields)
    }

    interface OnReceiptListener {
        fun onReceiptClick(position: Int)
    }
}