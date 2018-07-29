package com.haroldadmin.kshitijchauhan.resumade.utilities

import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume

fun createBaseHTML() : String {
	var html = ""
	html += "<!DOCTYPE html>\n" +
			"<html>\n" +
			"<head>\n" +
			"    <title>Resume</title>\n" +
			"\n" + "<style>\n" +
			"        /* body {\n" +
			"            background: #f5f5f5;\n" +
			"        } */\n" +
			"        a {\n" +
			"            color: #FFFFFF;\n" +
			"        }\n" +
			"        a:hover {\n" +
			"            color: #f5f5f5;\n" +
			"        }\n" +
			"        .main-content {\n" +
			"            width: 80%;\n" +
			"            font-family: Helvetica, sans-serif;\n" +
			"            padding-left: 20px;\n" +
			"            padding-top: 20px;\n" +
			"        }\n" +
			"\n" +
			"        .name {\n" +
			"            padding-bottom: 10px;\n" +
			"        }\n" +
			"        .phone, .email {\n" +
			"            line-height: 0.5;\n" +
			"        }\n" +
			"\n" +
			"        .phone-container, .email-container {\n" +
			"            display: flex;\n" +
			"            flex-direction: row;\n" +
			"        }\n" +
			"\n" +
			"        #phone-number, #email-address, #current-city {\n" +
			"            color: #c2c2c2;\n" +
			"            line-height: 0.6;\n" +
			"        }\n" +
			"\n" +
			"        .cards-list {\n" +
			"            display: flex;\n" +
			"            flex-flow: row wrap;\n" +
			"        }\n" +
			"\n" +
			"        .card {\n" +
			"            width: auto;\n" +
			"            max-width: 400px;\n" +
			"            min-width: 200px;\n" +
			"            margin: 10px 10px 10px 0px;\n" +
			"            box-shadow: 0px 0px 6px 4px rgba(0,0,0,0.2);\n" +
			"            border-radius: 6px;\n" +
			"            background: #3f89f4;\n" +
			"        }\n" +
			"\n" +
			"        .card-container {\n" +
			"            color: white;\n" +
			"            width: auto;\n" +
			"            height: auto;\n" +
			"        }\n" +
			"\n" +
			"        .card-title {\n" +
			"            font-weight: 700;\n" +
			"            font-size: 1.2em;\n" +
			"            border-bottom: 1px solid #7eb8ff;\n" +
			"        }\n" +
			"\n" +
			"        .card-content {\n" +
			"            padding: 0px 10px 0px 10px;\n" +
			"        }\n" +
			"\n" +
			"        .card-title-text {\n" +
			"            padding: 10px 10px 0px 10px;\n" +
			"        }\n" +
			"\n" +
			"        .skills-list {\n" +
			"            width: fit-content;\n" +
			"        }\n" +
			"        li {\n" +
			"            line-height: 1.5;\n" +
			"        }\n" +
			"    </style>" +
			"</head>" +
			"<body class=\"main-content\">\n"

	return html
}

fun addPersonalInfo(resume: Resume) : String {
	var html = ""
	if (resume.isNotEmpty()) {
		html += "<div class=\"personal-info\">\n" +
				"        <div class=\"name-container\">\n" +
				"            <h1 class=\"name\">${resume.name.toUpperCase()}</h1>\n" +
				"        </div>\n" +
				"        <div class=\"contact-info-container\">\n" +
				"            <p id=\"phone-number\">${resume.phone}</p> \n" +
				"            <p id=\"email-address\">${resume.email}</p>\n" +
				"            <p id=\"current-city\">${resume.currentCity}</p>\n" +
				"        </div>\n" +
				"        <br>\n" +
				"        <div class=\"description\">\n" +
				"            <p id=\"description\">${resume.description}</p>\n" +
				"        </div>\n" +
				"        <br>\n" +
				"    </div>"
	}
	return html
}

fun addEducationInfo(educationList: List<Education>) : String {
	var html = ""
	if (!educationList.isEmpty() && educationList.first().isNotEmpty()) {
		html += "<div class=\"education-info\">\n" +
				"        <h2>Education</h2>\n" +
				"        <div class=\"cards-list\">"
		for (education in educationList) {
			if (education.isNotEmpty()) {
				html += "<div class=\"card education-card\">\n" +
						"                <div class=\"card-container\">\n" +
						"                    <div class=\"card-title\">\n" +
						"                        <p class=\"card-title-text\" id=\"institute-name\">${education.instituteName}</p>\n" +
						"                    </div>\n" +
						"                    <div class=\"card-content\">\n" +
						"                        <p id=\"degree\">${education.degree}</p>\n" +
						"                        <p id=\"performance\">${education.performance}</p>\n" +
						"                        <p id=\"year-of-graduation\">${education.year}</p>\n" +
						"                    </div>\n" +
						"                </div>\n" +
						"            </div>"
			}
		}
		html += "</div>\n" +
				"    </div>"
	}
	return html
}

