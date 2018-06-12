package com.haroldadmin.kshitijchauhan.resuminator.adapters

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity.Companion.resumeId
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Education
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import com.haroldadmin.kshitijchauhan.resuminator.utilities.afterTextChanged
import com.haroldadmin.kshitijchauhan.resuminator.utilities.isEmpty
import kotlinx.android.synthetic.main.education_card.view.*
import kotlinx.coroutines.experimental.launch
import kotlin.text.Typography.degree

class EducationAdapter(val educationList : MutableList<Education>?, val context : Context?) : RecyclerView.Adapter<EducationAdapter.ViewHolder>() {

    lateinit var education: Education
    val database = ResumeDatabase.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.education_card, parent, false))
    }

    override fun getItemCount() = educationList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        education = educationList?.get(position) ?: Education(1,"","","", "", CreateResumeActivity.resumeId)
        if (education.saved) {
            with(holder) {
                saveButton.isEnabled = false
                saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
            }
        } else {
            holder.saveButton.setOnClickListener {
                //Assume that all details are filled already, therefore the tests will pass
                var passed = true
                //If any of the following tests fail, the education details will not be saved.
                if (holder.instituteName.isEmpty()) { passed = false; holder.instituteNameWrapper.error = "Can't be empty" }
                if (holder.degreeName.isEmpty()) { passed = false; holder.degreeNameWrapper.error = "Can't be empty" }
                if (holder.performance.isEmpty()) { passed = false; holder.performanceWrapper.error = "Can't be empty" }
                if (holder.yearOfGraduation.isEmpty()) { passed = false; holder.yearOfGraduationWrapper.error = "Can't be empty" }

                //Clear the errors when the user starts typing again
                holder.instituteName.afterTextChanged { holder.instituteNameWrapper.error = null }
                holder.degreeName.afterTextChanged { holder.degreeNameWrapper.error = null }
                holder.performance.afterTextChanged { holder.performanceWrapper.error = null }
                holder.yearOfGraduation.afterTextChanged { holder.yearOfGraduationWrapper.error = null }

                //If all tests passed, save the details
                if(passed) {
                    with(education) {
                        instituteName = holder.instituteName.text.toString()
                        degree = holder.degreeName.text.toString()
                        performance = holder.performance.text.toString()
                        year = holder.yearOfGraduation.text.toString()
                        resumeId = CreateResumeActivity.resumeId
                        println(this)
                        insertEducationIntoDatabase(this)
                        holder.saveButton.isEnabled = false
                        holder.saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
                    }
                }
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var instituteName: TextInputEditText = view.educationInstituteNameEditText
        var degreeName: TextInputEditText = view.educationDegreeEditText
        var performance: TextInputEditText = view.educationPerformanceEditText
        var yearOfGraduation: TextInputEditText = view.educationGraduationYearEditText
        var saveButton: Button = view.educationSaveButton
        var instituteNameWrapper : TextInputLayout = view.instituteNameWrapper
        var degreeNameWrapper : TextInputLayout = view.educationDegreeWrapper
        var performanceWrapper : TextInputLayout = view.educationPerformanceWrapper
        var yearOfGraduationWrapper : TextInputLayout = view.educationYearWrapper
    }

    fun insertEducationIntoDatabase(education : Education) {
        launch {
            education.id = database?.educationDAO()?.getEducationId()
            database?.educationDAO()?.insertEducation(education)
            println("Successfully inserted Education to database: $education at position ${education.id} with ResumeId ${education.resumeId}")
        }
    }
}