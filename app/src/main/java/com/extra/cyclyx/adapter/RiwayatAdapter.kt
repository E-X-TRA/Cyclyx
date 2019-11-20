package com.extra.cyclyx.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.extra.cyclyx.databinding.ItemRiwayatBinding
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.utils.DELETE_ITEM
import com.extra.cyclyx.utils.DETAIL_ITEM

class RiwayatAdapter(val clickListener: RiwayatClickListener) : ListAdapter<Bersepeda, RiwayatAdapter.RiwayatVH>(TrackingDiffUtil){
    companion object TrackingDiffUtil : DiffUtil.ItemCallback<Bersepeda>(){
        override fun areItemsTheSame(oldItem: Bersepeda, newItem: Bersepeda): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Bersepeda, newItem: Bersepeda): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatVH {
        return RiwayatVH.from(parent)
    }

    override fun onBindViewHolder(holder: RiwayatVH, position: Int) {
        val act = getItem(position)
        holder.bind(act,clickListener)
    }


    class RiwayatVH(private var binding : ItemRiwayatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(act: Bersepeda, clickListener : RiwayatClickListener){
            binding.bersepeda = act
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RiwayatVH {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRiwayatBinding.inflate(layoutInflater, parent, false)
                return RiwayatVH(binding)
            }
        }
    }
}

class RiwayatClickListener(val clickListener : (actsId : Long,action : String) -> Unit){
    fun onClickResult(acts : Bersepeda) = clickListener(acts.id, DETAIL_ITEM)
    fun onDeleteResult(acts: Bersepeda) = clickListener(acts.id, DELETE_ITEM)
}