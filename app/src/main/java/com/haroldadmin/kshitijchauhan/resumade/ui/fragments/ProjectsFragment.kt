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
import com.haroldadmin.kshitijchauhan.resumade.adapter.ProjectAdapter
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : Fragment(), SaveButtonClickListener, DeleteButtonClickListener {

	private lateinit var projectAdapter: ProjectAdapter
	private lateinit var linearLayoutManager: LinearLayoutManager
	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var projectRecyclerView : RecyclerView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_projects, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		createResumeViewModel = ViewModelProviders
				.of(activity!!)
				.get(CreateResumeViewModel::class.java)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		projectAdapter = ProjectAdapter(this, this)
		linearLayoutManager = LinearLayoutManager(context)

		projectRecyclerView = view.findViewById(R.id.projectsRecyclerView)
		projectRecyclerView.apply {
			adapter = projectAdapter
			layoutManager = linearLayoutManager
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
		createResumeViewModel.projectsList
				.observe(this, Observer {
					projectAdapter.updateProjectList(it ?: emptyList())
					if (it == null || it.isEmpty()) { createResumeViewModel.projectDetailsSaved = true }
					toggleNoProjectsLayout(it?.size ?: 0)
				})
	}

	override fun <Project> onSaveButtonClick(item: Project) {
		createResumeViewModel.apply {
			updateProject(item as com.haroldadmin.kshitijchauhan.resumade.repository.database.Project)
			projectDetailsSaved = true
		}
	}

	override fun <Project> onDeleteButtonClick(item: Project) {
		createResumeViewModel.deleteProject(item as com.haroldadmin.kshitijchauhan.resumade.repository.database.Project)
		(activity as CreateResumeActivity).displaySnackbar("Project deleted.")
	}

	private fun toggleNoProjectsLayout(size : Int) {
		if (size > 0) {
			projectRecyclerView.visibility = View.VISIBLE
			noProjectsView.visibility = View.INVISIBLE
		} else {
			projectRecyclerView.visibility = View.INVISIBLE
			noProjectsView.visibility = View.VISIBLE
		}
	}

}