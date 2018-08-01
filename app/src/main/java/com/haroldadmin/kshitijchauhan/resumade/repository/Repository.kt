package com.haroldadmin.kshitijchauhan.resumade.repository

import android.arch.lifecycle.LiveData
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
	fun insertResume(resume: Resume): Long
	fun deleteResume(resume : Resume)
	fun updateResume(resume : Resume)
	fun getLastResumeId() : LiveData<Long>
	fun deleteResumeForId(resumeId : Long)

	// Tasks related to education-list
	fun getAllEducationForResume(resumeId: Long) : LiveData<List<Education>>
	fun getAllEducationForResumeOnce(resumeId: Long) : List<Education>
	fun insertEducation(education: Education): Long
	fun deleteEducation(education: Education)
	fun updateEducation(education: Education)

	// Tasks related to experience-list
	fun getAllExperienceForResume(resumeId: Long) : LiveData<List<Experience>>
	fun getAllExperienceForResumeOnce(resumeId: Long) : List<Experience>
	fun insertExperience(experience: Experience): Long
	fun deleteExperience(experience: Experience)
	fun updateExperience(experience: Experience)

	// Tasks related to projects-list
	fun getAllProjectsForResume(resumeId: Long) : LiveData<List<Project>>
	fun getAllProjectsForResumeOnce(resumeId : Long) : List<Project>
	fun insertProject(project: Project): Long
	fun deleteProject(project: Project)
	fun updateProject(project: Project)
}