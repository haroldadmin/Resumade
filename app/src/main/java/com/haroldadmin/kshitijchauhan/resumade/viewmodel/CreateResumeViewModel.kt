package com.haroldadmin.kshitijchauhan.resumade.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.haroldadmin.kshitijchauhan.resumade.repository.LocalRepository
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity

class CreateResumeViewModel(application: Application) : AndroidViewModel(application) {

	private val TAG = this::class.java.simpleName
	private var resumeId: Long
	var resume: LiveData<Resume>
	var educationList: LiveData<List<Education>>
	var experienceList: LiveData<List<Experience>>
	var projectsList: LiveData<List<Project>>

	/*
	Initializing these values as true because
	the user might not want to add any of these
	details at all. These will be set to false
	upon the creation of an item in the respective
	categories, and then back to true when the item
	is saved. This also ensures that no empty item
	is saved too.
	 */
	var personalDetailsSaved : Boolean = true
	var educationDetailsSaved : Boolean = true
	var experienceDetailsSaved : Boolean = true
	var projectDetailsSaved : Boolean = true

	private var repository: LocalRepository = LocalRepository(getApplication())

	init {
		resumeId = CreateResumeActivity.currentResumeId
		if (resumeId == -1L) {
			/*
			We can run this in a non blocking way
			because we don't care about the resumeId
			for a new resume as long as the user does
			not press the save button on the personal fragment,
			or the add button in any other fragments. Those events
			occur a long time after the viewmodel is created,
			so we can ignore the possibility of resumeId being -1L
			any time when it actually matters.
			 */
			resumeId = repository.insertResume(Resume("My Resume", "", "", "", "", "", "", ""))
		}
		resume = repository.getResumeForId(resumeId)
		educationList = repository.getAllEducationForResume(resumeId)
		experienceList = repository.getAllExperienceForResume(resumeId)
		projectsList = repository.getAllProjectsForResume(resumeId)
	}

	fun insertBlankEducation() {
		val education = Education("", "", "", "", resumeId)
		repository.insertEducation(education)
	}

	fun insertBlankExperience() {
		val experience = Experience("", "", "", resumeId)
		repository.insertExperience(experience)
	}

	fun insertBlankProject() {
		val project = Project("", "", "", "", resumeId)
		repository.insertProject(project)
	}

	fun updateResume(resume : Resume) {
		resume.id = resumeId
		repository.updateResume(resume)
	}

	fun updateEducation(education: Education) {
		repository.updateEducation(education)
	}

	fun updateExperience(experience: Experience) = repository.updateExperience(experience)

	fun updateProject(project: Project) = repository.updateProject(project)

	fun deleteEducation(education : Education) = repository.deleteEducation(education)

	fun deleteExperience(experience: Experience) = repository.deleteExperience(experience)

	fun deleteProject(project: Project) = repository.deleteProject(project)

	fun deleteTempResume() {
		repository.deleteResumeForId(resumeId)
	}

}