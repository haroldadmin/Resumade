package com.haroldadmin.kshitijchauhan.resuminator.data

import android.arch.persistence.room.*
import android.content.Context

@Database(entities = arrayOf(Resume::class, Education::class, Experience::class, Project::class), version = 1)
abstract class ResumeDatabase : RoomDatabase() {

    abstract fun resumeDAO() : resumeDAO
    abstract fun educationDAO() : educationDAO
    abstract fun experienceDAO() : experienceDAO
    abstract fun projectsDAO() : projectsDAO

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