package com.haroldadmin.kshitijchauhan.resumade.repository

import androidx.lifecycle.LiveData
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume

interface Repository {

	/*
	Any data repository should implement
	following methods.
	 */

	// Tasks related to resume-list
	fun getAllResume() : LiveData<List<Resume>>
	fun getResumeForId(resumeId: Long): LiveData<Resume>
	fun getSingleResumeForId(resumeId : Long) : Resume
	fun getLastResumeId() : LiveData<Long>
	suspend fun insertResume(resume: Resume): Long
	suspend fun deleteResume(resume : Resume)
	suspend fun updateResume(resume : Resume)
	suspend fun deleteResumeForId(resumeId : Long)

	// Tasks related to education-list
	fun getAllEducationForResume(resumeId: Long) : LiveData<List<Education>>
	fun getAllEducationForResumeOnce(resumeId: Long) : List<Education>
	suspend fun insertEducation(education: Education): Long
	suspend fun deleteEducation(education: Education)
	suspend fun updateEducation(education: Education)

	// Tasks related to experience-list
	fun getAllExperienceForResume(resumeId: Long) : LiveData<List<Experience>>
	fun getAllExperienceForResumeOnce(resumeId: Long) : List<Experience>
	suspend fun insertExperience(experience: Experience): Long
	suspend fun deleteExperience(experience: Experience)
	suspend fun updateExperience(experience: Experience)

	// Tasks related to projects-list
	fun getAllProjectsForResume(resumeId: Long) : LiveData<List<Project>>
	fun getAllProjectsForResumeOnce(resumeId : Long) : List<Project>
	suspend fun insertProject(project: Project): Long
	suspend fun deleteProject(project: Project)
	suspend fun updateProject(project: Project)
}