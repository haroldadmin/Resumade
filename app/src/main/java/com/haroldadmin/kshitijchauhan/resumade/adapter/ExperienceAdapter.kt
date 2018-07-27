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
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener

class ExperienceAdapter(val saveButtonClickListener: SaveButtonClickListener,
                        val deleteButtonClickListener: DeleteButtonClickListener) : RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {

	private var experienceList: List<Experience> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_experience, parent, false))
	}

	override fun getItemCount(): Int = experienceList.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val experience = experienceList[position]
		holder.apply {
			companyName.setText(experience.companyName)
			jobTitle.setText(experience.jobTitle)
			duration.setText(experience.duration)
			bindClick(experience)
		}
	}

	fun setItems(items: List<Experience>) {
		experienceList = items
		notifyDataSetChanged()
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val companyNameWrapper: TextInputLayout = itemView.findViewById(R.id.experienceCompanyNameWrapper)
		val companyName: TextInputEditText = itemView.findViewById(R.id.experienceCompanyName)
		val jobTitleWrapper: TextInputLayout = itemView.findViewById(R.id.experienceJobTitleWrapper)
		val jobTitle: TextInputEditText = itemView.findViewById(R.id.experienceJobTitle)
		val durationWrapper: TextInputLayout = itemView.findViewById(R.id.experienceDurationWrapper)
		val duration: TextInputEditText = itemView.findViewById(R.id.experienceDuration)
		val saveButton: MaterialButton = itemView.findViewById(R.id.experienceSaveButton)
		val deleteButton: MaterialButton = itemView.findViewById(R.id.experienceDeleteButton)

		fun bindClick(experience: Experience) {
			saveButton.apply {
				setOnClickListener {
					val companyName = this@ViewHolder.companyName.text?.toString() ?: ""
					val duration = this@ViewHolder.duration.text?.toString() ?: ""
					val jobTitle = this@ViewHolder.jobTitle.text?.toString() ?: ""

					var passed = true
					if (companyName.trim().isEmpty()) {
						companyNameWrapper.error = "Please enter the company name"
						passed = false
					} else {
						companyNameWrapper.isErrorEnabled = false
					}
					if (jobTitle.trim().isEmpty()) {
						jobTitleWrapper.error = "Please enter your job title"
						passed = false
					} else {
						jobTitleWrapper.isErrorEnabled = false
					}
					if (duration.trim().isEmpty()) {
						durationWrapper.error = "Please enter the duration of your job"
						passed = false
					} else {
						durationWrapper.isErrorEnabled = false
					}

					if (passed) {
						experience.companyName = companyName
						experience.duration = duration
						experience.jobTitle = jobTitle
						saveButtonClickListener.onSaveButtonClick(experience)
						isEnabled = false
						text = "Saved"
					}
				}
			}
			deleteButton.apply {
				setOnClickListener {
					deleteButtonClickListener.onDeleteButtonClick(experience)
				}
			}
		}
	}

	fun updateExperienceList(newExperienceList : List<Experience>) {
		val experienceDiffUtilCallback = DiffUtilCallback(this.experienceList, newExperienceList)
		val diffResult = DiffUtil.calculateDiff(experienceDiffUtilCallback)
		this.experienceList = newExperienceList
		diffResult.dispatchUpdatesTo(this)
	}
}