package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.R.id.*
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity
import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.*
import com.haroldadmin.kshitijchauhan.resumade.utilities.inputvalidator.validators.TextValidator
import com.haroldadmin.kshitijchauhan.resumade.utilities.showKeyboard
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_personal.*

class PersonalFragment : Fragment() {

    private lateinit var createResumeViewModel: CreateResumeViewModel
    private lateinit var resume: Resume

    private var tempResumeName = ""
    private var tempName = ""
    private var tempEmail = ""
    private var tempPhone = ""
    private var tempCity = ""
    private var tempSkills = ""
    private var tempHobbies = ""
    private var tempDescription = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createResumeViewModel = ViewModelProviders
            .of(activity!!)
            .get(CreateResumeViewModel::class.java)

        createResumeViewModel.resume
            .observe(viewLifecycleOwner, Observer {
                this.resume = it ?: Resume.emptyResume()
                fillTextFieldsWithData(this.resume)
                adjustSaveButton(this.resume.saved)
                createResumeViewModel.personalDetailsSaved = this.resume.saved
            })
    }

    private fun fillTextFieldsWithData(resume: Resume) {
        with(resume) {
            personalNameEditText.setText(name)
            personalPhoneEditText.setText(phone)
            personalEmailEditText.setText(email)
            personalCityEditText.setText(currentCity)
            personalSkillsEditText.setText(skills)
            personalHobbiesEditText.setText(hobbies)
            personalDescriptionEditText.setText(description)
            resumeNameEditText.setText(resumeName)
        }
    }

    private fun adjustSaveButton(resumeSavedStatus: Boolean) {
        personalSaveButton.apply {
            if (resumeSavedStatus) {
                // Edit Mode
                this.text = context.resources.getString(R.string.editButtonText)
                disableTextFields()
                this.setOnClickListener {
                    enableTextFields()
                    onEditButtonClicked(resume)
                    this.text = context.resources.getString(R.string.saveButtonText)
                }
            } else {
                // Save Mode
                this.text = context.resources.getString(R.string.saveButtonText)
                setOnClickListener {
                    if (runChecks()) {
                        resume.apply {
                            resumeName = tempResumeName
                            name = tempName
                            phone = tempPhone
                            email = tempEmail
                            currentCity = tempCity
                            skills = tempSkills
                            hobbies = tempHobbies
                            description = tempDescription
                        }
                        onSaveButtonClick(resume)
                        disableTextFields()
                        this.text = context.resources.getString(R.string.editButtonText)
                    }
                }
            }
        }
    }


    private fun <T : ResumeEntity> onSaveButtonClick(item: T) {
        item.saved = true
        createResumeViewModel.apply {
            updateResume(item as Resume)
        }
    }

    private fun <T : ResumeEntity> onEditButtonClicked(item: T) {
        item.saved = false
        createResumeViewModel.apply {
            updateResume(item as Resume)
        }
    }

    private fun runChecks(): Boolean {
        tempResumeName = resumeNameEditText.text.toString()
        tempName = personalNameEditText.text.toString()
        tempPhone = personalPhoneEditText.text.toString()
        tempEmail = personalEmailEditText.text.toString()
        tempCity = personalCityEditText.text.toString()
        tempSkills = personalSkillsEditText.text.toString()
        tempHobbies = personalHobbiesEditText.text.toString()
        tempDescription = personalDescriptionEditText.text.toString()

        arrayOf<Pair<InputValidator<Text>, TextInputLayout>>(
            tempName.addValidation()
                .required("Please enter your name")
                .validate() to personalNameWrapper,

            tempPhone.addValidation()
                .required("Please enter your phone number")
                .validate() to personalPhoneWrapper,

            tempEmail.addValidation()
                .required("Please enter your email address")
                .matches(
                    "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$".toRegex(RegexOption.IGNORE_CASE),
                    "Please enter a valid email"
                )
                .validate() to personalEmailWrapper,

            tempCity.addValidation()
                .required("Please enter a city")
                .validate() to personalCityWrapper,


            tempHobbies.addValidation()
                .required("Please enter your hobbies")
                .validate() to personalHobbiesWrapper,


            tempSkills.addValidation()
                .required("Please enter your skills")
                .validate() to personalSkillsWrapper,

            tempDescription.addValidation()
                .required("Please describe yourself")
                .minLength(20, "Please add at least 20 characters")
                .validate() to personalDescriptionWrapper,


            tempResumeName.addValidation()
                .required("Resume name is required")
                .matches("^[a-zA-Z0-9 ]*$".toRegex(RegexOption.IGNORE_CASE), "Can contain only alphabets and numbers")
                .validate() to resumeNameWrapper
        )
            .validateAll()
            .also { result ->
                return result
            }
    }

    private fun toggleTextFields(value: Boolean) {
        personalNameWrapper.isEnabled = value
        personalEmailWrapper.isEnabled = value
        personalPhoneWrapper.isEnabled = value
        personalCityWrapper.isEnabled = value
        personalSkillsWrapper.isEnabled = value
        personalHobbiesWrapper.isEnabled = value
        personalDescriptionWrapper.isEnabled = value
        resumeNameWrapper.isEnabled = value
    }

    private fun enableTextFields() {
        toggleTextFields(true)
        personalNameWrapper.showKeyboard(context)
    }

    private fun disableTextFields() = toggleTextFields(false)
}