fun addExperienceInfo(experienceList : List<Experience>) : String {
	var html = ""
	if (!experienceList.isEmpty() && experienceList.first().isNotEmpty()) {
		html += "<br>\n" +
				"    <div class=\"experience-info\">\n" +
				"        <h2>Experience</h2>\n" +
				"        <div class=\"cards-list\">"
		for (experience in experienceList) {
			if (experience.isNotEmpty()) {
				html += "<div class=\"card experience-card\">\n" +
						"                <div class=\"card-container\">\n" +
						"                    <div class=\"card-title\">\n" +
						"                        <p class=\"card-title-text\" id=\"experience-name\">${experience.companyName}</p>\n" +
						"                    </div>\n" +
						"                    <div class=\"card-content\">\n" +
						"                        <p id=\"experience-jobTitle\">${experience.jobTitle}</p>\n" +
						"                        <p id=\"experience-duration\">${experience.duration}</p>\n" +
						"                    </div>\n" +
						"                </div>\n" +
						"            </div>"
			}
		}

		html += "</div>\n" +
				"    </div>\n" +
				"    <br>"
	}
	return html
}

fun addProjectInfo(projectsList : List<Project>) : String {
	var html = ""
	if (!projectsList.isEmpty() && projectsList.first().isNotEmpty()) {
		html += "<div class=\"projects-info\">\n" +
				"        <h2>Projects</h2>\n" +
				"        <div class=\"cards-list\">"
		for (project in projectsList) {
			if (project.isNotEmpty()) {
				html += "<div class=\"card project-card\">\n" +
						"                <div class=\"card-container\">\n" +
						"                    <div class=\"card-title\">\n" +
						"                        <p class=\"card-title-text\" id=\"project-name\">${project.projectName}</p>\n" +
						"                    </div>\n" +
						"                    <div class=\"card-content\">\n" +
						"                        <p id=\"project-role\">${project.role}</p>\n"
				// We need to add this check because projectLink might be empty
				if (!project.link.isEmpty()) {
					html += "<p id=\"project-link\"><a href=\"${project.link}\">${project.link}</a></p>\n"
				}
				html +=
						"                        <p id=\"project-description\">${project.description}</p>\n" +
						"                    </div>\n" +
						"                </div>\n" +
						"            </div>"
			}

			html += "</div>\n" +
					"    </div>\n" +
					"    <br>"
		}
	}
	return html
}

fun addSkills(resume : Resume) : String {
	var html = ""
	if (resume.skills != "") {
		val skillsList = resume.skills.split(",")
		skillsList.map { it -> it.trim() }
		if (skillsList.isNotEmpty() && skillsList.first().isNotBlank()) {
			html += "    <div class=\"skills\">\n" +
					"        <div class=\"skills-header\">\n" +
					"            <h2>Skills</h2>\n" +
					"        </div>\n" +
					"        <div class=\"skills-text\">\n" +
					"            <ul class=\"skills-list\">"
			for (skill in skillsList) {
				if (skill.isNotBlank()) {
					html += "<li>$skill</li>"
				}
			}

			html += "            </ul>\n" +
					"        </div>\n" +
					"    </div>\n" +
					"    <br>"
		}
	}
	return html
}

fun addHobbies(resume : Resume) : String {
	var html = ""
	if (resume.hobbies != "") {
		val hobbiesList = resume.hobbies.split(",")
		hobbiesList.map { it -> it.trim() }
		if (hobbiesList.isNotEmpty() && hobbiesList.first().isNotBlank()) {
			html += "    <div class=\"hobbies\">\n" +
					"        <div class=\"hobbies-header\">\n" +
					"            <h2>Hobbies</h2>\n" +
					"        </div>\n" +
					"        <div class=\"hobbies-text\">\n" +
					"            <ul>"
			for (hobby in hobbiesList) {
				if (hobby.isNotBlank()) {
					html += "<li>$hobby</li>"
				}
			}

			html += "            </ul>\n" +
					"        </div>\n" +
					"    </div>"
		}
	}
	return html
}

fun closeHtmlFile() : String {
	var html = ""
	html += "</body>\n" +
			"</html>"
	return html
}

fun buildHtml(resume : Resume, educationList: List<Education>, experienceList: List<Experience>, projectList: List<Project>) : String {
	var html = ""
	html += createBaseHTML()
	html += addPersonalInfo(resume)
	html += addEducationInfo(educationList)
	html += addExperienceInfo(experienceList)
	html += addProjectInfo(projectList)
	html += addSkills(resume)
	html += addHobbies(resume)
	html += closeHtmlFile()
	return html
}