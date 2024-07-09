package com.example.ecomikankita

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class FourthFragment : Fragment() {

    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var changePhotoButton: Button
    private lateinit var logoutButton: Button
    private lateinit var orderHistoryButton: ImageButton
    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private var username: String? = null

    companion object {
        private const val ARG_USERNAME = "USERNAME"
        private const val PICK_IMAGE_REQUEST = 71

        @JvmStatic
        fun newInstance(username: String): FourthFragment {
            val fragment = FourthFragment()
            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance().reference
        arguments?.let {
            username = it.getString(ARG_USERNAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fourth, container, false)

        profileImage = view.findViewById(R.id.profile_image)
        profileName = view.findViewById(R.id.profile_name)
        changePhotoButton = view.findViewById(R.id.change_photo_button)
        logoutButton = view.findViewById(R.id.logout_button)
        orderHistoryButton = view.findViewById(R.id.tombol_riwayat_pesanan)

        profileName.text = "$username"
        loadUserProfile()

        changePhotoButton.setOnClickListener {
            chooseImage()
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        orderHistoryButton.setOnClickListener {
            val intent = Intent(activity, OrderHistoryActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        return view
    }

    private fun loadUserProfile() {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        username?.let { username ->
            usersRef.child(username).child("imageUrl").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val imageUrl = snapshot.getValue(String::class.java)
                    imageUrl?.let {
                        Picasso.get()
                            .load(it)
                            .transform(CircleTransform())
                            .into(profileImage)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Gagal memuat gambar profil: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    class CircleTransform : com.squareup.picasso.Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)

            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, source.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader
            paint.isAntiAlias = true

            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)

            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar Profil"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val filePath = data.data
            filePath?.let { uploadImage(it) }
        }
    }

    private fun uploadImage(filePath: Uri) {
        val storageRef = storageRef.child("profile_pictures/${username}.jpg")
        storageRef.putFile(filePath)
            .addOnSuccessListener {
                Toast.makeText(context, "Upload berhasil", Toast.LENGTH_SHORT).show()
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    updateUserProfile(uri.toString())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Upload gagal: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserProfile(imageUrl: String) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")
        username?.let { username ->
            usersRef.child(username).child("imageUrl").setValue(imageUrl)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    Picasso.get().load(imageUrl).into(profileImage)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Gagal memperbarui profil: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
