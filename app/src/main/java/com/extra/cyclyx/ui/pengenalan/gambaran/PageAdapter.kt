package com.extra.cyclyx.ui.pengenalan.gambaran

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.extra.cyclyx.R
import com.extra.cyclyx.utils.IntroItem

class PageAdapter: PagerAdapter() {

    lateinit var mContext: Context
    lateinit var mListScreen: List<IntroItem>

    fun IntroViewPagerAdapter(mContext: Context, mListScreen: List<IntroItem>) {
        this.mContext = mContext
        this.mListScreen = mListScreen
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layoutScreen: View = inflater.inflate(R.layout.intro_screen, null)

        var imgSlide: ImageView = layoutScreen.findViewById(R.id.intro_img)
        var title: TextView = layoutScreen.findViewById(R.id.intro_title)
        var description: TextView = layoutScreen.findViewById(R.id.intro_description)

        title.setText(mListScreen[position].getTitle())
        description.setText(mListScreen[position].getDescription())
        imgSlide.setImageResource(mListScreen[position].getScreenImg())

        container.addView(layoutScreen)

        return layoutScreen
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return mListScreen.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


}