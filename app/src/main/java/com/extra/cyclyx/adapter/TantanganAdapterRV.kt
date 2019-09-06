package com.extra.cyclyx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.extra.cyclyx.R
import com.extra.cyclyx.entity.Tantangan
import java.util.*

class TantanganAdapterRV(private val tantanganList : List<Tantangan>,val context : Context )
    : RecyclerView.Adapter<TantanganAdapterRV.TantanganItemHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TantanganItemHolder {
        return TantanganItemHolder(LayoutInflater.from(context).inflate(R.layout.tantangan_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return tantanganList.size;
    }

    override fun onBindViewHolder(holder: TantanganItemHolder, position: Int) {
        holder.bindItem(tantanganList[position])
    }

    class TantanganItemHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
//        nanti put id nya yaaa
//        val badge : ImageView = itemView.findViewById(R.id.)

        fun bindItem(data : Tantangan) {
            //nanti ngebind disini =))
        }
    }


}