package com.example.ecomikankita

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class OrdersAdapter(private val ordersList: List<Order>) :
    RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = ordersList[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = ordersList.size

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.item_name)
        private val itemQuantity: TextView = itemView.findViewById(R.id.item_quantity)
        private val itemPrice: TextView = itemView.findViewById(R.id.item_price)
        private val itemImage: ImageView = itemView.findViewById(R.id.item_image)

        fun bind(order: Order) {
            itemName.text = order.nama_barang
            itemQuantity.text = "Jumlah: ${order.jumlah}"
            itemPrice.text = "Total Harga: ${order.total_harga}"
            Picasso.get().load(order.foto).into(itemImage)
        }
    }
}
