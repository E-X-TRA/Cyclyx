package com.extra.cyclyx.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.extra.cyclyx.R
import com.extra.cyclyx.entity.Bersepeda
import com.extra.cyclyx.entity.Tantangan
import org.w3c.dom.Text

class RiwayatAdapterRV(private val bersepedaList : List<Bersepeda>, val context : Context )
    : RecyclerView.Adapter<RiwayatAdapterRV.RiwayatItemHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatItemHolder {
        return RiwayatItemHolder(LayoutInflater.from(context).inflate(R.layout.riwayat_list_item,parent,false))
    }

    override fun getItemCount(): Int {
        return bersepedaList.size;
    }

    override fun onBindViewHolder(holder: RiwayatItemHolder, position: Int) {
        holder.bindItem(bersepedaList[position])
    }

    class RiwayatItemHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val tanggalR : TextView = itemView.findViewById(R.id.tv_itemR_tanggal);
        val waktuR : TextView = itemView.findViewById(R.id.tv_itemR_waktu);
        val jarakR : TextView = itemView.findViewById(R.id.tv_itemR_jarak);

        fun bindItem(data : Bersepeda) {
            tanggalR.text = data.tanggalBersepeda.toString()
            waktuR.text = data.waktuBersepeda.toString()
            jarakR.text = data.jarak.toString()
        }
    }


}