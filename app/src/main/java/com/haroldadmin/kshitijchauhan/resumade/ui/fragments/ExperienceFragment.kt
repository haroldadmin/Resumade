package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ExperienceAdapter
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.EditButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_experience.*

class ExperienceFragment : Fragment(), SaveButtonClickListener, DeleteButtonClickListener, EditButtonClickListener {

	private lateinit var experienceAdapter : ExperienceAdapter
	private lateinit var createResumeViewModel : CreateResumeViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_experience, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		createResumeViewModel = ViewModelProviders
				.of(activity!!)
				.get(CreateResumeViewModel::class.java)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		experienceAdapter = ExperienceAdapter(this, this, this)
		experienceRecyclerView.apply {
			adapter = experienceAdapter
			layoutManager = LinearLayoutManager(context)
		}
	}

	/*
	While observing the list from the viewmodel,
	we check if the list is empty to set saved status
	in the viewmodel. We do this because otherwise
	the user encounters an unsaved message if he adds
	items to the list, and deletes them all before
	saving the resume.
	 */
	override fun onStart() {
		super.onStart()
		createResumeViewModel.experienceList
				.observe(this, Observer {
					experienceAdapter.updateExperienceList(it ?: emptyList())
					if (it == null || it.isEmpty()) { createResumeViewModel.experienceDetailsSaved = true }
					toggleNoExperienceLayout(it?.size ?: 0)
				})
	}

	override fun <T : ResumeEntity> onSaveButtonClick(item: T) {
		item.saved = true
		createResumeViewModel.apply {
			updateExperience(item as Experience)
			experienceDetailsSaved = true
		}
	}

	override fun <T : ResumeEntity> onDeleteButtonClick(item: T) {
		createResumeViewModel.deleteExperience(item as Experience)
		(activity as CreateResumeActivity).displaySnackbar("Experience deleted.")
	}

	override fun <T : ResumeEntity> onEditButtonClicked(item: T) {
		item.saved = false
		createResumeViewModel.apply {
			updateExperience(item as Experience)
			experienceDetailsSaved = false
		}
	}

	private fun toggleNoExperienceLayout(size : Int) {
		if (size > 0) {
			experienceRecyclerView.visibility = View.VISIBLE
			noExperienceView.visibility = View.INVISIBLE
		} else {
			experienceRecyclerView.visibility = View.INVISIBLE
			noExperienceView.visibility = View.VISIBLE
		}
	}
}
