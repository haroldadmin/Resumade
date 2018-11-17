package com.haroldadmin.kshitijchauhan.resumade.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.haroldadmin.kshitijchauhan.resumade.repository.database.*
import com.haroldadmin.kshitijchauhan.resumade.utilities.AppDispatchers
import kotlinx.coroutines.withContext


/*
This is the local repository layer that
interacts with the database.
 */
class LocalRepository(application: Application) : Repository {

    val database: ResumeDatabase = ResumeDatabase.getInstance(application)

    override fun getAllResume(): LiveData<List<Resume>> = database.resumeDAO().getAllResume()

    override fun getResumeForId(resumeId: Long): LiveData<Resume> = database.resumeDAO().getResumeForId(resumeId)

    override fun getSingleResumeForId(resumeId: Long) = database.resumeDAO().getSingleResume(resumeId)

    override suspend fun insertResume(resume: Resume): Long = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().insertResume(resume)
    }

    override suspend fun deleteResume(resume: Resume) = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().deleteResume(resume)
    }

    override suspend fun deleteResumeForId(resumeId: Long) = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().deleteResumeForId(resumeId)
    }

    override suspend fun updateResume(resume: Resume) = withContext(AppDispatchers.diskDispatcher) {
        database.resumeDAO().updateResume(resume)
    }

    override fun getLastResumeId(): LiveData<Long> = database.resumeDAO().getLastResumeId()

    override fun getAllEducationForResume(resumeId: Long): LiveData<List<Education>> = database.educationDAO().getEducationForResume(resumeId)

    override fun getAllEducationForResumeOnce(resumeId: Long): List<Education> = database.educationDAO().getEducationForResumeOnce(resumeId)

    override suspend fun insertEducation(education: Education): Long =
            withContext(AppDispatchers.diskDispatcher) {
                database.educationDAO().insertEducation(education)
            }

    override suspend fun deleteEducation(education: Education) = withContext(AppDispatchers.diskDispatcher) {
        database.educationDAO().deleteEducation(education)
    }

    override suspend fun updateEducation(education: Education) = withContext(AppDispatchers.diskDispatcher) {
        database.educationDAO().updateEducation(education)
    }

    override fun getAllExperienceForResume(resumeId: Long): LiveData<List<Experience>> = database.experienceDAO().getExperienceForResume(resumeId)

    override fun getAllExperienceForResumeOnce(resumeId: Long): List<Experience> = database.experienceDAO().getExperienceForResumeOnce(resumeId)

    override suspend fun insertExperience(experience: Experience): Long =
            withContext(AppDispatchers.diskDispatcher) {
                database.experienceDAO().insertExperience(experience)
            }

    override suspend fun deleteExperience(experience: Experience) = withContext(AppDispatchers.diskDispatcher) {
        database.experienceDAO().deleteExperience(experience)
    }

    override suspend fun updateExperience(experience: Experience) = withContext(AppDispatchers.diskDispatcher) {
        database.experienceDAO().updateExperience(experience)
    }

    override fun getAllProjectsForResume(resumeId: Long): LiveData<List<Project>> = database.projectsDAO().getProjectsForResume(resumeId)

    override fun getAllProjectsForResumeOnce(resumeId: Long): List<Project> = database.projectsDAO().getProjectsForResumeOnce(resumeId)

    override suspend fun insertProject(project: Project): Long = withContext(AppDispatchers.diskDispatcher) {
        database.projectsDAO().insertProject(project)
    }

    override suspend fun deleteProject(project: Project) = withContext(AppDispatchers.diskDispatcher) {
        database.projectsDAO().deleteProject(project)
    }

    override suspend fun updateProject(project: Project) = withContext(AppDispatchers.diskDispatcher) {
        database.projectsDAO().updateProject(project)
    }
}