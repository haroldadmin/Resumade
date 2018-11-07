package com.haroldadmin.kshitijchauhan.resumade.adapter

import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
                     val editButtonClickListener: EditButtonClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

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

	inner class ProjectViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

		private lateinit var mProject: Project

		private val projectNameWrapper: TextInputLayout = itemView.findViewById(R.id.projectNameWrapper)
		private val projectName: TextInputEditText = itemView.findViewById(R.id.projectName)
		private val projectRoleWrapper: TextInputLayout = itemView.findViewById(R.id.projectRoleWrapper)
		private val projectRole: TextInputEditText = itemView.findViewById(R.id.projectRole)
		private val projectLinkWrapper: TextInputLayout = itemView.findViewById(R.id.projectLinkWrapper)
		private val projectLink: TextInputEditText = itemView.findViewById(R.id.projectLink)
		private val projectDescriptionWrapper: TextInputLayout = itemView.findViewById(R.id.projectDescriptionWrapper)
		private val projectDescription: TextInputEditText = itemView.findViewById(R.id.projectDescription)
		private val saveButton: MaterialButton = itemView.findViewById(R.id.projectSaveButton)
		private val deleteButton: MaterialButton = itemView.findViewById(R.id.projectDeleteButton)

		fun setItem(project: Project) {
			mProject = project
			this.apply {
				projectName.setText(mProject.projectName)
				projectRole.setText(mProject.role)
				projectLink.setText(mProject.link)
				projectDescription.setText(mProject.description)
				saveButton.apply {
					text = if (mProject.saved) {
						this.context.getString(R.string.editButtonText)
					} else {
						this.context.getString(R.string.saveButtonText)
					}
				}
				projectNameWrapper.isEnabled = !mProject.saved
				projectRoleWrapper.isEnabled = !mProject.saved
				projectLinkWrapper.isEnabled = !mProject.saved
				projectDescriptionWrapper.isEnabled = !mProject.saved
			}
		}

		fun bindClick() {
			saveButton.apply {
				setOnClickListener {
					if (mProject.saved) {
						// Edit Mode

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
						this.text = this.context.getString(R.string.saveButtonText)
					} else {
						// Save Mode
						val tempProjectName = this@ProjectViewHolder.projectName.text?.toString()
								?: ""
						val tempProjectRole = this@ProjectViewHolder.projectRole.text?.toString()
								?: ""
						val tempProjectLink = this@ProjectViewHolder.projectLink.text?.toString()
								?: ""
						val tempProjectDescription = this@ProjectViewHolder.projectDescription.text?.toString()
								?: ""

						var passed = true

						if (tempProjectName.trim().isEmpty()) {
							projectNameWrapper.error = "Please enter the project name"
							passed = false
						} else {
							projectNameWrapper.isErrorEnabled = false
						}
						if (tempProjectRole.trim().isEmpty()) {
							projectRoleWrapper.error = "Please enter your role in the project"
							passed = false
						} else {
							projectRoleWrapper.isErrorEnabled = false
						}
						if (tempProjectDescription.trim().isEmpty()) {
							projectDescriptionWrapper.error = "Please provide a short description of the project"
							passed = false
						} else {
							projectDescriptionWrapper.isErrorEnabled = false
						}

						if (passed) {
							// Save the new values into the member variable
							mProject.projectName = tempProjectName
							mProject.role = tempProjectRole
							mProject.link = tempProjectLink
							mProject.description = tempProjectDescription
							saveButtonClickListener.onSaveButtonClick(mProject)

							this.text = this.context.getString(R.string.editButtonText)

							// Disable text fields
							projectNameWrapper.isEnabled = false
							projectRoleWrapper.isEnabled = false
							projectLinkWrapper.isEnabled = false
							projectDescriptionWrapper.isEnabled = false
						}
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
		}
	}

	fun updateProjectList(newProjectList: List<Project>) {
		val projectDiffUtilCallback = DiffUtilCallback(this.projectList, newProjectList)
		val diffResult = DiffUtil.calculateDiff(projectDiffUtilCallback)
		this.projectList = newProjectList
		diffResult.dispatchUpdatesTo(this)
	}
}