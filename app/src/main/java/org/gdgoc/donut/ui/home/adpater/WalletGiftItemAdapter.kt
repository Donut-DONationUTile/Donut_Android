package org.gdgoc.donut.ui.home.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.remote.response.home.ResponseWalletGiftList
import org.gdgoc.donut.databinding.ItemHistoryUnusedBinding
import org.gdgoc.donut.util.DonutUtil

class WalletGiftItemAdapter : RecyclerView.Adapter<WalletGiftItemAdapter.UnusedItemViewHolder>() {
    var itemList = emptyList<ResponseWalletGiftList>()
    private var listener: ((ResponseWalletGiftList, Int) -> Unit)? = null
    var mPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):UnusedItemViewHolder {val binding = ItemHistoryUnusedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnusedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnusedItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class UnusedItemViewHolder(private val binding: ItemHistoryUnusedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseWalletGiftList) {
            val date = data.dueDate.substring(0, 10)
            binding.tvDayNum.text = DonutUtil().getDDayInfo(date).toString()
            binding.tvCalendar.text = DonutUtil().setCalendarFormat(date)
            binding.tvName.text = data.product
            binding.tvDollar.text = data.price.toString()
            setClinkListenerOnPosition(data)
        }

        private fun setClinkListenerOnPosition(data: ResponseWalletGiftList) {
            binding.clUnusedItem.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(data, mPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: ((ResponseWalletGiftList, Int) -> Unit)?) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGiftItemList(data: List<ResponseWalletGiftList>) {
        this.itemList = data
        notifyDataSetChanged()
    }
}