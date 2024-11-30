package org.gdgoc.donut.ui.home.adpater

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.data.remote.response.home.ResponseWalletImpendingList
import org.gdgoc.donut.databinding.ItemWalletImminentBinding
import org.gdgoc.donut.ui.viewModel.DonationViewModel
import org.gdgoc.donut.util.DonutUtil

class WalletImpedingItemAdapter(private val viewModel: DonationViewModel) : RecyclerView.Adapter<WalletImpedingItemAdapter.UnusedItemViewHolder>() {
    var itemList = emptyList<ResponseWalletImpendingList>()
    private var listener: ((ResponseWalletImpendingList, Int) -> Unit)? = null
    var mPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):UnusedItemViewHolder {val binding = ItemWalletImminentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UnusedItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnusedItemViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class UnusedItemViewHolder(private val binding: ItemWalletImminentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ResponseWalletImpendingList) {
            val date = data.dueDate.substring(0, 10)
            binding.tvDayNum.text = DonutUtil().getDDayInfo(date).toString()
            binding.tvCalendar.text = DonutUtil().setCalendarFormat(date)
            binding.tvName.text = data.product
            binding.tvDollar.text = data.price.toString()
            binding.clDonate.setOnClickListener {
                DonutSharedPreferences.getAccessToken()?.let { viewModel.requestDirectDonation(it, data.giftId) }
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setImpendingItemList(data: List<ResponseWalletImpendingList>) {
        this.itemList = data
        notifyDataSetChanged()
    }
}