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
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_experience.*

class ExperienceFragment : Fragment(), SaveButtonClickListener, DeleteButtonClickListener {

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
		experienceAdapter = ExperienceAdapter(this, this)
		experienceRecyclerView.apply {
			adapter = experienceAdapter
			layoutManager = LinearLayoutManager(context)
		}
	}

	override fun onStart() {
		super.onStart()
		createResumeViewModel.experienceList
				.observe(this, Observer {
					experienceAdapter.updateExperienceList(it ?: emptyList())
					if (it == null || it.isEmpty()) { createResumeViewModel.experienceDetailsSaved = true }
					toggleNoExperienceLayout(it?.size ?: 0)
				})
	}

	override fun <Experience> onSaveButtonClick(item: Experience) {
		createResumeViewModel.apply {
			updateExperience(item as com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience)
			experienceDetailsSaved = true
		}
	}

	override fun <Experience> onDeleteButtonClick(item: Experience) {
		createResumeViewModel.deleteExperience(item as com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience)
		(activity as CreateResumeActivity).displaySnackbar("Experience deleted.")
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
