package com.haroldadmin.kshitijchauhan.resuminator.adapters

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Education
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import kotlinx.android.synthetic.main.education_card.view.*
import kotlinx.coroutines.experimental.launch

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
                with(education) {
                    instituteName = holder.instituteName.text.toString()
                    degree = holder.degreeName.text.toString()
                    performance = holder.performance.text.toString()
                    year = holder.yearOfGraduation.text.toString()
                    resumeId = CreateResumeActivity.resumeId
                    println(this)
                }
                insertEducationIntoDatabase(education)
                holder.saveButton.isEnabled = false
                holder.saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var instituteName: TextInputEditText = view.educationInstituteNameEditText
        var degreeName: TextInputEditText = view.educationDegreeEditText
        var performance: TextInputEditText = view.educationPerformanceEditText
        var yearOfGraduation: TextInputEditText = view.educationGraduationYearEditText
        var saveButton: Button = view.educationSaveButton
    }

    fun insertEducationIntoDatabase(education : Education) {
        launch {
            education.id = database?.educationDAO()?.getEducationId()
            database?.educationDAO()?.insertEducation(education)
            println("Successfully inserted Education to database: $education at position ${education.id} with ResumeId ${education.resumeId}")
        }
    }
}