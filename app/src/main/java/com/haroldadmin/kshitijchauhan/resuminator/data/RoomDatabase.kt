package com.haroldadmin.kshitijchauhan.resuminator.data

import android.arch.persistence.room.*
import android.content.Context

@Database(entities = arrayOf(Resume::class, Education::class, Experience::class, Project::class), version = 1)
abstract class ResumeDatabase : RoomDatabase() {

    abstract fun resumeDAO() : ResumeDAO
    abstract fun educationDAO() : EducationDAO
    abstract fun experienceDAO() : ExperienceDAO
    abstract fun projectsDAO() : ProjectsDAO

    companion object {
        private var INSTANCE : ResumeDatabase? = null
        fun getInstance (context : Context?) : ResumeDatabase? {
            if (INSTANCE == null) {
                synchronized(ResumeDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context!!.applicationContext, ResumeDatabase::class.java, "resumes.db").build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance() { INSTANCE = null }
    }

}