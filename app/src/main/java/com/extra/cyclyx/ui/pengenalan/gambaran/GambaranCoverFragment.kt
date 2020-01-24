package com.extra.cyclyx.ui.pengenalan.gambaran


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.extra.cyclyx.R
import com.extra.cyclyx.utils.IntroItem
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList

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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengenalan_cover, container, false)

        val view = inflater.inflate(R.layout.fragment_pengenalan_cover, container, false)

        btnSkip = view.findViewById(R.id.btn_skip)
        btngetStarted = view.findViewById(R.id.btn_get_started)

        linearLayoutSkip = view.findViewById(R.id.linear_layout_skip)
        linearLayoutGetStarted = view.findViewById(R.id.linear_layout_get_started)

        tabIndicator = view.findViewById(R.id.tab_indicator)


        // Fill data description
        val mList: ArrayList<IntroItem> = ArrayList<IntroItem>()

        mList.add(IntroItem("","Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book", R.drawable.mountain_1))

        mList.add(IntroItem("", "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book", R.drawable.mountain_2))

        mList.add(IntroItem("Tunggu apa lagi..", "Ayo daftar !", R.drawable.mountain_3))

        //Setup View Pager
        screenPager = view.findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = PageAdapter(activity?.applicationContext, mList)
        screenPager.adapter = introViewPagerAdapter

        //setup tab Indicator
        tabIndicator.setupWithViewPager(screenPager)

        btnSkip.setOnClickListener {
            screenPager.setCurrentItem(screenPager.currentItem+1, true)
        }

        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == mList.size - 1) {
                    loadLastScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun loadLastScreen() {
        linearLayoutSkip.visibility = View.INVISIBLE
        linearLayoutGetStarted.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }


}
