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

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLinkButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi views
        usernameInput = findViewById(R.id.Username_Input_Box)
        passwordInput = findViewById(R.id.Password_Input_Box)
        loginButton = findViewById(R.id.Button_Masuk)
        registerLinkButton = findViewById(R.id.Button_Register_Link)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("users")

        // Setup tombol login
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim().lowercase()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Username dan Password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }

        registerLinkButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(username: String, password: String) {
        val hashedPassword = HashUtils.sha256(password)
        val userRef = database.child(username)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val savedPassword = snapshot.child("password").value as String
                    if (hashedPassword == savedPassword) {
                        // Login berhasil, arahkan ke activity selanjutnya (misalnya MainActivity)
                        Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                        // Di sini bisa tambahkan kode untuk pindah ke activity lain
                        // contoh:
                        // val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        // startActivity(intent)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Password salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Username tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
