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
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.utilities.DeleteButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener

class EducationAdapter(val saveButtonClickListener: SaveButtonClickListener,
                       val deleteButtonClickListener: DeleteButtonClickListener) : RecyclerView.Adapter<EducationAdapter.ViewHolder>() {

	private var educationList: List<Education> = emptyList()

	override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_education, parent, false))
	}

	override fun getItemCount(): Int = educationList.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val education = educationList[position]
		holder.apply {
			instituteName.setText(education.instituteName)
			degree.setText(education.degree)
			performance.setText(education.performance)
			yearOfGraduation.setText(education.year)
			bindClick(education)
		}
	}

	inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val instituteNameWrapper: TextInputLayout = itemView.findViewById(R.id.educationInstituteNameWrapper)
		val instituteName: TextInputEditText = itemView.findViewById(R.id.educationInstituteName)
		val degreeWrapper: TextInputLayout = itemView.findViewById(R.id.educationDegreeWrapper)
		val degree: TextInputEditText = itemView.findViewById(R.id.educationDegree)
		val performanceWrapper: TextInputLayout = itemView.findViewById(R.id.educationPerformanceWrapper)
		val performance: TextInputEditText = itemView.findViewById(R.id.educationPerformance)
		val yearWrapper: TextInputLayout = itemView.findViewById(R.id.educationYearWrapper)
		val yearOfGraduation: TextInputEditText = itemView.findViewById(R.id.educationYear)
		val saveButton: MaterialButton = itemView.findViewById(R.id.educationSaveButton)
		val deleteButton: MaterialButton = itemView.findViewById(R.id.educationDeleteButton)

		fun bindClick(education: Education) {
			saveButton.apply {
				setOnClickListener {
					val instituteName = this@ViewHolder.instituteName.text?.toString() ?: ""
					val degree = this@ViewHolder.degree.text?.toString() ?: ""
					val performance = this@ViewHolder.performance.text?.toString() ?: ""
					val year = this@ViewHolder.yearOfGraduation.text?.toString() ?: ""

					var passed = true

					if (instituteName.trim().isEmpty()) {
						instituteNameWrapper.error = "Please enter an institute name"
						passed = false
					} else {
						instituteNameWrapper.isErrorEnabled = false
					}
					if (degree.trim().isEmpty()) {
						degreeWrapper.error = "Can't be empty"
						passed = false
					} else {
						degreeWrapper.isErrorEnabled = false
					}
					if (performance.trim().isEmpty()) {
						performanceWrapper.error = "Can't be empty"
						passed = false
					} else {
						performanceWrapper.isErrorEnabled = false
					}
					if (year.trim().toLongOrNull() == null) {
						yearWrapper.error = "Year must contain numbers only"
						passed = false
					} else if (year.trim().isEmpty()) {
						yearWrapper.error = "Please enter the graduation year"
						passed = false
					} else {
						yearWrapper.isErrorEnabled = false
					}

					if (passed) {
						education.instituteName = instituteName
						education.degree = degree
						education.performance = performance
						education.year = year
						saveButtonClickListener.onSaveButtonClick(education)
						isEnabled = false
						text = context.getString(R.string.saveButtonSaved)
					}
				}
			}
			deleteButton.setOnClickListener {
				deleteButtonClickListener.onDeleteButtonClick(education)
			}
		}
	}

	fun setEducationList(items: List<Education>) {
		educationList = items
		notifyDataSetChanged()
	}

	fun updateEducationList(newEducationList : List<Education>) {
		val educationDiffUtilCallback = DiffUtilCallback(this.educationList, newEducationList)
		val diffResult = DiffUtil.calculateDiff(educationDiffUtilCallback)
		educationList = newEducationList
		diffResult.dispatchUpdatesTo(this)
	}

}