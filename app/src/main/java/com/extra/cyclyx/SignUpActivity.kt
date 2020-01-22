package com.extra.cyclyx

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.extra.cyclyx.ui.pengenalan.gambaran.PageAdapter
import com.extra.cyclyx.utils.IntroItem
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.collections.ArrayList

class SignUpActivity : AppCompatActivity(){


    lateinit var screenPager: ViewPager

    lateinit var introViewPagerAdapter: PageAdapter

    lateinit var tabIndicator: TabLayout

    lateinit var btnSkip: Button

    lateinit var btngetStarted: Button

    lateinit var linearLayoutSkip: LinearLayout

    lateinit var linearLayoutGetStarted: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        setContentView(R.layout.activity_sign_up)

        btnSkip = findViewById(R.id.btn_skip)
        btngetStarted = findViewById(R.id.btn_get_started)

        linearLayoutSkip = findViewById(R.id.linear_layout_skip)
        linearLayoutGetStarted = findViewById(R.id.linear_layout_get_started)

        tabIndicator = findViewById(R.id.tab_indicator)


        // Fill data description
        val mList: ArrayList<IntroItem> = ArrayList<IntroItem>()

        mList.add(IntroItem("","Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book", R.drawable.mountain_1))

        mList.add(IntroItem("", "Lorem ipsum, or lipsum as it is sometimes known, is dummy text used in laying out print, graphic or web designs. The passage is attributed to an unknown typesetter in the 15th century who is thought to have scrambled parts of Cicero's De Finibus Bonorum et Malorum for use in a type specimen book", R.drawable.mountain_2))

        mList.add(IntroItem("Tunggu apa lagi..", "Ayo daftar !", R.drawable.mountain_3))

        //Setup View Pager
        screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = PageAdapter(this, mList)
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
}



    class RandomStringGenerator{
        companion object {
            private val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
        }

        fun getRandomString(sizeOfRandomString: Int): String {
            val random = Random()
            val sb = StringBuilder(sizeOfRandomString)
            for (i in 0 until sizeOfRandomString)
                sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
            return sb.toString()
        }
    }



