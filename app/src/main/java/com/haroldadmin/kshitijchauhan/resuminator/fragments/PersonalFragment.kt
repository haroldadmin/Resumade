package com.haroldadmin.kshitijchauhan.resuminator.fragments

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Resume
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import com.haroldadmin.kshitijchauhan.resuminator.utilities.afterTextChanged
import kotlinx.android.synthetic.main.personal_fragment.*
import kotlinx.android.synthetic.main.personal_fragment.view.*
import kotlinx.coroutines.experimental.launch

class PersonalFragment : Fragment() {

    lateinit var database: ResumeDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.personal_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        resumeNameEditText.setText(CreateResumeActivity.tempResume.resumeName)

        database = ResumeDatabase.getInstance(activity)!!

        personalSaveButton.setOnClickListener {
            if (runChecks()) {
                with(CreateResumeActivity.tempResume) {
                    resumeName = resumeNameEditText.text.toString().trim()
                    name = personalNameEditText.text.toString().trim()
                    phone = personalPhoneEditText.text.toString().trim()
                    email = personalEmailEditText.text.toString().trim()
                    currentCity = personalCityEditText.text.toString().trim()
                    skills = personalSkillsEditText.text.toString().trim()
                    hobbies = personalHobbiesEditText.text.toString().trim()
                    description = personalDescriptionEditText.text.toString().trim()
                }
                updateResume(CreateResumeActivity.tempResume)
                personalSaveButton.text = resources.getString(R.string.saveButtonSaved)
                personalSaveButton.isEnabled = false
            }
        }

        personalNameEditText.afterTextChanged { personalNameWrapper.error = null }
        personalPhoneEditText.afterTextChanged { personalPhoneWrapper.error = null }
        personalEmailEditText.afterTextChanged { personalEmailWrapper.error = null }
        personalCityEditText.afterTextChanged { currentCityWrapper.error = null }
        personalSkillsEditText.afterTextChanged { skillsWrapper.error = null }
        personalHobbiesEditText.afterTextChanged { hobbiesWrapper.error = null }
        personalDescriptionEditText.afterTextChanged { descriptionWrapper.error = null }
    }

    fun updateResume(resume : Resume) {
        launch {
            database.resumeDAO().updateResume(CreateResumeActivity.tempResume)
            println(CreateResumeActivity.tempResume)
        }
    }

    fun runChecks() : Boolean {
        var passed = true
        if (personalNameEditText.text.isEmpty()) { personalNameWrapper.error="Can't be empty"; passed = false } else { personalNameWrapper.isErrorEnabled = false }
        if (personalPhoneEditText.text.isEmpty()) { personalPhoneWrapper.error="Can't be empty"; passed = false } else { personalPhoneWrapper.isErrorEnabled = false }
        if (personalEmailEditText.text.isEmpty()) { personalEmailWrapper.error="Can't be empty"; passed = false } else { personalEmailWrapper.isErrorEnabled = false }
        if (personalCityEditText.text.isEmpty()) { currentCityWrapper.error="Can't be empty"; passed = false } else { currentCityWrapper.isErrorEnabled = false }
        if (personalHobbiesEditText.text.isEmpty()) { hobbiesWrapper.error="Can't be empty"; passed = false } else { hobbiesWrapper.isErrorEnabled = false }
        if (personalSkillsEditText.text.isEmpty()) { skillsWrapper.error="Can't be empty"; passed = false } else { skillsWrapper.isErrorEnabled = false }
        if (personalDescriptionEditText.text.isEmpty()) { descriptionWrapper.error="Can't be empty"; passed = false } else { descriptionWrapper.isErrorEnabled = false }
        return passed
        }
}