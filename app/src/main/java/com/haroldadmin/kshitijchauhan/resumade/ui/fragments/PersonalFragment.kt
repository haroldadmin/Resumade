package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_personal.*
import java.util.regex.Pattern

class PersonalFragment : Fragment(), SaveButtonClickListener {

	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var personalSaveButton: MaterialButton

	private lateinit var resumeName: String
	private lateinit var name: String
	private lateinit var email: String
	private lateinit var phone: String
	private lateinit var city: String
	private lateinit var skills: String
	private lateinit var hobbies: String
	private lateinit var description: String

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_personal, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		createResumeViewModel = ViewModelProviders
				.of(activity!!)
				.get(CreateResumeViewModel::class.java)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		personalSaveButton = view.findViewById(R.id.personalSaveButton)
		personalSaveButton.apply {
			setOnClickListener {
				if (runChecks()) {
					onSaveButtonClick(Resume(resumeName, name, phone, email, city, description, skills, hobbies))
					isEnabled = false
					text = "Edit"
				}
			}
		}
	}

	override fun onStart() {
		super.onStart()
		createResumeViewModel.resume
				.observe(this, Observer {
					it?.let { fillTextFieldsWithData(it) }
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

	override fun <T : ResumeEntity> onSaveButtonClick(item: T) {
		createResumeViewModel.apply {
			updateResume(item as Resume)
			personalDetailsSaved = true
		}
	}

	private fun isEmailValid(email: String): Boolean {
		val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
		val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
		val matcher = pattern.matcher(email.trim())
		return matcher.matches()
	}

	private fun runChecks(): Boolean {
		resumeName = resumeNameEditText.text.toString()
		name = personalNameEditText.text.toString()
		phone = personalPhoneEditText.text.toString()
		email = personalEmailEditText.text.toString()
		city = personalCityEditText.text.toString()
		skills = personalSkillsEditText.text.toString()
		hobbies = personalHobbiesEditText.text.toString()
		description = personalDescriptionEditText.text.toString()

		var passed = true

		when {
			name.isEmpty() -> {
				personalNameWrapper.error = "Can't be empty"
				passed = false
			}
			else -> personalNameWrapper.isErrorEnabled = false
		}
		when {
			phone.isEmpty() -> {
				personalPhoneWrapper.error = "Can't be empty"
				passed = false
			}
			phone.trim().length < 10 -> {
				personalPhoneWrapper.error = "Phone number should be 10 digits"
				passed = false
			}
			else -> personalPhoneWrapper.isErrorEnabled = false
		}
		when {
			email.isEmpty() -> {
				personalEmailWrapper.error = "Please enter an email"
				passed = false
			}
			!isEmailValid(email) -> {
				personalEmailWrapper.error = "Not a valid email"
				passed = false
			}
			else -> personalEmailWrapper.isErrorEnabled = false
		}
		if (city.isEmpty()) {
			personalCityWrapper.error = "Please enter a city"
			passed = false
		} else {
			personalCityWrapper.isErrorEnabled = false
		}
		if (hobbies.isEmpty()) {
			personalHobbiesWrapper.error = "Please enter some hobbies"
			passed = false
		} else {
			personalHobbiesWrapper.isErrorEnabled = false
		}
		if (skills.isEmpty()) {
			personalSkillsWrapper.error = "Please enter some skills"
			passed = false
		} else {
			personalSkillsWrapper.isErrorEnabled = false
		}
		if (description.isEmpty()) {
			personalDescriptionWrapper.error = "Please describe yourself"
			passed = false
		} else {
			personalDescriptionWrapper.isErrorEnabled = false
		}
		if (resumeName.isEmpty()) {
			resumeNameWrapper.error = "Please enter a name for this resume"
		} else {
			resumeNameWrapper.isErrorEnabled = false
		}
		return passed
	}
}