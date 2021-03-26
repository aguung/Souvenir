package com.devfutech.souvenir.adapter.souvenir

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.AnimationUtils
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devfutech.souvenir.R
import com.devfutech.souvenir.data.local.entity.Souvenir
import com.devfutech.souvenir.databinding.ItemSouvenirBinding
import com.devfutech.souvenir.utils.toast


class SouvenirAdapter(private val listener: OnItemClickListener) :
    PagingDataAdapter<Souvenir, SouvenirAdapter.SouvenirViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: SouvenirViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: SouvenirViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SouvenirViewHolder {
        return SouvenirViewHolder(ItemSouvenirBinding.inflate(LayoutInflater.from(parent.context)))
    }

    inner class SouvenirViewHolder(private val binding: ItemSouvenirBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Souvenir?) {
            binding.apply {
                tvNama.text = item?.souvenirName
                tvAlamat.text = item?.souvenirAddress
                tvOpen.text = if (item?.souvenirOpenClosed == "") {
                    "-"
                } else {
                    item?.souvenirOpenClosed
                }
                tvTelp.text = if (item?.souvenirTelp == "") {
                    "-"
                } else {
                    item?.souvenirTelp
                }
                imageSouvenir.load(item?.souvenirImageUrl) {
                    crossfade(true)
                    error(R.drawable.icon)
                }
                root.animation = AnimationUtils.loadAnimation(root.context, R.anim.bounce_interpolar)
                container.setOnClickListener {
                    listener.onItemClick(item)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(items: Souvenir?)
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Souvenir>() {
            override fun areContentsTheSame(oldItem: Souvenir, newItem: Souvenir): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Souvenir, newItem: Souvenir): Boolean =
                oldItem.souvenirCode == newItem.souvenirCode
        }
    }
}