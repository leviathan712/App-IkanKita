package com.example.ikankita

data class Barang(
    val nama_barang: String = "",
    val foto: String = "",
    val deskripsi: String = "",
    val harga: Int = 0,
    var kategori: String = "", // Menambahkan field kategori
    var username: String = "" // Menambahkan field username
)

