package org.gdgoc.donut.ui.ranking.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.remote.response.ranking.ResponsePriceRankingData
import org.gdgoc.donut.databinding.ItemRankingNumberBinding

class SumRankingAdapter : RecyclerView.Adapter<SumRankingAdapter.SumRankingViewHolder>() {
    var itemList = mutableListOf<ResponsePriceRankingData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SumRankingViewHolder {val binding = ItemRankingNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SumRankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SumRankingViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    class SumRankingViewHolder(private val binding: ItemRankingNumberBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(data: ResponsePriceRankingData){
            binding.tvRankingNum.text = data.rank.toString()
            binding.tvName.text = data.name
            binding.tvNumberNum.text = data.price.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPriceRankingItemList(data: List<ResponsePriceRankingData>){
        itemList.clear()
        itemList.addAll(data.drop(1))
        notifyDataSetChanged()
    }
}