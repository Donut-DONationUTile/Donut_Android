package org.gdgoc.donut.ui.home.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverBox
import org.gdgoc.donut.databinding.ItemPackageBinding
import org.gdgoc.donut.util.DonutUtil

class PackageItemAdapter : RecyclerView.Adapter<PackageItemAdapter.PackageItemViewHolder>() {
    var itemList = emptyList<ResponseHomeReceiverBox>()
    private var listener: ((ResponseHomeReceiverBox, Int) -> Unit)? = null
    var mPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PackageItemViewHolder {
        val binding = ItemPackageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PackageItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PackageItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class PackageItemViewHolder(private val binding: ItemPackageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun onBind(data: ResponseHomeReceiverBox) {
            val date = data.dueDate.substring(0, 10)
            binding.tvDayNum.text = DonutUtil().getDDayInfo(date).toString()
            binding.tvCalendar.text = DonutUtil().setCalendarFormat(date)
            binding.tvName.text = data.store
            binding.tvDollar.text = data.amount.toString()

            setClinkListenerOnPosition(data)
        }

        private fun setClinkListenerOnPosition(data: ResponseHomeReceiverBox) {
            binding.clPackageItem.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(data, mPosition)
            }
        }
    }

    fun setOnItemClickListener(listener: ((ResponseHomeReceiverBox, Int) -> Unit)?) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBoxItemList(data: List<ResponseHomeReceiverBox>) {
        this.itemList = data
        notifyDataSetChanged()
    }
}