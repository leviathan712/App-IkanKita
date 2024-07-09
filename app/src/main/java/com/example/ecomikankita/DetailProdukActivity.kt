package com.example.ecomikankita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ikankita.FavoritItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailProdukActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        // Mengambil data dari intent
        val namaBarang = intent.getStringExtra("nama_barang")
        val fotoBarang = intent.getStringExtra("foto")
        val deskripsiBarang = intent.getStringExtra("deskripsi")
        val hargaBarang = intent.getIntExtra("harga", 0)
        val username = intent.getStringExtra("USERNAME")

        // Mengatur data ke view
        findViewById<TextView>(R.id.detail_namaBarang).text = namaBarang
        findViewById<TextView>(R.id.detail_deskripsiBarang).text = deskripsiBarang
        findViewById<TextView>(R.id.detail_hargaBarang).text = "Rp $hargaBarang"

        Glide.with(this)
            .load(fotoBarang)
            .into(findViewById(R.id.detail_gambarBarang))

        // Tombol beli
        findViewById<Button>(R.id.button_beli).setOnClickListener {
            val intent = Intent(this, JumlahPembelianActivity::class.java).apply {
                putExtra("nama", username)
                putExtra("nama_barang", namaBarang)
                putExtra("foto", fotoBarang)
                putExtra("harga", hargaBarang)
            }
            startActivity(intent)
        }

        // Tombol favorit
        findViewById<Button>(R.id.button_favorit).setOnClickListener {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users/rahmat adi syahputra/favorit")
            val favoritItem = FavoritItem(
                nama_barang = namaBarang.toString(),
                foto = fotoBarang.toString(),
                deskripsi = deskripsiBarang.toString(),
                harga = hargaBarang
            )
            databaseReference.push().setValue(favoritItem)
                .addOnSuccessListener {
                    Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menambahkan ke Favorit", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
