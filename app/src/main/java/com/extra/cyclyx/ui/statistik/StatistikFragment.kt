package com.extra.cyclyx.ui.statistik


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.extra.cyclyx.databinding.FragmentStatistikBinding
import com.extra.cyclyx.ui.adapter.StatistikViewPager

/**
 * A simple [Fragment] subclass.
 */
class StatistikFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = requireNotNull(activity)
        val binding = FragmentStatistikBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val statistikViewPager = StatistikViewPager(activity.applicationContext,childFragmentManager)
        binding.statistikViewPager.adapter = statistikViewPager
        binding.statistikTabLayout.setViewPager(binding.statistikViewPager)

        return binding.root
    }


}
