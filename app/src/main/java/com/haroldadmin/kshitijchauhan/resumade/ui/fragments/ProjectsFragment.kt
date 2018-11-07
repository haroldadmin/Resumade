package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ProjectAdapter
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.EditButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.areAllItemsSaved
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : androidx.fragment.app.Fragment(), SaveButtonClickListener, DeleteButtonClickListener, EditButtonClickListener {

	private val TAG : String = this::class.java.simpleName
	private lateinit var projectAdapter: ProjectAdapter
	private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var projectRecyclerView : androidx.recyclerview.widget.RecyclerView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_projects, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let {
			createResumeViewModel = ViewModelProviders
					.of(it)
					.get(CreateResumeViewModel::class.java)
		}

		createResumeViewModel.projectsList
				.observe(viewLifecycleOwner, Observer {
					projectAdapter.updateProjectList(it ?: emptyList())
					createResumeViewModel.projectDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
					toggleNoProjectsLayout(it?.size ?: 0)
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		projectAdapter = ProjectAdapter(this, this, this)
		linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

		projectRecyclerView = view.findViewById(R.id.projectsRecyclerView)
		projectRecyclerView.apply {
			adapter = projectAdapter
			layoutManager = linearLayoutManager
		}
	}

	override fun <T : ResumeEntity> onSaveButtonClick(item: T) {
		item.saved = true
		createResumeViewModel.apply {
			updateProject(item as Project)
			projectDetailsSaved = true
		}
	}

	override fun <T : ResumeEntity> onDeleteButtonClick(item: T) {
		createResumeViewModel.deleteProject(item as Project)
		(activity as CreateResumeActivity).displaySnackbar("Project deleted.")
	}

	override fun <T : ResumeEntity> onEditButtonClicked(item: T) {
		item.saved = false
		createResumeViewModel.apply {
			updateProject(item as Project)
			projectDetailsSaved = false
		}
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