package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity
import com.haroldadmin.kshitijchauhan.resumade.utilities.showKeyboard
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_personal.*
import java.util.regex.Pattern

class PersonalFragment : Fragment() {

	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var resume: Resume
	private lateinit var personalSaveButton: MaterialButton
	private lateinit var personalNameWrapper: TextInputLayout
	private lateinit var personalEmailWrapper: TextInputLayout
	private lateinit var personalPhoneWrapper: TextInputLayout
	private lateinit var personalCityWrapper: TextInputLayout
	private lateinit var personalSkillsWrapper: TextInputLayout
	private lateinit var personalHobbiesWrapper: TextInputLayout
	private lateinit var personalDescriptionWrapper: TextInputLayout
	private lateinit var resumeNameWrapper : TextInputLayout

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
					this.resume = it ?: Resume("", "", "", "", "", "", "", "")
					fillTextFieldsWithData(this.resume)
					adjustSaveButton(this.resume.saved.get())
					createResumeViewModel.personalDetailsSaved = this.resume.saved.get()
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		personalNameWrapper = view.findViewById(R.id.personalNameWrapper)
		personalEmailWrapper = view.findViewById(R.id.personalEmailWrapper)
		personalPhoneWrapper = view.findViewById(R.id.personalPhoneWrapper)
		personalCityWrapper = view.findViewById(R.id.personalCityWrapper)
		personalSkillsWrapper = view.findViewById(R.id.personalSkillsWrapper)
		personalHobbiesWrapper = view.findViewById(R.id.personalHobbiesWrapper)
		personalDescriptionWrapper = view.findViewById(R.id.personalDescriptionWrapper)
		resumeNameWrapper = view.findViewById(R.id.resumeNameWrapper)
		personalSaveButton = view.findViewById(R.id.personalSaveButton)
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
		item.saved.set(true)
		createResumeViewModel.apply {
			updateResume(item as Resume)
		}
	}

	private fun <T : ResumeEntity> onEditButtonClicked(item: T) {
		item.saved.set(false)
		createResumeViewModel.apply {
			updateResume(item as Resume)
		}
	}

	private fun isEmailValid(email: String): Boolean {
		val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
		val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
		val matcher = pattern.matcher(email.trim())
		return matcher.matches()
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

		var passed = true

		when {
			tempName.isEmpty() -> {
				personalNameWrapper.error = "Can't be empty"
				passed = false
			}
			else -> personalNameWrapper.isErrorEnabled = false
		}
		when {
			tempPhone.isEmpty() -> {
				personalPhoneWrapper.error = "Can't be empty"
				passed = false
			}
			tempPhone.trim().length < 10 -> {
				personalPhoneWrapper.error = "Phone number should be 10 digits"
				passed = false
			}
			else -> personalPhoneWrapper.isErrorEnabled = false
		}
		when {
			tempEmail.isEmpty() -> {
				personalEmailWrapper.error = "Please enter an email"
				passed = false
			}
			!isEmailValid(tempEmail) -> {
				personalEmailWrapper.error = "Not a valid email"
				passed = false
			}
			else -> personalEmailWrapper.isErrorEnabled = false
		}
		if (tempCity.isEmpty()) {
			personalCityWrapper.error = "Please enter a city"
			passed = false
		} else {
			personalCityWrapper.isErrorEnabled = false
		}
		if (tempHobbies.isEmpty()) {
			personalHobbiesWrapper.error = "Please enter some hobbies"
			passed = false
		} else {
			personalHobbiesWrapper.isErrorEnabled = false
		}
		if (tempSkills.isEmpty()) {
			personalSkillsWrapper.error = "Please enter some skills"
			passed = false
		} else {
			personalSkillsWrapper.isErrorEnabled = false
		}
		if (tempDescription.isEmpty()) {
			personalDescriptionWrapper.error = "Please describe yourself"
			passed = false
		} else {
			personalDescriptionWrapper.isErrorEnabled = false
		}
		if (tempResumeName.isEmpty()) {
			resumeNameWrapper.error = "Please enter a name for this resume"
		} else {
			resumeNameWrapper.isErrorEnabled = false
		}
		return passed
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