package com.devfutech.souvenir.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devfutech.souvenir.R
import com.devfutech.souvenir.data.local.entity.Souvenir
import com.devfutech.souvenir.databinding.ItemCategoryBinding
import com.devfutech.souvenir.utils.changeDrawableColor

class CategoryAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Souvenir, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {
    private var selected = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, position)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(items: Souvenir, position: Int) {
            binding.apply {
                name.text = items.souvenirCategory
                when (items.souvenirCategory) {
                    "Makanan" -> name.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        R.drawable.ic_baseline_fastfood_24,
                        0,
                        0
                    )
                    "Souvenir" -> name.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        R.drawable.ic_baseline_shopping_basket_24,
                        0,
                        0
                    )
                    else -> name.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        R.drawable.ic_baseline_dashboard_24,
                        0,
                        0
                    )
                }
                if (selected == position) {
                    materialCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.colorPrimary
                        )
                    )
                    name.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                    name.changeDrawableColor(R.color.white)
                } else {
                    materialCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.white
                        )
                    )
                    name.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                    name.changeDrawableColor(R.color.black)
                }

                root.setOnClickListener {
                    if (position != RecyclerView.NO_POSITION) {
                        val category = getItem(position)
                        listener.onItemCategoryClick(category)
                        selected = position
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemCategoryClick(items: Souvenir)
    }

    class DiffCallback : DiffUtil.ItemCallback<Souvenir>() {
        override fun areItemsTheSame(oldItem: Souvenir, newItem: Souvenir) =
            oldItem.souvenirCode == newItem.souvenirCode

        override fun areContentsTheSame(oldItem: Souvenir, newItem: Souvenir) =
            oldItem == newItem
    }
}