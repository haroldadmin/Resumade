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
import com.haroldadmin.kshitijchauhan.resumade.utilities.EditButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.showKeyboard
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class ExperienceAdapter(val saveButtonClickListener: SaveButtonClickListener,
                        val deleteButtonClickListener: DeleteButtonClickListener,
                        val editButtonClickListener: EditButtonClickListener) : RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder>() {

	private var experienceList: List<Experience> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): ExperienceViewHolder {
		return ExperienceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_experience, parent, false))
	}

	override fun getItemCount(): Int = experienceList.size

	override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
		val experience = experienceList[position]
		holder.apply {
			setItem(experience)
			bindClick()
		}
	}

	inner class ExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private lateinit var mExperience: Experience

		private val companyNameWrapper: TextInputLayout = itemView.findViewById(R.id.experienceCompanyNameWrapper)
		private val companyName: TextInputEditText = itemView.findViewById(R.id.experienceCompanyName)
		private val jobTitleWrapper: TextInputLayout = itemView.findViewById(R.id.experienceJobTitleWrapper)
		private val jobTitle: TextInputEditText = itemView.findViewById(R.id.experienceJobTitle)
		private val durationWrapper: TextInputLayout = itemView.findViewById(R.id.experienceDurationWrapper)
		private val duration: TextInputEditText = itemView.findViewById(R.id.experienceDuration)
		private val saveButton: MaterialButton = itemView.findViewById(R.id.experienceSaveButton)
		private val deleteButton: MaterialButton = itemView.findViewById(R.id.experienceDeleteButton)
		private val editButton: MaterialButton = itemView.findViewById(R.id.experienceEditButton)

		fun setItem(experience: Experience) {
			mExperience = experience
			this.apply {
				companyName.setText(mExperience.companyName)
				jobTitle.setText(mExperience.jobTitle)
				duration.setText(mExperience.duration)
				saveButton.apply {
					if (mExperience.saved) {
						isEnabled = false
						text = "Saved"
					} else {
						isEnabled = true
						text = "Save"
					}
				}
				companyNameWrapper.isEnabled = !mExperience.saved
				jobTitleWrapper.isEnabled = !mExperience.saved
				durationWrapper.isEnabled = !mExperience.saved
				editButton.isEnabled = mExperience.saved
			}
		}

		fun bindClick() {
			saveButton.apply {
				setOnClickListener {
					val companyName = this@ExperienceViewHolder.companyName.text?.toString() ?: ""
					val duration = this@ExperienceViewHolder.duration.text?.toString() ?: ""
					val jobTitle = this@ExperienceViewHolder.jobTitle.text?.toString() ?: ""

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
						// Save the new values into the member variable
						mExperience.companyName = companyName
						mExperience.duration = duration
						mExperience.jobTitle = jobTitle
						saveButtonClickListener.onSaveButtonClick(mExperience)

						// Enable edit button and disable save button
						isEnabled = false
						text = "Saved"
						editButton.isEnabled = true

						// Disable text fields
						companyNameWrapper.isEnabled = false
						jobTitleWrapper.isEnabled = false
						durationWrapper.isEnabled = false
					}
				}
			}
			deleteButton.setOnClickListener {
				/*
				I love anko-dialogs.
				 */
				itemView.context.alert(Appcompat, "Are you sure you want to delete this experience card?") {
					yesButton {
						deleteButtonClickListener.onDeleteButtonClick(mExperience)
					}
					noButton { /* Do Nothing */ }
				}.show()
			}

			editButton.setOnClickListener {
				editButtonClickListener.onEditButtonClicked(mExperience)

				// Enable text fields
				companyNameWrapper.apply {
					isEnabled = true
					requestFocus()
					showKeyboard(itemView.context)
				}
				jobTitleWrapper.isEnabled = true
				durationWrapper.isEnabled = true

				it.isEnabled = false
				saveButton.isEnabled = true
				saveButton.text = "Save"
			}
		}
	}

fun updateExperienceList(newExperienceList: List<Experience>) {
	val experienceDiffUtilCallback = DiffUtilCallback(this.experienceList, newExperienceList)
	val diffResult = DiffUtil.calculateDiff(experienceDiffUtilCallback)
	this.experienceList = newExperienceList
	diffResult.dispatchUpdatesTo(this)
}
}