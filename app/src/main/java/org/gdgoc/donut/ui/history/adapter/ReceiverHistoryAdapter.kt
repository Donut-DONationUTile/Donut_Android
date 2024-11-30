package org.gdgoc.donut.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryReceiverGift
import org.gdgoc.donut.databinding.ItemHistoryUnusedBinding
import org.gdgoc.donut.databinding.ItemHistoryUsedBinding
import org.gdgoc.donut.util.DonutUtil

class ReceiverHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var itemList = emptyList<ResponseHistoryReceiverGift>()
    private var listener: ((ResponseHistoryReceiverGift, Int) -> Unit)? = null
    var mPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == OptionViewType.USED){
            return UsedItemViewHolder(ItemHistoryUsedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
        return UnusedItemViewHolder(ItemHistoryUnusedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(itemList[position].isUsed == "USED") (holder as UsedItemViewHolder).onBind(itemList[position])
        else (holder as UnusedItemViewHolder).onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class UsedItemViewHolder(private val binding: ItemHistoryUsedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseHistoryReceiverGift) {
            val date = data.dueDate.substring(0, 10)
            binding.tvCalendar.text = DonutUtil().setCalendarFormat(date)
            binding.tvName.text = data.product
            binding.tvDollar.text = data.price.toString()

            setClinkListenerOnPosition(data)
        }

        private fun setClinkListenerOnPosition(data: ResponseHistoryReceiverGift) {
            binding.clUsedItem.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(data, mPosition)
            }
        }
    }

    inner class UnusedItemViewHolder(private val binding: ItemHistoryUnusedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseHistoryReceiverGift) {
            val date = data.dueDate.substring(0, 10)
            binding.tvDayNum.text = DonutUtil().getDDayInfo(date).toString()
            binding.tvCalendar.text = DonutUtil().setCalendarFormat(date)
            binding.tvName.text = data.product
            binding.tvDollar.text = data.price.toString()

            setClinkListenerOnPosition(data)
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
    fun setGiftItemList(data: ResponseHistoryReceiverGift) {
        this.itemList = listOf(data)
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return when (itemList[position].isUsed == "USED") {
            true -> OptionViewType.USED
            false -> OptionViewType.UNUSED
        }
    }

    object OptionViewType {
        const val USED = 1
        const val UNUSED = 2
    }
}