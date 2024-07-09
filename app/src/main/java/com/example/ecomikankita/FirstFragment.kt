package com.example.ecomikankita

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    private lateinit var welcomeTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)

        welcomeTextView = view.findViewById(R.id.welcome_text_view)

        val username = arguments?.getString("USERNAME")
        welcomeTextView.text = "Selamat datang,\n$username!"

        return view
    }

    companion object {
        fun newInstance(username: String): FirstFragment {
            val fragment = FirstFragment()
            val args = Bundle()
            args.putString("USERNAME", username)
            fragment.arguments = args
            return fragment
        }
    }
}

