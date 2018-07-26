package com.haroldadmin.kshitijchauhan.resumade.repository

import android.app.Application
import android.arch.lifecycle.LiveData
import com.haroldadmin.kshitijchauhan.resumade.repository.database.*
import com.haroldadmin.kshitijchauhan.resumade.utilities.AppExecutors
import java.util.concurrent.Callable
import java.util.concurrent.Future


/*
This is the local repository layer that
interacts with the database.
 */
class LocalRepository(application: Application) : Repository {

	val database : ResumeDatabase = ResumeDatabase.getInstance(application)

	override fun getAllResume(): LiveData<List<Resume>> = database.resumeDAO().getAllResume()

	override fun getResumeForId(resumeId: Long): LiveData<Resume> = database.resumeDAO().getResumeForId(resumeId)

	override fun insertResume(resume: Resume): Long {
		val id : Future<Long> = AppExecutors.diskIO.submit(
				Callable<Long> {
					database.resumeDAO().insertResume(resume)
				}
		)
		return id.get()
	}

	override fun deleteResume(resume: Resume) = AppExecutors.diskIO.execute {
		database.resumeDAO().deleteResume(resume)
	}

	override fun deleteResumeForId(resumeId: Long)  = AppExecutors.diskIO.execute {
		database.resumeDAO().deleteResumeForId(resumeId)
	}

	override fun updateResume(resume: Resume) = AppExecutors.diskIO.execute {
		database.resumeDAO().updateResume(resume)
	}

	override fun getLastResumeId(): LiveData<Long> = database.resumeDAO().getLastResumeId()

	override fun getAllEducationForResume(resumeId: Long): LiveData<List<Education>> = database.educationDAO().getEducationForResume(resumeId)

	override fun insertEducation(education: Education): Long {
		val id : Future<Long> =
		AppExecutors.diskIO.submit(Callable<Long> {
			database.educationDAO().insertEducation(education)
		})
		return id.get()
	}

	override fun deleteEducation(education: Education) = AppExecutors.diskIO.execute {
		database.educationDAO().deleteEducation(education)
	}

	override fun updateEducation(education: Education) = AppExecutors.diskIO.execute {
		database.educationDAO().updateEducation(education)
	}

	override fun getAllExperienceForResume(resumeId: Long): LiveData<List<Experience>> = database.experienceDAO().getExperienceForResume(resumeId)

	override fun insertExperience(experience: Experience): Long {
		val id : Future<Long> =
				AppExecutors.diskIO.submit(Callable<Long> {
					database.experienceDAO().insertExperience(experience)
				})
		return id.get()
	}

	override fun deleteExperience(experience: Experience) = AppExecutors.diskIO.execute {
		database.experienceDAO().deleteExperience(experience)
	}

	override fun updateExperience(experience: Experience) = AppExecutors.diskIO.execute {
		database.experienceDAO().updateExperience(experience)
	}

	override fun getAllProjectsForResume(resumeId: Long): LiveData<List<Project>> = database.projectsDAO().getProjectsForResume(resumeId)

	override fun insertProject(project: Project): Long {
		val id : Future<Long> =
				AppExecutors.diskIO.submit(Callable<Long> {
					database.projectsDAO().insertProject(project)
				})
		return id.get()
	}

	override fun deleteProject(project: Project) = AppExecutors.diskIO.execute {
		database.projectsDAO().deleteProject(project)
	}

	override fun updateProject(project: Project) = AppExecutors.diskIO.execute {
		database.projectsDAO().updateProject(project)
	}
}