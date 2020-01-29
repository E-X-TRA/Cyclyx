package com.extra.cyclyx.ui.pengenalan.gambaran


import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.extra.cyclyx.R
import com.extra.cyclyx.ui.pengenalan.registrasi.RegistrasiDataDiriFragment
import com.extra.cyclyx.utils.IntroItem
import com.google.android.material.tabs.TabLayout
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import java.util.ArrayList
import android.content.Context as Context

/**
 * A simple [Fragment] subclass.
 */
class GambaranCoverFragment : Fragment() {
    lateinit var screenPager: ViewPager

    lateinit var introViewPagerAdapter: PageAdapter

    lateinit var tabIndicator: TabLayout

    lateinit var btnSkip: Button

    lateinit var btngetStarted: Button

    lateinit var linearLayoutSkip: LinearLayout

    lateinit var linearLayoutGetStarted: LinearLayout

    lateinit var dotsIndicatorIn : DotsIndicator

    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pengenalan_cover, container, false)


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengenalan_cover, container, false)
    }

    private fun loadLastScreen() {
        linearLayoutSkip.visibility = View.INVISIBLE
        linearLayoutGetStarted.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSkip = view.findViewById(R.id.btn_skip)
        btngetStarted = view.findViewById(R.id.btn_get_started)

        linearLayoutSkip = view.findViewById(R.id.linear_layout_skip)
        linearLayoutGetStarted = view.findViewById(R.id.linear_layout_get_started)

        tabIndicator = view.findViewById(R.id.tab_indicator)

        dotsIndicatorIn = view.findViewById(R.id.worm_dot)

        // Fill data description
        val mList: ArrayList<IntroItem> = ArrayList<IntroItem>()

        mList.add(IntroItem("","Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book", R.drawable.mountain_1))

        mList.add(IntroItem("", "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book", R.drawable.mountain_2))

        mList.add(IntroItem("Tunggu apa lagi..", "Ayo daftar !", R.drawable.mountain_3))

        //Setup View Pager
        screenPager = view.findViewById(R.id.screen_viewpager)
        val introViewPagerAdapter = PageAdapter(requireContext().applicationContext, mList)
        screenPager.adapter = introViewPagerAdapter

        //setup tab Indicator
        tabIndicator.setupWithViewPager(screenPager)
        tabIndicator.isClickable = false
        dotsIndicatorIn.setViewPager(screenPager)

        btnSkip.setOnClickListener {
            screenPager.setCurrentItem(screenPager.currentItem+1, true)
        }

        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == mList.size - 1) {
                    loadLastScreen()
                    btngetStarted.setOnClickListener {

                        val registrasiDataDiriFragment: RegistrasiDataDiriFragment = RegistrasiDataDiriFragment()

                        fragmentManager?.beginTransaction()
                            ?.replace(R.id.frame_content, registrasiDataDiriFragment)
                            ?.addToBackStack(null)
                            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            ?.commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }


}
