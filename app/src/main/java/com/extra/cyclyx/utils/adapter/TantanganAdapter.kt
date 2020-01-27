package com.extra.cyclyx.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.extra.cyclyx.databinding.ItemTantanganBinding
import com.extra.cyclyx.entity.Tantangan

class TantanganAdapter() : ListAdapter<Tantangan, TantanganAdapter.TantanganVH>(TrackingDiffUtil){
    companion object TrackingDiffUtil : DiffUtil.ItemCallback<Tantangan>(){
        override fun areItemsTheSame(oldItem: Tantangan, newItem: Tantangan): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Tantangan, newItem: Tantangan): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TantanganVH {
        return TantanganVH.from(parent)
    }

    override fun onBindViewHolder(holder: TantanganVH, position: Int) {
        val act = getItem(position)
        holder.bind(act)
    }


    class TantanganVH(private var binding : ItemTantanganBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(act: Tantangan){
            binding.tantangan = act
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TantanganVH {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTantanganBinding.inflate(layoutInflater, parent, false)
                return TantanganVH(binding)
            }
        }
    }
}