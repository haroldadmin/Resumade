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
import com.haroldadmin.kshitijchauhan.resuminator.data.Experience
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import kotlinx.android.synthetic.main.experience_card.view.*
import kotlinx.coroutines.experimental.launch

class ExperienceAdapter(val experienceList : MutableList<Experience>?, val context : Context?) : RecyclerView.Adapter<ExperienceAdapter.ViewHolder>() {

    lateinit var experience : Experience
    val database = ResumeDatabase.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.experience_card, parent, false))
    }

    override fun getItemCount() = experienceList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        experience = experienceList?.get(position) ?: Experience(1,"","","", CreateResumeActivity.resumeId)
        if (experience.saved) {
            with(holder) {
                saveButton.isEnabled = false
                saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
            }
        } else {
            holder.saveButton.setOnClickListener {
                with(experience) {
                    companyName = holder.companyName.text.toString()
                    jobTitle = holder.jobTitle.text.toString()
                    duration = holder.duration.text.toString()
                    resumeId = CreateResumeActivity.resumeId
                    println(this)
                }
                insertExperienceIntoDatabase(experience)
                holder.saveButton.isEnabled = false
                holder.saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val companyName : TextInputEditText = view.experienceCompanyNameEditText
        val jobTitle : TextInputEditText = view.experienceJobTitleEditText
        val duration : TextInputEditText = view.experienceDurationEditText
        val saveButton : Button = view.experienceSaveButton
    }

    fun insertExperienceIntoDatabase(experience : Experience) {
        launch {
            experience.id = database?.experienceDAO()?.getExperienceId()
            database?.experienceDAO()?.insertExperience(experience)
            println("Successfully inserted Experience to database: $experience at position ${experience.id} with ResumeId ${experience.resumeId}")
        }
    }
}