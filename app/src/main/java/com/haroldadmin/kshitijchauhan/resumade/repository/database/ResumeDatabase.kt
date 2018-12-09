package com.haroldadmin.kshitijchauhan.resumade.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.haroldadmin.kshitijchauhan.resumade.utilities.AppDispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [(Resume::class), (Education::class), (Experience::class), (Project::class)], version = 2)
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
						.addCallback(object : Callback() {
							override fun onCreate(db: SupportSQLiteDatabase) {
								super.onCreate(db)
								GlobalScope.launch (AppDispatchers.diskDispatcher) {
									val resume = Resume(
											"Example Resume",
											"John Doe",
											"1234567890",
											"johndoe@example.com",
											"San Francisco",
											"The most average guy on this planet. Will do any task you throw at me, and I'll do it in an average manner.",
											"Business Management, Average-anything",
											"Guitar, Biking, Cooking")
									resume.saved.set(true)
									ResumeDatabase.getInstance(it).resumeDAO().insertResume(resume)
								}
							}
						})
						.fallbackToDestructiveMigration()
						.build()
			}
	)

}