package com.example.ecomikankita

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class JumlahPembelianActivity : AppCompatActivity() {

    companion object {
        private const val ARG_USERNAME = "USERNAME"
        fun newInstance(username: String): Fragment {
            val fragment = FourthFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jumlah_pembelian)

        // Mengambil data dari intent
        val namaBarang = intent.getStringExtra("nama_barang")
        val fotoBarang = intent.getStringExtra("foto")
        val hargaBarang = intent.getIntExtra("harga", 0)
        val username = intent.getStringExtra("nama")

        // Mengatur data ke view
        findViewById<TextView>(R.id.pembelian_namaBarang).text = namaBarang
        findViewById<TextView>(R.id.pembelian_hargaBarang).text = "Rp $hargaBarang"

        Glide.with(this)
            .load(fotoBarang)
            .into(findViewById(R.id.pembelian_gambarBarang))

        // Tombol konfirmasi pembelian
        findViewById<Button>(R.id.button_konfirmasi).setOnClickListener {
            val jumlahBarang = findViewById<TextView>(R.id.jumlah_barang).text.toString().toInt()
            val totalHarga = hargaBarang * jumlahBarang

            // Simpan data pesanan ke Firebase
            val databaseReference = FirebaseDatabase.getInstance().getReference("users/rahmat adi syahputra/pesanan")
            val pesananId = databaseReference.push().key // Mendapatkan ID unik untuk pesanan baru
            if (pesananId != null) {
                val pesananItem = mapOf(
                    "nama" to username,
                    "foto" to fotoBarang,
                    "nama_barang" to namaBarang,
                    "jumlah" to jumlahBarang,
                    "total_harga" to totalHarga
                )
                databaseReference.child(pesananId).setValue(pesananItem)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pesanan berhasil dibuat", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Gagal membuat pesanan", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
