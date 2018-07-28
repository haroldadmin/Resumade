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
import com.haroldadmin.kshitijchauhan.resumade.utilities.EditButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.showKeyboard
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class ProjectAdapter(val saveButtonClickListener: SaveButtonClickListener,
                     val deleteButtonClickListener: DeleteButtonClickListener,
                     val editButtonClickListener: EditButtonClickListener) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

	private var projectList: List<Project> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): ProjectViewHolder {
		return ProjectViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_project, parent, false))
	}

	override fun getItemCount(): Int = projectList.size

	override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
		val project = projectList[position]
		holder.apply {
			setItem(project)
			bindClick()
		}
	}

	inner class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private lateinit var mProject: Project

		val projectNameWrapper: TextInputLayout = itemView.findViewById(R.id.projectNameWrapper)
		val projectName: TextInputEditText = itemView.findViewById(R.id.projectName)
		val projectRoleWrapper: TextInputLayout = itemView.findViewById(R.id.projectRoleWrapper)
		val projectRole: TextInputEditText = itemView.findViewById(R.id.projectRole)
		val projectLinkWrapper: TextInputLayout = itemView.findViewById(R.id.projectLinkWrapper)
		val projectLink: TextInputEditText = itemView.findViewById(R.id.projectLink)
		val projectDescriptionWrapper: TextInputLayout = itemView.findViewById(R.id.projectDescriptionWrapper)
		val projectDescription: TextInputEditText = itemView.findViewById(R.id.projectDescription)
		val saveButton: MaterialButton = itemView.findViewById(R.id.projectSaveButton)
		val deleteButton: MaterialButton = itemView.findViewById(R.id.projectDeleteButton)
		val editButton: MaterialButton = itemView.findViewById(R.id.projectEditButton)

		fun setItem(project: Project) {
			mProject = project
			this.apply {
				projectName.setText(mProject.projectName)
				projectRole.setText(mProject.role)
				projectLink.setText(mProject.link)
				projectDescription.setText(mProject.description)
				saveButton.apply {
					if (mProject.saved) {
						isEnabled = false
						text = "Saved"
					} else {
						isEnabled = true
						text = "Save"
					}
				}
				editButton.isEnabled = mProject.saved

				projectNameWrapper.isEnabled = !mProject.saved
				projectRoleWrapper.isEnabled = !mProject.saved
				projectLinkWrapper.isEnabled = !mProject.saved
				projectDescriptionWrapper.isEnabled = !mProject.saved
			}
		}

		fun bindClick() {
			saveButton.apply {
				setOnClickListener {
					val projectName = this@ProjectViewHolder.projectName.text?.toString() ?: ""
					val projectRole = this@ProjectViewHolder.projectRole.text?.toString() ?: ""
					val projectLink = this@ProjectViewHolder.projectLink.text?.toString() ?: ""
					val projectDescription = this@ProjectViewHolder.projectDescription.text?.toString()
							?: ""

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
						// Save the new values into the member variable
						mProject.projectName = projectName
						mProject.role = projectRole
						mProject.link = projectLink
						mProject.description = projectDescription
						saveButtonClickListener.onSaveButtonClick(mProject)

						// Enable edit button and disable save button
						isEnabled = false
						text = "Saved"
						editButton.isEnabled = true

						// Disable text fields
						projectNameWrapper.isEnabled = false
						projectRoleWrapper.isEnabled = false
						projectLinkWrapper.isEnabled = false
						projectDescriptionWrapper.isEnabled = false
					}
				}
			}
			deleteButton.setOnClickListener {
				/*
				I love anko-dialogs.
				 */
				itemView.context.alert(Appcompat, "Are you sure you want to delete this project card?") {
					yesButton {
						deleteButtonClickListener.onDeleteButtonClick(mProject)
					}
					noButton { /* Do Nothing */ }
				}.show()
			}

			editButton.setOnClickListener {
				editButtonClickListener.onEditButtonClicked(mProject)

				// Enable text fields
				projectNameWrapper.apply {
					isEnabled = true
					requestFocus()
					showKeyboard(itemView.context)
				}
				projectRoleWrapper.isEnabled = true
				projectLinkWrapper.isEnabled = true
				projectDescriptionWrapper.isEnabled = true

				it.isEnabled = false
				saveButton.isEnabled = true
				saveButton.text = "Save"
			}

		}
	}

	fun updateProjectList(newProjectList: List<Project>) {
		val projectDiffUtilCallback = DiffUtilCallback(this.projectList, newProjectList)
		val diffResult = DiffUtil.calculateDiff(projectDiffUtilCallback)
		this.projectList = newProjectList
		diffResult.dispatchUpdatesTo(this)
	}
}