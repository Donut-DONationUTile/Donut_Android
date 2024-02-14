package org.gdsc.donut.ui.home.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdsc.donut.data.local.PackageItemData
import org.gdsc.donut.data.local.UnusedItemData
import org.gdsc.donut.databinding.ItemHistoryUnusedBinding
import org.gdsc.donut.databinding.ItemPackageBinding

class DetailItemAdapter : RecyclerView.Adapter<DetailItemAdapter.DetailItemViewHolder>() {
    val itemList = mutableListOf<UnusedItemData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailItemViewHolder {val binding = ItemHistoryUnusedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return DetailItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    class DetailItemViewHolder(private val binding: ItemHistoryUnusedBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(data: UnusedItemData){
            binding.tvDayNum.text = data.day
            binding.tvCalendar.text = data.day
            binding.tvName.text = data.name
            binding.tvDollar.text = data.price
        }
    }
}