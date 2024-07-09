package com.example.ecomikankita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomikankita.R
import com.example.ikankita.Barang
import com.example.ikankita.BarangAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SecondFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var barangList: MutableList<Barang>
    private lateinit var adapter: BarangAdapter

    companion object {
        private const val ARG_USERNAME = "USERNAME"

        fun newInstance(username: String): SecondFragment {
            val fragment = SecondFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        barangList = mutableListOf()
        adapter = BarangAdapter(barangList, username)
        recyclerView.adapter = adapter

        // Mendapatkan referensi untuk masing-masing kategori
        val databaseIkanHiasAirLaut = FirebaseDatabase.getInstance().getReference("penjualan/ikan_hias_air_laut")
        val databaseIkanHiasAirTawar = FirebaseDatabase.getInstance().getReference("penjualan/ikan_hias_air_tawar")
        val databaseAsesoris = FirebaseDatabase.getInstance().getReference("penjualan/asesoris")

        fetchBarangData(databaseIkanHiasAirLaut, "ikan_hias_air_laut")
        fetchBarangData(databaseIkanHiasAirTawar, "ikan_hias_air_tawar")
        fetchBarangData(databaseAsesoris, "asesoris")
    }

    private fun fetchBarangData(database: DatabaseReference, kategori: String) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val barang = dataSnapshot.getValue(Barang::class.java)
                    if (barang != null) {
                        barang.kategori = kategori // Menyimpan kategori
                        barangList.add(barang)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}

