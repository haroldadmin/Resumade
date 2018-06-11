package com.haroldadmin.kshitijchauhan.resuminator.data

import android.arch.persistence.room.*

@Dao
interface ResumeDAO {

    @Query("SELECT * FROM resumes")
    fun getAllResume(): MutableList<Resume>

    @Query("SELECT * FROM resumes WHERE id=:resumeId")
    fun getResumeForId(resumeId : Long) : Resume

    @Query("SELECT MAX(id) FROM resumes")
    fun getNewResumeId() : Long

    @Insert
    fun insertResume(resume: Resume)

    @Delete
    fun deleteResume(resume : Resume)

    @Update
    fun updateResume(resume : Resume)
}

@Dao
interface EducationDAO {

    @Query("SELECT * FROM education")
    fun getAllEducation() : MutableList<Education>

    @Query("SELECT * FROM education WHERE resumeId=:resumeId")
    fun getEducationForResume(resumeId : Long?) : MutableList<Education>

    @Query("SELECT count(*) FROM education")
    fun getEducationId() : Long

    @Insert
    fun insertEducation(education : Education)
}

@Dao
interface ExperienceDAO {

    @Query("SELECT * FROM experience")
    fun getAllExperience() : MutableList<Experience>

    @Query("SELECT * FROM experience WHERE resumeId=:resumeId")
    fun getExperienceForResume(resumeId : Long?) : MutableList<Experience>

    @Query("SELECT count(*) FROM experience")
    fun getExperienceId() : Long

    @Insert
    fun insertExperience(experience : Experience)
}

@Dao
interface ProjectsDAO {

    @Query("SELECT * FROM projects")
    fun getAllProjects() : MutableList<Project>

    @Query("SELECT * FROM projects WHERE resumeId=:resumeId")
    fun getProjectForResume(resumeId: Long?) : MutableList<Project>

    @Query("SELECT count(*) FROM projects")
    fun getProjectId() : Long

    @Insert
    fun insertProject(project : Project)
}
