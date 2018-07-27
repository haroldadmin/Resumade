package com.haroldadmin.kshitijchauhan.resumade.repository.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

abstract class ResumeEntity {
	@PrimaryKey(autoGenerate = true)
	var id : Long = 0L

	abstract fun isEmpty() : Boolean

	fun isNotEmpty() : Boolean = !isEmpty()
}

@Entity(tableName = "resumes")
data class Resume(@ColumnInfo(name = "resumeName") var resumeName: String = "My Resume",
             @ColumnInfo(name = "name") var name: String,
             @ColumnInfo(name = "phone") var phone: String,
             @ColumnInfo(name = "email") var email: String,
             @ColumnInfo(name = "currentCity") var currentCity: String,
             @ColumnInfo(name = "description") var description: String,
             @ColumnInfo(name = "skills") var skills: String,
             @ColumnInfo(name = "hobbies") var hobbies: String) : ResumeEntity() {

	override fun isEmpty() : Boolean {
		if (name.isBlank() &&
				phone.isBlank() &&
				email.isBlank() &&
				currentCity.isBlank() &&
				description.isBlank() &&
				skills.isBlank() &&
				hobbies.isBlank()) { return true }
		return false
	}
}

@Entity(tableName = "education",
		foreignKeys = [(ForeignKey(entity = Resume::class,
				parentColumns = arrayOf("id"),
				childColumns = arrayOf("resumeId"),
				onDelete = ForeignKey.CASCADE))])
data class Education (
		@ColumnInfo(name = "instituteName")
		var instituteName: String,
		@ColumnInfo(name = "degree")
		var degree: String,
		@ColumnInfo(name = "yearOfGraduation")
		var year: String,
		@ColumnInfo(name = "performance")
		var performance: String,
		@ColumnInfo(name = "resumeId")
		var resumeId: Long) : ResumeEntity() {

	override fun isEmpty() : Boolean {
		if (instituteName.isBlank() &&
				degree.isBlank() &&
				year.isBlank() &&
				performance.isBlank()) {
			return true
		}
		return false
	}

}

@Entity(tableName = "experience",
		foreignKeys = [(ForeignKey(entity = Resume::class,
				parentColumns = arrayOf("id"),
				childColumns = arrayOf("resumeId"),
				onDelete = ForeignKey.CASCADE))])
data class Experience (

		@ColumnInfo(name = "companyName")
		var companyName: String,
		@ColumnInfo(name = "jobTitle")
		var jobTitle: String,
		@ColumnInfo(name = "duration")
		var duration: String,
		@ColumnInfo(name = "resumeId")
		var resumeId: Long) : ResumeEntity() {

	override fun isEmpty(): Boolean {
		if (companyName.isBlank() &&
				jobTitle.isBlank() &&
				duration.isBlank()) {
			return true
		}
		return false
	}
}

@Entity(tableName = "projects",
		foreignKeys = [(ForeignKey(entity = Resume::class,
				parentColumns = arrayOf("id"),
				childColumns = arrayOf("resumeId"),
				onDelete = ForeignKey.CASCADE))])
data class Project (

		@ColumnInfo(name = "projectName")
		var projectName: String,
		@ColumnInfo(name = "role")
		var role: String,
		@ColumnInfo(name = "link")
		var link: String,
		@ColumnInfo(name = "description")
		var description: String,
		@ColumnInfo(name = "resumeId")
		var resumeId: Long) : ResumeEntity() {

	override fun isEmpty(): Boolean {
		if (projectName.isBlank() &&
				role.isBlank() &&
				link.isBlank() &&
				description.isBlank()) {
			return true
		}
		return false
	}
}