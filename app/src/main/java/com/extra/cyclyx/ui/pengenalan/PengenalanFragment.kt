package com.extra.cyclyx.ui.pengenalan


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.extra.cyclyx.R
import com.extra.cyclyx.databinding.FragmentPengenalanBinding
import com.extra.cyclyx.ui.adapter.GambaranPageAdapter
import com.extra.cyclyx.ui.pengenalan.registrasi.RegistrasiDataDiriFragment
import com.extra.cyclyx.utils.IntroItem
import com.google.android.material.tabs.TabLayout
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PengenalanFragment : Fragment() {
    lateinit var binding : FragmentPengenalanBinding
    lateinit var mContext: Context
    lateinit var introViewPagerAdapterGambaran: GambaranPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPengenalanBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun loadLastScreen() {
        binding.linearLayoutSkip.visibility = View.INVISIBLE
        binding.linearLayoutGetStarted.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fill data description
        val mList: ArrayList<IntroItem> = ArrayList<IntroItem>()

        mList.add(
            IntroItem(
                "",
                "Life Is Like Riding a Bicycle. To Keep Your Balance, You Must Keep Moving - Albert Enstein",
                R.drawable.mountain_1
            )
        )

        mList.add(
            IntroItem(
                "",
                "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book",
                R.drawable.mountain_2
            )
        )

        mList.add(IntroItem("Tunggu apa lagi...", "Ayo daftar !", R.drawable.mountain_3))

        //Setup View Pager
        introViewPagerAdapterGambaran =
            GambaranPageAdapter(
                requireContext().applicationContext,
                mList
            )
        binding.screenViewPager.adapter = introViewPagerAdapterGambaran

        //setup tab Indicator
        binding.tabIndicator.setupWithViewPager(binding.screenViewPager)
        binding.tabIndicator.isClickable = false
        binding.wormDot.setViewPager(binding.screenViewPager)

        binding.btnSkip.setOnClickListener {
            binding.screenViewPager.setCurrentItem(binding.screenViewPager.currentItem + 1, true)
        }

        binding.tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == mList.size - 1) {
                    loadLastScreen()
                    binding.btnGetStarted.setOnClickListener {

                        val registrasiDataDiriFragment: RegistrasiDataDiriFragment =
                            RegistrasiDataDiriFragment()

                        fragmentManager?.beginTransaction()
                            ?.replace(R.id.frame_content, registrasiDataDiriFragment)
                            ?.addToBackStack(null)
                            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            ?.commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //do nothing
            }
        })
    }


}
