package com.haroldadmin.kshitijchauhan.resuminator.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ResumeFragmentsAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager) {

    private var listOfFragments = mutableListOf<Fragment>()
    private var titlesOfFragments = mutableListOf<String>()

    override fun getItem(position: Int): Fragment {
        return listOfFragments[position]
    }

    override fun getCount(): Int {
        return listOfFragments.size
    }

    fun addFragment(fragment : Fragment, title : String) {
        titlesOfFragments.add(title)
        listOfFragments.add(fragment)
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return titlesOfFragments[position]
    }


}