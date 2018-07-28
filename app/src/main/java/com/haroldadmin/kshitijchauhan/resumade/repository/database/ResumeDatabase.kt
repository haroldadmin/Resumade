package com.haroldadmin.kshitijchauhan.resumade.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(Resume::class), (Education::class), (Experience::class), (Project::class)], version = 1)
abstract class ResumeDatabase : RoomDatabase() {

	abstract fun resumeDAO() : ResumeDAO
	abstract fun educationDAO() : EducationDAO
	abstract fun experienceDAO() : ExperienceDAO
	abstract fun projectsDAO() : ProjectsDAO

	companion object : SingletonHolder<ResumeDatabase, Context>(
			{
				Room.databaseBuilder(it.applicationContext,
						ResumeDatabase::class.java,
						"resumes")
						.fallbackToDestructiveMigration()
						.build()
			}
	)

}