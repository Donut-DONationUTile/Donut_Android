package org.gdgoc.donut.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryReceiverGift
import org.gdgoc.donut.databinding.ItemHistoryUsedBinding
import org.gdgoc.donut.util.DonutUtil

class UsedItemAdapter : RecyclerView.Adapter<UsedItemAdapter.UsedItemViewHolder>() {
    val itemList = mutableListOf<ResponseHistoryReceiverGift>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsedItemViewHolder {val binding = ItemHistoryUsedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return UsedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsedItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    class UsedItemViewHolder(private val binding: ItemHistoryUsedBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(data: ResponseHistoryReceiverGift){
            val date = data.dueDate.substring(0, 10)
            if(data.isUsed == "USED"){
                binding.tvCalendar.text = DonutUtil().setCalendarFormat(date)
                binding.tvName.text = data.product
                binding.tvDollar.text = data.price.toString()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGiftItemList(data: List<ResponseHistoryReceiverGift>) {
        itemList.clear()
        itemList.addAll(data.filter { it.isUsed == "USED" })
        notifyDataSetChanged()
    }
}