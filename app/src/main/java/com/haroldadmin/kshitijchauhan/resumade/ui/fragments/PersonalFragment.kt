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
import com.haroldadmin.kshitijchauhan.resumade.utilities.SaveButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.fragment_personal.*

class PersonalFragment : Fragment(), SaveButtonClickListener {

	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var personalSaveButton : MaterialButton

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
				onSaveButtonClick(null)
				isEnabled = false
				text = "Saved"
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

	override fun <Any> onSaveButtonClick(item: Any) {
		val resumeName = resumeNameEditText.text.toString()
		val name = personalNameEditText.text.toString()
		val phone = personalPhoneEditText.text.toString()
		val email = personalEmailEditText.text.toString()
		val city = personalCityEditText.text.toString()
		val skills = personalSkillsEditText.text.toString()
		val hobbies = personalHobbiesEditText.text.toString()
		val description =  personalDescriptionEditText.text.toString()
		val resume = Resume(resumeName, name, phone, email, city, description, skills, hobbies)
		createResumeViewModel.updateResume(resume)
	}
}