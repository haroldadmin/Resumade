package com.haroldadmin.kshitijchauhan.resuminator.data

import android.arch.persistence.room.*

@Entity(tableName = "resumes")
data class Resume(

        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        @ColumnInfo(name = "resumeName")
        var resumeName : String = "My Resume",
        @ColumnInfo(name = "name")
        var name : String,
        @ColumnInfo(name = "phone")
        var phone : String,
        @ColumnInfo(name = "email")
        var email : String,
        @ColumnInfo(name = "currentCity")
        var currentCity : String,
        @ColumnInfo(name = "description")
        var description: String,
        @ColumnInfo(name = "skills")
        var skills : String,
        @ColumnInfo(name = "hobbies")
        var hobbies : String
)

@Entity(tableName = "education",
        foreignKeys = arrayOf(ForeignKey(entity = Resume::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("resumeId"),
                onDelete = ForeignKey.CASCADE)))
data class Education(
        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        @ColumnInfo(name = "instituteName")
        var instituteName : String,
        @ColumnInfo(name = "degree")
        var degree : String,
        @ColumnInfo(name = "yearOfGraduation")
        var year : String,
        @ColumnInfo(name = "performance")
        var performance : String,
        @ColumnInfo(name = "resumeId")
        var resumeId : Long,
        @Ignore
        var saved : Boolean = false) {

    constructor() : this(0, "", "","", "", 0)

}

@Entity(tableName = "experience",
        foreignKeys = arrayOf(ForeignKey(entity = Resume::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("resumeId"),
                onDelete = ForeignKey.CASCADE)))
data class Experience(
        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        @ColumnInfo(name = "companyName")
        var companyName : String,
        @ColumnInfo(name = "jobTitle")
        var jobTitle : String,
        @ColumnInfo(name = "duration")
        var duration : String,
        @ColumnInfo(name = "resumeId")
        var resumeId : Long,
        @Ignore
        var saved : Boolean = false) {

    constructor() : this(0, "", "","", 0)
}

@Entity(tableName = "projects",
        foreignKeys = arrayOf(ForeignKey(entity = Resume::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("resumeId"),
                onDelete = ForeignKey.CASCADE)))
data class Project(
        @PrimaryKey(autoGenerate = true)
        var id : Long?,
        @ColumnInfo(name = "projectName")
        var projectName : String,
        @ColumnInfo(name = "role")
        var role : String,
        @ColumnInfo(name = "link")
        var link : String,
        @ColumnInfo(name = "description")
        var description : String,
        @ColumnInfo(name = "resumeId")
        var resumeId : Long,
        @Ignore
        var saved : Boolean = false) {

    constructor() : this(0, "", "","", "", 0)

}
