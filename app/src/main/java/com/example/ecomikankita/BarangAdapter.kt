package com.example.ikankita

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomikankita.DetailProdukActivity
import com.example.ecomikankita.R

class BarangAdapter(private val barangList: List<Barang>, username: String?) : RecyclerView.Adapter<BarangAdapter.BarangViewHolder>() {

    inner class BarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gambarBarang: ImageView = itemView.findViewById(R.id.gambarBarang)
        val namaBarang: TextView = itemView.findViewById(R.id.namaBarang)
        val deskripsiBarang: TextView = itemView.findViewById(R.id.deskripsiBarang)
        val hargaBarang: TextView = itemView.findViewById(R.id.hargaBarang)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return BarangViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val barang = barangList[position]
        holder.namaBarang.text = barang.nama_barang
        holder.deskripsiBarang.text = barang.deskripsi
        holder.hargaBarang.text = "Rp ${barang.harga}"

        Glide.with(holder.itemView.context)
            .load(barang.foto)
            .into(holder.gambarBarang)

        // Menangani klik pada item
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailProdukActivity::class.java).apply {
                putExtra("nama_barang", barang.nama_barang)
                putExtra("foto", barang.foto)
                putExtra("deskripsi", barang.deskripsi)
                putExtra("harga", barang.harga)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = barangList.size
}

