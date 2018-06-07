package com.haroldadmin.kshitijchauhan.resuminator.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Resume
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import kotlinx.android.synthetic.main.personal_fragment.*
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
            with(CreateResumeActivity.tempResume) {
                resumeName = resumeNameEditText.text.toString()
                name = personalNameEditText.text.toString()
                phone = personalPhoneEditText.text.toString()
                email = personalEmailEditText.text.toString()
                currentCity = personalEmailEditText.text.toString()
                skills = personalSkillsEditText.text.toString()
                hobbies = personalHobbiesEditText.text.toString()
            }
            updateResume(CreateResumeActivity.tempResume)
            personalSaveButton.text = resources.getString(R.string.saveButtonSaved)
            personalSaveButton.isEnabled = false
        }
    }

    fun updateResume(resume : Resume) {
        launch {
            database.resumeDAO().updateResume(CreateResumeActivity.tempResume)
            println(CreateResumeActivity.tempResume)
        }
    }

}