package com.haroldadmin.kshitijchauhan.resumade.repository.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*

@Dao
interface ResumeDAO {

	@Query("SELECT * FROM resumes")
	fun getAllResume(): LiveData<List<Resume>>

	@Query("SELECT * FROM resumes WHERE id=:resumeId")
	fun getResumeForId(resumeId : Long) : LiveData<Resume>

	@Query("SELECT MAX(id) FROM resumes")
	fun getLastResumeId() : LiveData<Long>

	@Insert
	fun insertResume(resume: Resume) : Long

	@Delete
	fun deleteResume(resume : Resume)

	@Query("DELETE FROM resumes WHERE id=:resumeId")
	fun deleteResumeForId(resumeId : Long)

	@Update
	fun updateResume(resume : Resume)

	@Query("SELECT * FROM resumes WHERE id=:resumeId")
	fun getSingleResume(resumeId: Long) : Resume
}

@Dao
interface EducationDAO {

	@Query("SELECT * FROM education")
	fun getAllEducation() : MutableList<Education>

	@Query("SELECT * FROM education WHERE resumeId=:resumeId")
	fun getEducationForResume(resumeId : Long) : LiveData<List<Education>>

	@Query("SELECT * FROM education WHERE resumeId=:resumeId")
	fun getEducationForResumeOnce(resumeId : Long) : List<Education>

	@Query("SELECT count(*) FROM education")
	fun getEducationId() : Long

	@Insert
	fun insertEducation(education : Education) : Long

	@Delete
	fun deleteEducation(education : Education)

	@Update
	fun updateEducation(education: Education)
}

@Dao
interface ExperienceDAO {

	@Query("SELECT * FROM experience")
	fun getAllExperience() : LiveData<List<Experience>>

	@Query("SELECT * FROM experience WHERE resumeId=:resumeId")
	fun getExperienceForResume(resumeId : Long) : LiveData<List<Experience>>

	@Query("SELECT * FROM experience WHERE resumeId=:resumeId")
	fun getExperienceForResumeOnce(resumeId : Long) : List<Experience>

	@Query("SELECT count(*) FROM experience")
	fun getExperienceId() : Long

	@Insert
	fun insertExperience(experience : Experience) : Long

	@Delete
	fun deleteExperience(experience : Experience)

	@Update
	fun updateExperience(experience : Experience)
}

@Dao
interface ProjectsDAO {

	@Query("SELECT * FROM projects")
	fun getAllProjects() : LiveData<List<Project>>

	@Query("SELECT * FROM projects WHERE resumeId=:resumeId")
	fun getProjectsForResume(resumeId: Long) : LiveData<List<Project>>

	@Query("SELECT * FROM projects WHERE resumeId=:resumeId")
	fun getProjectsForResumeOnce(resumeId: Long) : List<Project>

	@Query("SELECT count(*) FROM projects")
	fun getProjectId() : Long

	@Insert
	fun insertProject(project : Project) : Long

	@Update
	fun updateProject(project : Project)

	@Delete
	fun deleteProject(project : Project)
}
