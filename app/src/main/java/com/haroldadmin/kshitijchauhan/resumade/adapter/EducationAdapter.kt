package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.EditButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.showKeyboard

class EducationAdapter(val saveButtonClickListener: SaveButtonClickListener,
                       val deleteButtonClickListener: DeleteButtonClickListener,
                       val editButtonClickListener: EditButtonClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

	private var educationList: List<Education> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): EducationViewHolder {
		return EducationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_education, parent, false))
	}

	override fun getItemCount(): Int = educationList.size

	override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
		val education = educationList[position]
		holder.apply {
			setItem(education)
			bindClick()
		}
	}

	inner class EducationViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

		private lateinit var mEducation: Education

		private val instituteNameWrapper: TextInputLayout = itemView.findViewById(R.id.educationInstituteNameWrapper)
		private val instituteName: TextInputEditText = itemView.findViewById(R.id.educationInstituteName)
		private val degreeWrapper: TextInputLayout = itemView.findViewById(R.id.educationDegreeWrapper)
		private val degree: TextInputEditText = itemView.findViewById(R.id.educationDegree)
		private val performanceWrapper: TextInputLayout = itemView.findViewById(R.id.educationPerformanceWrapper)
		private val performance: TextInputEditText = itemView.findViewById(R.id.educationPerformance)
		private val yearWrapper: TextInputLayout = itemView.findViewById(R.id.educationYearWrapper)
		private val yearOfGraduation: TextInputEditText = itemView.findViewById(R.id.educationYear)
		private val saveButton: MaterialButton = itemView.findViewById(R.id.educationSaveButton)
		private val deleteButton: MaterialButton = itemView.findViewById(R.id.educationDeleteButton)

		fun setItem(education: Education) {
			mEducation = education
			this.apply {
				instituteName.setText(mEducation.instituteName)
				degree.setText(mEducation.degree)
				performance.setText(mEducation.performance)
				yearOfGraduation.setText(mEducation.year)
				saveButton.apply {
					this.text = if (mEducation.saved) {
						this.context.getString(R.string.editButtonText)
					} else {
						this.context.getString(R.string.saveButtonText)
					}
				}
				instituteNameWrapper.isEnabled = !mEducation.saved
				degreeWrapper.isEnabled = !mEducation.saved
				performanceWrapper.isEnabled = !mEducation.saved
				yearWrapper.isEnabled = !mEducation.saved
			}
		}

		fun bindClick() {
			saveButton.apply {
				setOnClickListener {
					if (mEducation.saved) {
						// Edit Mode
						editButtonClickListener.onEditButtonClicked(mEducation)
						instituteNameWrapper.apply {
							isEnabled = true
							requestFocus()
							showKeyboard(itemView.context)
						}
						degreeWrapper.isEnabled = true
						performanceWrapper.isEnabled = true
						yearWrapper.isEnabled = true
						this.text = this.context.getString(R.string.saveButtonText)
					} else {
						// Save Mode
						val tempInstituteName = this@EducationViewHolder.instituteName.text?.toString()
								?: ""
						val tempDegree = this@EducationViewHolder.degree.text?.toString() ?: ""
						val tempPerformance = this@EducationViewHolder.performance.text?.toString() ?: ""
						val tempYear = this@EducationViewHolder.yearOfGraduation.text?.toString() ?: ""

						var passed = true

						if (tempInstituteName.trim().isEmpty()) {
							instituteNameWrapper.error = "Please enter an institute name"
							passed = false
						} else {
							instituteNameWrapper.isErrorEnabled = false
						}
						if (tempDegree.trim().isEmpty()) {
							degreeWrapper.error = "Can't be empty"
							passed = false
						} else {
							degreeWrapper.isErrorEnabled = false
						}
						if (tempPerformance.trim().isEmpty()) {
							performanceWrapper.error = "Can't be empty"
							passed = false
						} else {
							performanceWrapper.isErrorEnabled = false
						}
						if (tempYear.trim().toLongOrNull() == null) {
							yearWrapper.error = "Year must contain numbers only"
							passed = false
						} else if (tempYear.trim().isEmpty()) {
							yearWrapper.error = "Please enter the graduation year"
							passed = false
						} else {
							yearWrapper.isErrorEnabled = false
						}

						if (passed) {
							// Save the new values into the member variable
							mEducation.instituteName = tempInstituteName.trim()
							mEducation.degree = tempDegree.trim()
							mEducation.performance = tempPerformance.trim()
							mEducation.year = tempYear.trim()
							saveButtonClickListener.onSaveButtonClick(mEducation)

							text = this.context.getString(R.string.editButtonText)

							// Disable text fields
							instituteNameWrapper.isEnabled = false
							degreeWrapper.isEnabled = false
							performanceWrapper.isEnabled = false
							yearWrapper.isEnabled = false
						}
					}

				}
			}
			deleteButton.setOnClickListener {
				AlertDialog.Builder(ContextThemeWrapper(itemView.context, R.style.MyAlertDialog))
						.setMessage("Are you sure you want to delete this education card?")
						.setPositiveButton("Yes") { _, _ ->
							deleteButtonClickListener.onDeleteButtonClick(mEducation)
						}
						.setNegativeButton("No") { dialog, _ ->
							dialog.dismiss()
						}
						.create()
						.show()
			}
		}
	}

	fun updateEducationList(newEducationList: List<Education>) {
		val educationDiffUtilCallback = DiffUtilCallback(this.educationList, newEducationList)
		val diffResult = DiffUtil.calculateDiff(educationDiffUtilCallback)
		educationList = newEducationList
		diffResult.dispatchUpdatesTo(this)
	}

}