package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.databinding.CardEducationBinding
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.utilities.showKeyboard

class EducationAdapter(val onSaveButtonClick: (Education) -> Unit,
                       val onDeleteButtonClick: (Education) -> Unit,
                       val onEditButtonClick: (Education) -> Unit) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    private var educationList: List<Education> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): EducationViewHolder {
        val binding = CardEducationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EducationViewHolder(binding)
    }

    override fun getItemCount(): Int = educationList.size

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val education = educationList[position]
        holder.apply {
            binding.education = education
            bindClick(education)
        }
    }

    inner class EducationViewHolder(val binding: CardEducationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindClick(education: Education) {
            binding.educationSaveButton.setOnClickListener {
                if (education.saved.get()) {
                    onEditButtonClick(education)
                    binding.educationInstituteNameWrapper.apply {
                        requestFocus()
                        showKeyboard(binding.root.context)
                    }
                } else {
                    with(binding) {
                        val tempInstituteName = educationInstituteName.text?.toString() ?: ""
                        val tempDegree = educationDegree.text?.toString() ?: ""
                        val tempPerformance = educationPerformance.text?.toString() ?: ""
                        val tempYear = educationYear.text?.toString() ?: ""

                        var passed = true

                        if (tempInstituteName.trim().isEmpty()) {
                            educationInstituteNameWrapper.error = "Please enter an institute name"
                            passed = false
                        } else {
                            educationInstituteNameWrapper.isErrorEnabled = false
                        }
                        if (tempDegree.trim().isEmpty()) {
                            educationDegreeWrapper.error = "Can't be empty"
                            passed = false
                        } else {
                            educationDegreeWrapper.isErrorEnabled = false
                        }
                        if (tempPerformance.trim().isEmpty()) {
                            educationPerformanceWrapper.error = "Can't be empty"
                            passed = false
                        } else {
                            educationPerformanceWrapper.isErrorEnabled = false
                        }
                        if (tempYear.trim().toLongOrNull() == null) {
                            educationYearWrapper.error = "Year must contain numbers only"
                            passed = false
                        } else if (tempYear.trim().isEmpty()) {
                            educationYearWrapper.error = "Please enter the graduation year"
                            passed = false
                        } else {
                            educationYearWrapper.isErrorEnabled = false
                        }

                        if (passed) {
                            education.instituteName = tempInstituteName.trim()
                            education.degree = tempDegree.trim()
                            education.performance = tempPerformance.trim()
                            education.year = tempYear.trim()
                            onSaveButtonClick(education)
                        }
                    }
                }
            }

            binding.educationDeleteButton.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(itemView.context, R.style.MyAlertDialog))
                        .setMessage("Are you sure you want to delete this education card?")
                        .setPositiveButton("Yes") { _, _ ->
                            onDeleteButtonClick(education)
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