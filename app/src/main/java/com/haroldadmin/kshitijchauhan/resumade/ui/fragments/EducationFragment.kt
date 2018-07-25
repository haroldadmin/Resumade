package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.EducationAdapter
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_education.*

class EducationFragment : Fragment(), SaveButtonClickListener, DeleteButtonClickListener {

	private lateinit var educationAdapter: EducationAdapter
	private lateinit var linearLayoutManager : LinearLayoutManager
	private lateinit var createResumeViewModel : CreateResumeViewModel
	private lateinit var educationRecyclerView : RecyclerView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_education, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		createResumeViewModel = ViewModelProviders
				.of(activity!!)
				.get(CreateResumeViewModel::class.java)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		educationAdapter = EducationAdapter(this, this)
		linearLayoutManager = LinearLayoutManager(context)
		educationRecyclerView = view.findViewById(R.id.educationRecyclerView)

		educationRecyclerView.apply {
			adapter = educationAdapter
			layoutManager = linearLayoutManager
		}
	}

	override fun onStart() {
		super.onStart()
		createResumeViewModel.educationList
				.observe(this, Observer {
					educationAdapter.setEducationList(it ?: emptyList())
					toggleNoEducationLayout(it?.size ?: 0)
				})
	}

	override fun <Education> onSaveButtonClick(item: Education) {
		createResumeViewModel.updateEducation(item as com.haroldadmin.kshitijchauhan.resumade.repository.database.Education)
	}

	override fun <Education> onDeleteButtonClick(item: Education) {
		createResumeViewModel.deleteEducation(item as com.haroldadmin.kshitijchauhan.resumade.repository.database.Education)
	}

	private fun toggleNoEducationLayout(size : Int) {
		if (size > 0) {
			educationRecyclerView.visibility = View.VISIBLE
			noEducationTextView.visibility = View.INVISIBLE
		} else {
			educationRecyclerView.visibility = View.INVISIBLE
			noEducationTextView.visibility = View.VISIBLE
		}
	}
}