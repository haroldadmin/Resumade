package com.haroldadmin.kshitijchauhan.resuminator.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.adapters.ProjectAdapter
import com.haroldadmin.kshitijchauhan.resuminator.data.Project
import kotlinx.android.synthetic.main.projects_fragment.*

class ProjectsFragment : Fragment() {

    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.projects_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        projectAdapter = ProjectAdapter(CreateResumeActivity.projectsList, context)
        val animation = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)
        projectsRecyclerView.layoutManager = LinearLayoutManager(context)
        projectsRecyclerView.adapter = projectAdapter
        projectsRecyclerView.layoutAnimation = animation
        addProject(CreateResumeActivity.resumeId)
    }

    fun addProject(resumeId : Long) {
        val position = CreateResumeActivity.projectsList?.size ?: 1
        CreateResumeActivity.projectsList?.add(position, Project(position.toLong(), "", "", "", "", resumeId))
        projectAdapter.notifyItemInserted(position)
    }
}