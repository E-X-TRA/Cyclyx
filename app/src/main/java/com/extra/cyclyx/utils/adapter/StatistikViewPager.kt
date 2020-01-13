package com.extra.cyclyx.utils.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.extra.cyclyx.ui.statistik.riwayat.RiwayatFragment
import com.extra.cyclyx.ui.statistik.tantangan.TantanganFragment

class StatistikViewPager (private val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = TantanganFragment()
            1 -> fragment = RiwayatFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = 2
}
