package com.haroldadmin.kshitijchauhan.resuminator.adapters

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.TextView
import com.crashlytics.android.Crashlytics
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity.Companion.educationList
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity.Companion.experienceList
import kotlinx.android.synthetic.main.activity_main.*
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.*
import com.haroldadmin.kshitijchauhan.resuminator.utilities.*
import kotlinx.android.synthetic.main.resume_card.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import kotlin.math.exp

class ResumeAdapter(var resumeList : MutableList<Resume>, val context : Context) : RecyclerView.Adapter<ResumeAdapter.ViewHolder>() {

    val database = ResumeDatabase.getInstance(context)!!
    val webView = WebView(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.resume_card, parent, false))
    }

    override fun getItemCount() = resumeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resume = resumeList.get(position)
        with(holder) {
            resumeName.text = resume.resumeName
            personalNameTextView.text = resume.name
            personalEmailTextView.text = resume.email
            deleteResumeButton.setOnClickListener {
                async(UI) {
                    Crashlytics.log(1, "Resume Adapter", "Deleting at position $position, resume: $resume")
                    val result = bg { database.resumeDAO().deleteResume(resume) }
                    result.await()
                    resumeList.removeAt(position)
                    notifyDataSetChanged()
                }
            }
            printResumeButton.setOnClickListener {
                lateinit var educationList : List<Education>
                lateinit var experienceList : List<Experience>
                lateinit var projectsList : List<Project>
                async(UI) {
                    var html = ""
                    val result = bg {
                        educationList = database.educationDAO().getEducationForResume(position.toLong().plus(1))
                        experienceList = database.experienceDAO().getExperienceForResume(position.toLong().plus(1))
                        projectsList = database.projectsDAO().getProjectForResume(position.toLong().plus(1))
                        Crashlytics.log(1, "Resume Adapter", "Lists retrieved for HTML builder: $educationList \n $experienceList \n $projectsList")
                        html = genHtml(resume, educationList, experienceList, projectsList)
                    }
                    result.await()
                    webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
                    createWebPrintJob(webView)
                }
            }
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val resumeName: TextView = view.resumeName
        val personalNameTextView: TextView = view.resumeCardPersonalNameTextView
        val personalEmailTextView: TextView = view.resumeCardPersonalEmailTextView
        val deleteResumeButton: ImageButton = view.resumeDeleteButton
        val printResumeButton: ImageButton = view.resumePrintButton
    }

    fun genHtml(resume : Resume, educationList : List<Education>, experienceList : List<Experience>, projectList : List<Project>) : String {
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

    fun createWebPrintJob(webview: WebView) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webview.createPrintDocumentAdapter()
        val printJob = printManager.print("Resuminator Job", printAdapter, PrintAttributes.Builder().build())
    }
}