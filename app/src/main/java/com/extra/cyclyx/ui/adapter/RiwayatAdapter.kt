package com.extra.cyclyx.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.extra.cyclyx.databinding.ItemRiwayatBinding
import com.extra.cyclyx.databinding.ItemRiwayatErrorBinding
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.utils.DELETE_ITEM
import com.extra.cyclyx.utils.DETAIL_ITEM
import com.extra.cyclyx.utils.RIWAYAT_ITEM_ERROR
import com.extra.cyclyx.utils.RIWAYAT_ITEM_NORMAL

class RiwayatAdapter(val clickListener: RiwayatClickListener) : ListAdapter<Bersepeda, RiwayatAdapter.BaseRiwayatVH<*>>(TrackingDiffUtil){
    companion object TrackingDiffUtil : DiffUtil.ItemCallback<Bersepeda>(){
        override fun areItemsTheSame(oldItem: Bersepeda, newItem: Bersepeda): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Bersepeda, newItem: Bersepeda): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when(item.finished){
            true -> {
                if(item.speed >= 0){
                    RIWAYAT_ITEM_NORMAL
                }else{
                    RIWAYAT_ITEM_ERROR
                }
            }
            false -> RIWAYAT_ITEM_ERROR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRiwayatVH<*> {
        return when(viewType){
            RIWAYAT_ITEM_ERROR -> {
                RiwayatErrorVH.from(parent)
            }
            RIWAYAT_ITEM_NORMAL -> {
                RiwayatVH.from(parent)
            }
            else -> throw IllegalArgumentException("Invalid View Type")
        }
    }

    override fun onBindViewHolder(holder: BaseRiwayatVH<*>, position: Int) {
        val act = getItem(position)
        holder.bind(act,clickListener)
    }

    abstract class BaseRiwayatVH<T>(itemView : View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(act : Bersepeda, clickListener: RiwayatClickListener)
    }

    class RiwayatVH(val binding : ItemRiwayatBinding) : BaseRiwayatVH<ItemRiwayatBinding>(binding.root) {
        override fun bind(act: Bersepeda, clickListener : RiwayatClickListener){
            binding.bersepeda = act
            binding.clickListener = clickListener
            binding.cvRiwayat.setOnLongClickListener {
                clickListener.onDeleteResult(act)
                true
            }
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

    class RiwayatErrorVH(val binding : ItemRiwayatErrorBinding) : BaseRiwayatVH<ItemRiwayatErrorBinding>(binding.root) {
        override fun bind(act: Bersepeda, clickListener : RiwayatClickListener){
            binding.bersepeda = act
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RiwayatErrorVH {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRiwayatErrorBinding.inflate(layoutInflater, parent, false)
                return RiwayatErrorVH(binding)
            }
        }
    }

    class RiwayatClickListener(val clickListener : (actsId : Long,action : String) -> Unit){
        fun onClickResult(acts : Bersepeda) = clickListener(acts.id, DETAIL_ITEM)
        fun onDeleteResult(acts: Bersepeda) = clickListener(acts.id, DELETE_ITEM)
    }
}