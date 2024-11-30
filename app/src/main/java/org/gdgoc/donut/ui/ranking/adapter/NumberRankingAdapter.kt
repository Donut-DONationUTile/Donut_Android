package org.gdgoc.donut.ui.ranking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.remote.response.ranking.ResponseNumberRankingData
import org.gdgoc.donut.databinding.ItemRankingNumberBinding

class NumberRankingAdapter : RecyclerView.Adapter<NumberRankingAdapter.NumberRankingViewHolder>() {
    var itemList = mutableListOf<ResponseNumberRankingData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NumberRankingViewHolder {val binding = ItemRankingNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return NumberRankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NumberRankingViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    class NumberRankingViewHolder(private val binding: ItemRankingNumberBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(data: ResponseNumberRankingData){
            binding.tvRankingNum.text = data.rank.toString()
            binding.tvName.text = data.name
            binding.tvNumberNum.text = data.number.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNumberRankingItemList(data: List<ResponseNumberRankingData>){
        itemList.clear()
        itemList.addAll(data.drop(1))
        notifyDataSetChanged()
    }
}