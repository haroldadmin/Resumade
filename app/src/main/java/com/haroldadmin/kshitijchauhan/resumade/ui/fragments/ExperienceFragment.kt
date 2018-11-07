package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.haroldadmin.kshitijchauhan.resumade.utilities.areAllItemsSaved
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_experience.*

class ExperienceFragment : androidx.fragment.app.Fragment(), SaveButtonClickListener, DeleteButtonClickListener, EditButtonClickListener {

	private lateinit var experienceAdapter : ExperienceAdapter
	private lateinit var createResumeViewModel : CreateResumeViewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_experience, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let {
			createResumeViewModel = ViewModelProviders
					.of(it)
					.get(CreateResumeViewModel::class.java)
		}

		createResumeViewModel.experienceList
				.observe(viewLifecycleOwner, Observer {
					experienceAdapter.updateExperienceList(it ?: emptyList())
					createResumeViewModel.experienceDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
					toggleNoExperienceLayout(it?.size ?: 0)
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		experienceAdapter = ExperienceAdapter(this, this, this)
		experienceRecyclerView.apply {
			adapter = experienceAdapter
			layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
		}
	}

	override fun <T : ResumeEntity> onSaveButtonClick(item: T) {
		item.saved = true
		createResumeViewModel.apply {
			updateExperience(item as Experience)
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
