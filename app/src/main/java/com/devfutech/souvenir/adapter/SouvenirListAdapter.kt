package com.devfutech.souvenir.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devfutech.souvenir.R
import com.devfutech.souvenir.data.local.entity.Souvenir
import com.devfutech.souvenir.databinding.ItemSouvenirBinding

class SouvenirListAdapter(val onItemClick: (Souvenir?) -> Unit) :
    ListAdapter<Souvenir, SouvenirListAdapter.SouvenirListAdapterViewHolder>(POST_COMPARATOR) {

    override fun onBindViewHolder(holder: SouvenirListAdapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: SouvenirListAdapterViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }
        else{
            for (payload in payloads) {
                if (payload == PAYLOAD_TITLE) {
                    holder.bind(getItem(position))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SouvenirListAdapterViewHolder {
        return SouvenirListAdapterViewHolder(ItemSouvenirBinding.inflate(LayoutInflater.from(parent.context)))
    }

    inner class SouvenirListAdapterViewHolder(private val binding: ItemSouvenirBinding) :
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

                root.animation = AnimationUtils.loadAnimation(
                    root.context,
                    R.anim.bounce_interpolar
                )
                container.setOnClickListener {
                    onItemClick(item)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun onItemSouvenirClick(items: Souvenir?)
    }

    companion object {
        const val PAYLOAD_TITLE = "PAYLOAD_TITLE"
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Souvenir>() {
            override fun areContentsTheSame(oldItem: Souvenir, newItem: Souvenir): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Souvenir, newItem: Souvenir): Boolean =
                oldItem.souvenirCode == newItem.souvenirCode

        }
    }
}