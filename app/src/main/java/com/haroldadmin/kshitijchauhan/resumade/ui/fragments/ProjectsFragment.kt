package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ProjectAdapter
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.areAllItemsSaved
import com.haroldadmin.kshitijchauhan.resumade.utilities.invisible
import com.haroldadmin.kshitijchauhan.resumade.utilities.visible
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : Fragment() {

	private val TAG : String = this::class.java.simpleName
	private lateinit var projectAdapter: ProjectAdapter
	private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var projectRecyclerView : androidx.recyclerview.widget.RecyclerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		projectAdapter = ProjectAdapter(
				{ item: Project ->
					item.saved.set(true)
					createResumeViewModel.apply {
						updateProject(item)
						projectDetailsSaved = true
					}
				},
				{ item: Project ->
					createResumeViewModel.deleteProject(item)
					(activity as CreateResumeActivity).displaySnackbar("Project deleted.")
				},
				{ item: Project ->
					item.saved.set(false)
					createResumeViewModel.apply {
						updateProject(item)
						projectDetailsSaved = false
					}
				})
		linearLayoutManager = LinearLayoutManager(context)
	}

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
		projectRecyclerView = view.findViewById(R.id.projectsRecyclerView)
		projectRecyclerView.apply {
			adapter = projectAdapter
			layoutManager = linearLayoutManager
		}
	}

	private fun toggleNoProjectsLayout(size : Int) {
		if (size > 0) {
			projectRecyclerView.visible()
			noProjectsView.invisible()
		} else {
			projectRecyclerView.invisible()
			noProjectsView.visible()
		}
	}
}