package org.gdgoc.donut.ui.history.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.gdgoc.donut.R
import org.gdgoc.donut.databinding.ItemFilterMonthsBinding

class MonthAdapter : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {
    val itemList = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    private var listener: ((String, Int) -> Unit)? = null
    var mPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthViewHolder {val binding = ItemFilterMonthsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.onBind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class MonthViewHolder(private val binding: ItemFilterMonthsBinding): RecyclerView.ViewHolder(binding.root){
        fun onBind(data: String){
            binding.tvMonth.text = data
            setClinkListenerOnPosition(data)
        }

        @SuppressLint("ResourceAsColor")
        private fun setClinkListenerOnPosition(data: String){
            binding.clMonth.setOnClickListener {
                val pos = adapterPosition
                mPosition = pos
                listener?.invoke(data, mPosition)
                binding.clMonth.isSelected = !binding.clMonth.isSelected
                if(binding.clMonth.isSelected){
                    binding.clMonth.setBackgroundResource(R.drawable.bg_white_maincoral_round20)
                    binding.tvMonth.setTextColor(ContextCompat.getColor(binding.root.context, R.color.main_coral))
                }
                if(!binding.clMonth.isSelected){
                    binding.clMonth.setBackgroundResource(R.drawable.bg_gray100_round20)
                    binding.tvMonth.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black_100))
                }
            }
        }
    }

    fun setOnItemClickListener(listener: ((String, Int) -> Unit)?) {
        this.listener = listener
    }

}