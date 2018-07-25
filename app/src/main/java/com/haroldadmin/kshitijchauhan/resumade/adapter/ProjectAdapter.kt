package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputEditText
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

	private var projectList : List<Project> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position : Int): ViewHolder {
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

	fun setItems(items : List<Project>) {
		projectList = items
		notifyDataSetChanged()
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val projectName : TextInputEditText = itemView.findViewById(R.id.projectName)
		val projectRole : TextInputEditText = itemView.findViewById(R.id.projectRole)
		val projectLink : TextInputEditText = itemView.findViewById(R.id.projectLink)
		val projectDescription : TextInputEditText = itemView.findViewById(R.id.projectDescription)
		val saveButton : MaterialButton = itemView.findViewById(R.id.projectSaveButton)
		val deleteButton : MaterialButton = itemView.findViewById(R.id.projectDeleteButton)

		fun bindClick(project : Project) {
			saveButton.apply {
				setOnClickListener {
					project.projectName = this@ViewHolder.projectName.text?.toString() ?: ""
					project.role = this@ViewHolder.projectRole.text?.toString() ?: ""
					project.link = this@ViewHolder.projectLink.text?.toString() ?: ""
					project.description = this@ViewHolder.projectDescription.text?.toString() ?: ""
					saveButtonClickListener.onSaveButtonClick(project)
					isEnabled = false
					text = "Saved"
				}
			}
			deleteButton.setOnClickListener {
				deleteButtonClickListener.onDeleteButtonClick(project)
			}

		}
	}
}