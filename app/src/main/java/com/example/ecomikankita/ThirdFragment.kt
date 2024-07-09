package com.example.ecomikankita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ThirdFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var favoriteProducts: MutableList<Product>

    private lateinit var database: DatabaseReference
    private lateinit var currentUser: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_favorites)
        recyclerView.layoutManager = LinearLayoutManager(context)
        favoriteProducts = mutableListOf()
        productAdapter = ProductAdapter(requireContext(), favoriteProducts)
        recyclerView.adapter = productAdapter

        currentUser = FirebaseAuth.getInstance().currentUser?.displayName ?: "rahmat adi syahputra"
        database = FirebaseDatabase.getInstance().reference.child("users").child(currentUser).child("favorit")

        loadFavoriteProducts()

        return view
    }

    private fun loadFavoriteProducts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteProducts.clear()
                for (productSnapshot in snapshot.children) {
                    val deskripsi = productSnapshot.child("deskripsi").value.toString()
                    val foto = productSnapshot.child("foto").value.toString()
                    val harga = productSnapshot.child("harga").value as Long
                    val nama_barang = productSnapshot.child("nama_barang").value.toString()

                    val product = Product(deskripsi, foto, harga, nama_barang)
                    favoriteProducts.add(product)
                }
                productAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            ThirdFragment().apply {
                arguments = Bundle().apply {
                    // You can pass arguments here if needed
                }
            }
    }
}
