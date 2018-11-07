package com.haroldadmin.kshitijchauhan.resumade.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.haroldadmin.kshitijchauhan.resumade.ui.fragments.*

class FragmentAdapter(manager : androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {

	private val listOfFragments: List<androidx.fragment.app.Fragment> = listOf(PersonalFragment(), EducationFragment(), ExperienceFragment(), ProjectsFragment())
	private val listOfTitles: List<String> = listOf("Personal", "Education", "Experience", "Projects")

	override fun getItem(position: Int): androidx.fragment.app.Fragment = listOfFragments[position]

	override fun getCount(): Int = listOfFragments.size

	override fun getPageTitle(position: Int) = listOfTitles[position]

}