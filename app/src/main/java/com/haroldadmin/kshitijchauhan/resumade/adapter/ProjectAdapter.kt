package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener

class ProjectAdapter(val saveButtonClickListener: SaveButtonClickListener,
                     val deleteButtonClickListener: DeleteButtonClickListener) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

	private var projectList: List<Project> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_project, parent, false))
	}

	override fun getItemCount(): Int = projectList.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val project = projectList[position]
		holder.apply {
			projectName.setText(project.projectName)
			projectRole.setText(project.role)
			projectLink.setText(project.link)
			projectDescription.setText(project.description)
			bindClick(project)
		}
	}

	fun setItems(items: List<Project>) {
		projectList = items
		notifyDataSetChanged()
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val projectNameWrapper: TextInputLayout = itemView.findViewById(R.id.projectNameWrapper)
		val projectName: TextInputEditText = itemView.findViewById(R.id.projectName)
		val projectRoleWrapper: TextInputLayout = itemView.findViewById(R.id.projectRoleWrapper)
		val projectRole: TextInputEditText = itemView.findViewById(R.id.projectRole)
		val projectLink: TextInputEditText = itemView.findViewById(R.id.projectLink)
		val projectDescriptionWrapper: TextInputLayout = itemView.findViewById(R.id.projectDescriptionWrapper)
		val projectDescription: TextInputEditText = itemView.findViewById(R.id.projectDescription)
		val saveButton: MaterialButton = itemView.findViewById(R.id.projectSaveButton)
		val deleteButton: MaterialButton = itemView.findViewById(R.id.projectDeleteButton)

		fun bindClick(project: Project) {
			saveButton.apply {
				setOnClickListener {
					val projectName = this@ViewHolder.projectName.text?.toString() ?: ""
					val projectRole = this@ViewHolder.projectRole.text?.toString() ?: ""
					val projectLink = this@ViewHolder.projectLink.text?.toString() ?: ""
					val projectDescription = this@ViewHolder.projectDescription.text?.toString() ?: ""

					var passed = true

					if (projectName.trim().isEmpty()) {
						projectNameWrapper.error = "Please enter the project name"
						passed = false
					} else {
						projectNameWrapper.isErrorEnabled = false
					}
					if (projectRole.trim().isEmpty()) {
						projectRoleWrapper.error = "Please enter your role in the project"
						passed = false
					} else {
						projectRoleWrapper.isErrorEnabled = false
					}
					if (projectDescription.trim().isEmpty()) {
						projectDescriptionWrapper.error = "Please provide a short description of the project"
						passed = false
					} else {
						projectDescriptionWrapper.isErrorEnabled = false
					}

					if (passed) {
						project.projectName = projectName
						project.role = projectRole
						project.link = projectLink
						project.description = projectDescription
						saveButtonClickListener.onSaveButtonClick(project)
						isEnabled = false
						text = "Saved"
					}
				}
			}
			deleteButton.setOnClickListener {
				deleteButtonClickListener.onDeleteButtonClick(project)
			}

		}
	}

	fun updateProjectList(newProjectList : List<Project>) {
		val projectDiffUtilCallback = DiffUtilCallback(this.projectList, newProjectList)
		val diffResult = DiffUtil.calculateDiff(projectDiffUtilCallback)
		this.projectList = newProjectList
		diffResult.dispatchUpdatesTo(this)
	}
}