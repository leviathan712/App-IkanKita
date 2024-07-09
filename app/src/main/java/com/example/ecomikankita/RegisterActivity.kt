package com.example.ecomikankita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var registerButton: Button
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("users")

        // Inisialisasi views
        usernameInput = findViewById(R.id.Username_Input_Register)
        passwordInput = findViewById(R.id.Password_Input_Register)
        registerButton = findViewById(R.id.Button_Register)

        // Setup onClickListener untuk register button
        registerButton.setOnClickListener {
            val username = usernameInput.text.toString().trim().lowercase()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(username, password)
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        // Hashing password sebelum menyimpan ke Firebase Database
        val hashedPassword = HashUtils.sha256(password)

        // Periksa apakah username sudah ada di Firebase Database
        val userRef = database.child(username)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(this@RegisterActivity, "Username sudah digunakan", Toast.LENGTH_SHORT).show()
                } else {
                    // Tambahkan data pengguna baru ke Firebase Database
                    userRef.child("password").setValue(hashedPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this@RegisterActivity, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                                // Setelah registrasi berhasil, navigasi ke LoginActivity
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish() // Optional: untuk menutup RegisterActivity setelah navigasi
                            } else {
                                Toast.makeText(this@RegisterActivity, "Registrasi gagal, coba lagi", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity, "Terjadi kesalahan: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}