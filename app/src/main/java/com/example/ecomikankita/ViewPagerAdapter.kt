package com.example.ecomikankita

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ecomikankita.FirstFragment
import com.example.ecomikankita.FourthFragment
import com.example.ecomikankita.ThirdFragment

class ViewPagerAdapter(activity: FragmentActivity, private val username: String?) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 4 // Jumlah tab
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstFragment.newInstance(username ?: "")
            1 -> SecondFragment.newInstance(username ?: "")
            2 -> ThirdFragment.newInstance(username ?: "")
            3 -> FourthFragment.newInstance(username ?: "")
            else -> FirstFragment.newInstance(username ?: "")
        }
    }
}
