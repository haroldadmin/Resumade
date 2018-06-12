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
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Experience
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import com.haroldadmin.kshitijchauhan.resuminator.utilities.afterTextChanged
import com.haroldadmin.kshitijchauhan.resuminator.utilities.isEmpty
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
                //Assume that all details are filled already, therefore the tests will pass
                var passed = true
                //If any of the following tests fail, the education details will not be saved.
                if (holder.companyName.isEmpty()) {
                    passed = false; holder.companyNameWrapper.error = "Can't be empty"
                }
                if (holder.duration.isEmpty()) {
                    passed = false; holder.durationWrapper.error = "Can't be empty"
                }
                if (holder.jobTitle.isEmpty()) {
                    passed = false; holder.jobTitleWrapper.error = "Can't be empty"
                }
                //Clear the errors when the user starts typing again
                holder.companyName.afterTextChanged { holder.companyNameWrapper.error = null }
                holder.jobTitle.afterTextChanged { holder.jobTitleWrapper.error = null }
                holder.duration.afterTextChanged { holder.durationWrapper.error = null }

                if (passed) {
                    with(experience) {
                        companyName = holder.companyName.text.toString()
                        jobTitle = holder.jobTitle.text.toString()
                        duration = holder.duration.text.toString()
                        resumeId = CreateResumeActivity.resumeId
                        println(this)
                        insertExperienceIntoDatabase(this)
                        holder.saveButton.isEnabled = false
                        holder.saveButton.text = context?.getString(R.string.saveButtonSaved) ?: "Saved"
                    }
                }
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val companyName : TextInputEditText = view.experienceCompanyNameEditText
        val jobTitle : TextInputEditText = view.experienceJobTitleEditText
        val duration : TextInputEditText = view.experienceDurationEditText
        val saveButton : Button = view.experienceSaveButton
        val companyNameWrapper : TextInputLayout = view.companyNameWrapper
        val jobTitleWrapper : TextInputLayout = view.jobTitleWrapper
        val durationWrapper : TextInputLayout = view.experienceDurationWrapper
    }

    fun insertExperienceIntoDatabase(experience : Experience) {
        launch {
            experience.id = database?.experienceDAO()?.getExperienceId()
            database?.experienceDAO()?.insertExperience(experience)
            println("Successfully inserted Experience to database: $experience at position ${experience.id} with ResumeId ${experience.resumeId}")
        }
    }
}