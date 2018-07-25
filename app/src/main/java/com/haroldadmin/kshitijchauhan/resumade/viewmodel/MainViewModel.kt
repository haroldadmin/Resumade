package com.haroldadmin.kshitijchauhan.resumade.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.haroldadmin.kshitijchauhan.resumade.repository.LocalRepository
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume

/*
This ViewModel serves as the ViewModel for
the main activity. The CreateResumeActivity has a
separate viewmodel.
*/

class MainViewModel(application: Application) : AndroidViewModel(application) {

	private val TAG : String = this::class.java.simpleName

	/*
	Cached list of resume with a private setter
	so that it can only be read from other classes,
	but not modified.
	 */
	var resumesList: LiveData<List<Resume>>
		private set

	lateinit var educationList : List<Education>
	lateinit var experienceList : List<Experience>
	lateinit var projectList : List<Project>
	lateinit var resume: Resume

	/*
	The main view model extends AndroidViewModel,
	so it gets an instance of application in its constructor.
	We use this to initialize the repository,
	which in turn initializes the database.
	 */
	private var repository : LocalRepository = LocalRepository(getApplication())

	init {
		resumesList = repository.getAllResume()
	}

	fun deleteResume(resume : Resume) {
		repository.deleteResume(resume)
	}

	fun getListsAndResume(resumeId : Long) {
		resume = repository.getResumeForId(resumeId).value ?: Resume("", "", "", "", "", "", "", "")
		educationList = repository.getAllEducationForResume(resumeId).value ?: emptyList()
		experienceList = repository.getAllExperienceForResume(resumeId).value ?: emptyList()
		projectList = repository.getAllProjectsForResume(resumeId).value ?: emptyList()
	}

}