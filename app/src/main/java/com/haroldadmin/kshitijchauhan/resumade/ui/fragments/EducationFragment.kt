package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.EducationAdapter
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.EditButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.areAllItemsSaved
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_education.*

class EducationFragment : androidx.fragment.app.Fragment(), SaveButtonClickListener, DeleteButtonClickListener, EditButtonClickListener {

	private lateinit var educationAdapter: EducationAdapter
	private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var educationRecyclerView: androidx.recyclerview.widget.RecyclerView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_education, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let {
			createResumeViewModel = ViewModelProviders
				.of(it)
				.get(CreateResumeViewModel::class.java)
		}

		createResumeViewModel.educationList
				.observe(viewLifecycleOwner, Observer {
					educationAdapter.updateEducationList(it ?: emptyList())
					createResumeViewModel.educationDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
					toggleNoEducationLayout(it?.size ?: 0)
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		educationAdapter = EducationAdapter(this, this, this)
		linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
		educationRecyclerView = view.findViewById(R.id.educationRecyclerView)

		educationRecyclerView.apply {
			adapter = educationAdapter
			layoutManager = linearLayoutManager
		}
	}

	override fun <T : ResumeEntity> onSaveButtonClick(item: T) {
		item.saved = true
		createResumeViewModel.apply {
			updateEducation(item as Education)
		}
	}

	override fun <T : ResumeEntity> onDeleteButtonClick(item: T) {
		createResumeViewModel.deleteEducation(item as Education)
		(activity as CreateResumeActivity).displaySnackbar("Education deleted.")
	}

	override fun <T : ResumeEntity> onEditButtonClicked(item: T) {
		item.saved = false
		createResumeViewModel.apply {
			updateEducation(item as Education)
		}
	}

	private fun toggleNoEducationLayout(size: Int) {
		if (size > 0) {
			educationRecyclerView.visibility = View.VISIBLE
			noEducationView.visibility = View.INVISIBLE
		} else {
			educationRecyclerView.visibility = View.INVISIBLE
			noEducationView.visibility = View.VISIBLE
		}
	}
}