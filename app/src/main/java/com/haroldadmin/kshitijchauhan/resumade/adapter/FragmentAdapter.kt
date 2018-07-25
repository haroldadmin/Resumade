package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.haroldadmin.kshitijchauhan.resumade.ui.fragments.EducationFragment
import com.haroldadmin.kshitijchauhan.resumade.ui.fragments.ExperienceFragment
import com.haroldadmin.kshitijchauhan.resumade.ui.fragments.PersonalFragment
import com.haroldadmin.kshitijchauhan.resumade.ui.fragments.ProjectsFragment

class FragmentAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager) {

	private val listOfFragments = listOf<Fragment>(PersonalFragment(), EducationFragment(), ExperienceFragment(), ProjectsFragment())
	private val listOfTitles = listOf<String>("Personal", "Education", "Experience", "Projects")

	override fun getItem(position: Int): Fragment = listOfFragments[position]

	override fun getCount(): Int = listOfFragments.size

	override fun getPageTitle(position: Int) = listOfTitles[position]

}