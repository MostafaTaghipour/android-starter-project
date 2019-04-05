package ir.rainyday.android.common.components

/**
 * Created by taghipour on 14/10/2017.
 */

import java.util.*

internal class FragmentPagerAdapter(fragmentManager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = ArrayList<androidx.fragment.app.Fragment>()

    private val titles = ArrayList<String>()

    fun addFragment(fragment: androidx.fragment.app.Fragment, title: String? = null) {
        fragments.add(fragment)
        title?.let { titles.add(it) }
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

}