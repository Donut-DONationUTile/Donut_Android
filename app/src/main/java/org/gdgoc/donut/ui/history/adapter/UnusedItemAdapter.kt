package org.gdgoc.donut.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryReceiverGift
import org.gdgoc.donut.databinding.ItemHistoryUnusedBinding
import org.gdgoc.donut.util.DonutUtil

class UnusedItemAdapter : RecyclerView.Adapter<UnusedItemAdapter.UnusedItemViewHolder>() {
    var itemList = mutableListOf<ResponseHistoryReceiverGift>()
    private var listener: ((ResponseHistoryReceiverGift, Int) -> Unit)? = null
    var mPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UnusedItemViewHolder {val binding = ItemHistoryUnusedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnusedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnusedItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class UnusedItemViewHolder(private val binding: ItemHistoryUnusedBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(data: ResponseHistoryReceiverGift){
            val date = data.dueDate.substring(0, 10)
            if(data.isUsed == "UNUSED"){
                binding.tvDayNum.text = DonutUtil().getDDayInfo(date).toString()
                binding.tvCalendar.text = DonutUtil().setCalendarFormat(date)
                binding.tvName.text = data.product
                binding.tvDollar.text = data.price.toString()

                setClinkListenerOnPosition(data)
            }
        }

        private fun setClinkListenerOnPosition(data: ResponseHistoryReceiverGift) {
            binding.clUnusedItem.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(data, mPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: ((ResponseHistoryReceiverGift, Int) -> Unit)?) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGiftItemList(data: List<ResponseHistoryReceiverGift>) {
        itemList.clear()
        itemList.addAll(data.filter { it.isUsed == "UNUSED" })
        notifyDataSetChanged()
    }
}