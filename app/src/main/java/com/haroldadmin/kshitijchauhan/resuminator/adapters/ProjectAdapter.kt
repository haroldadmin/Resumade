package com.haroldadmin.kshitijchauhan.resuminator.adapters

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Project
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import com.haroldadmin.kshitijchauhan.resuminator.utilities.afterTextChanged
import com.haroldadmin.kshitijchauhan.resuminator.utilities.isEmpty
import kotlinx.android.synthetic.main.personal_fragment.view.*
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
                //Assume that all details are filled already, therefore the tests will pass
                var passed = true
                //If any of the following tests fail, the education details will not be saved.
                //Intentionally skipping out on the projectLink check.
                if (holder.projectName.isEmpty()) {
                    passed = false; holder.projectNameWrapper.error = "Can't be empty"
                }
                if (holder.projectRole.isEmpty()) {
                    passed = false; holder.projectRoleWrapper.error = "Can't be empty"
                }
                if (holder.projectDescription.isEmpty()) {
                    passed = false; holder.projectDescriptionWrapper.error = "Can't be empty"
                }
                //Clear errors when the user starts typing again
                holder.projectName.afterTextChanged { holder.projectNameWrapper.error = null }
                holder.projectRole.afterTextChanged { holder.projectRoleWrapper.error = null }
                holder.projectDescription.afterTextChanged { holder.projectDescriptionWrapper.error = null }
                
                //If all tests passed, save the details
                if (passed) {
                    with(project) {
                        projectName = holder.projectName.text.toString()
                        role = holder.projectRole.text.toString()
                        link = holder.projectLink.text.toString()
                        description = holder.projectDescription.text.toString()
                        resumeId = CreateResumeActivity.resumeId
                        println(this)
                        insertProjectIntoDatabase(this)
                        holder.saveButton.isEnabled = false
                        holder.saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
                    }
                }
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val projectName : TextInputEditText = view.projectNameEditText
        val projectRole : TextInputEditText = view.projectRoleEditText
        val projectLink : TextInputEditText = view.projectLinkEditText
        val projectDescription : TextInputEditText = view.projectDescriptionEditText
        val projectNameWrapper : TextInputLayout = view.projectNameWrapper
        val projectRoleWrapper : TextInputLayout = view.projectRoleWrapper
        val projectLinkWrapper : TextInputLayout = view.projectLinkWrapper
        val projectDescriptionWrapper : TextInputLayout = view.projectDescriptionWrapper
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