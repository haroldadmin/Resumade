package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputEditText
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

	private var educationList : List<Education> = emptyList()

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
		val instituteName : TextInputEditText = itemView.findViewById(R.id.educationInstituteName)
		val degree : TextInputEditText = itemView.findViewById(R.id.educationDegree)
		val performance : TextInputEditText = itemView.findViewById(R.id.educationPerformance)
		val yearOfGraduation : TextInputEditText = itemView.findViewById(R.id.educationYear)
		val saveButton : MaterialButton = itemView.findViewById(R.id.educationSaveButton)
		val deleteButton : MaterialButton = itemView.findViewById(R.id.educationDeleteButton)

		fun bindClick(education : Education) {
			saveButton.apply {
				setOnClickListener {
					education.instituteName = this@ViewHolder.instituteName.text?.toString() ?: ""
					education.degree = this@ViewHolder.degree.text?.toString() ?: ""
					education.performance = this@ViewHolder.performance.text?.toString() ?: ""
					education.year = this@ViewHolder.yearOfGraduation.text?.toString() ?: ""
					saveButtonClickListener.onSaveButtonClick(education)
					isEnabled = false
					text = "Saved"
				}
			}
			deleteButton.setOnClickListener {
				deleteButtonClickListener.onDeleteButtonClick(education)
			}
		}
	}

	fun setEducationList(items : List<Education>) {
		educationList = items
		notifyDataSetChanged()
	}

}