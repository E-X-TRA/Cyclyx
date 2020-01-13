package com.extra.cyclyx.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.extra.cyclyx.databinding.ItemTantanganBinding
import com.extra.cyclyx.entity.Tantangan
import com.extra.cyclyx.utils.DELETE_ITEM
import com.extra.cyclyx.utils.DETAIL_ITEM

class TantanganAdapter(val clickListener: TantanganClickListener) : ListAdapter<Tantangan, TantanganAdapter.TantanganVH>(TrackingDiffUtil){
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
        holder.bind(act,clickListener)
    }


    class TantanganVH(private var binding : ItemTantanganBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(act: Tantangan, clickListener : TantanganClickListener){
            binding.tantangan = act
            binding.clickListener = clickListener
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

class TantanganClickListener(val clickListener : (actsId : Int,action : String) -> Unit){
    fun onClickResult(acts : Tantangan) = clickListener(acts.id, DETAIL_ITEM)
    fun onDeleteResult(acts: Tantangan) = clickListener(acts.id, DELETE_ITEM)
}