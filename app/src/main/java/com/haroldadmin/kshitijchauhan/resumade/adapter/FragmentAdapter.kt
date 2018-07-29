package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.haroldadmin.kshitijchauhan.resumade.ui.fragments.*

class FragmentAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager) {

	private val listOfFragments: List<Fragment> = listOf(PersonalFragment(), EducationFragment(), ExperienceFragment(), ProjectsFragment())
	private val listOfTitles: List<String> = listOf("Personal", "Education", "Experience", "Projects")

	override fun getItem(position: Int): Fragment = listOfFragments[position]

	override fun getCount(): Int = listOfFragments.size

	override fun getPageTitle(position: Int) = listOfTitles[position]

}