package com.haroldadmin.kshitijchauhan.resumade.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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

	fun getResumeForId(resumeId : Long) : Resume = repository.getSingleResumeForId(resumeId)

	fun getEducationForResume(resumeId : Long) : List<Education> = repository.getAllEducationForResumeOnce(resumeId)

	fun getExperienceForResume(resumeId: Long) : List<Experience> = repository.getAllExperienceForResumeOnce(resumeId)

	fun getProjectForResume(resumeId: Long) : List<Project> = repository.getAllProjectsForResumeOnce(resumeId)

}