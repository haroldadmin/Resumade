package com.haroldadmin.kshitijchauhan.resuminator.adapters

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Project
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import kotlinx.android.synthetic.main.project_card.view.*
import kotlinx.coroutines.experimental.launch

class ProjectAdapter(val projectsList : MutableList<Project>?, val context : Context?) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    lateinit var project : Project
    val database = ResumeDatabase.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.project_card, parent, false))
    }

    override fun getItemCount() = projectsList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        project = projectsList?.get(position) ?: Project(1,"","","","", CreateResumeActivity.resumeId)
        if (project.saved) {
            with(holder) {
                saveButton.isEnabled = false
                saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
            }
        } else {
            holder.saveButton.setOnClickListener {
                with(project) {
                    projectName = holder.projectName.text.toString()
                    role = holder.projectRole.text.toString()
                    link = holder.projectLink.text.toString()
                    description = holder.projectDescription.toString()
                    resumeId = CreateResumeActivity.resumeId
                    println(this)
                }
                insertProjectIntoDatabase(project)
                holder.saveButton.isEnabled = false
                holder.saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val projectName : TextInputEditText = view.projectNameEditText
        val projectRole : TextInputEditText = view.projectRoleEditText
        val projectLink : TextInputEditText = view.projectLinkEditText
        val projectDescription : TextInputEditText = view.projectDescriptionEditText
        val saveButton : Button = view.experienceSaveButton
    }

    fun insertProjectIntoDatabase(project : Project) {
        launch {
            project.id = database?.projectsDAO()?.getProjectId()
            database?.projectsDAO()?.insertProject(project)
            println("Successfully inserted Project to database: $project at position ${project.id} with ResumeId ${project.resumeId}")
        }
    }

}