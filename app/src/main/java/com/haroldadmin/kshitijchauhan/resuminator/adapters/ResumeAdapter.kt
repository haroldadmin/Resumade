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
import android.widget.Toast
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity.Companion.educationList
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity.Companion.experienceList
import com.haroldadmin.kshitijchauhan.resuminator.utilities.*
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.*
import com.haroldadmin.kshitijchauhan.resuminator.utilities.*
import kotlinx.android.synthetic.main.resume_card.view.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

class ResumeAdapter(var resumeList: MutableList<Resume>?, val context: Context) : RecyclerView.Adapter<ResumeAdapter.ViewHolder>() {

    val database = ResumeDatabase.getInstance(context)
    val webView = WebView(context)
    var html = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.resume_card, parent, false))
    }

    override fun getItemCount() = resumeList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resume = resumeList?.get(position)
        with(holder) {
            resumeName.text = resume?.resumeName
            personalNameTextView.text = resume?.name
            personalEmailTextView.text = resume?.email
            deleteResumeButton.setOnClickListener {
                launch(UI) {
                    async {
                        database?.resumeDAO()?.deleteResume(resume!!)
                        resumeList?.removeAt(position)
                    }.await()
                    notifyItemRemoved(position)
                }
            }
            printResumeButton.setOnClickListener {
                lateinit var educationList: List<Education>
                lateinit var experienceList: List<Experience>
                lateinit var projectList: List<Project>
                launch(UI) {
                    educationList = async { getEducationList(resume?.id!!) }.await()
                    println(educationList)
                    experienceList = async { getExperienceList(resume?.id!!) }.await()
                    println(experienceList)
                    projectList = async { getProjectsList(resume?.id!!) }.await()
                    println(projectList)
                    println("Creating html file...")
                    html = createBaseHTML()
                    println(html)
                    html += addPersonalInfo(resume!!)
                    println(html)
                    html += addEducationInfo(educationList)
                    println(html)
                    html += addExperienceInfo(experienceList)
                    println(html)
                    html += addProjectInfo(projectList)
                    println(html)
                    html += addSkills(resume)
                    println(html)
                    html += addHobbies(resume)
                    println(html)
                    html += closeHtmlFile()
                    Log.d("Resume Adapter", "Html File created: ${html}")
                    //                    Log.d("Resume Adapter", "Html File created: $html")
                    println("Creating webview")
                    webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
                    createWebPrintJob(webView)
                }
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resumeName: TextView = view.resumeName
        val personalNameTextView: TextView = view.resumeCardPersonalNameTextView
        val personalEmailTextView: TextView = view.resumeCardPersonalEmailTextView
        val deleteResumeButton: ImageButton = view.resumeDeleteButton
        val printResumeButton: ImageButton = view.resumePrintButton
    }

    fun createWebPrintJob(webview: WebView) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webview.createPrintDocumentAdapter()
        val printJob = printManager.print("Resuminator Job", printAdapter, PrintAttributes.Builder().build())
    }

    suspend fun getEducationList(resumeId: Long): List<Education> {
        return database?.educationDAO()?.getEducationForResume(resumeId) as List<Education>
    }

    suspend fun getExperienceList(resumeId: Long): List<Experience> {
        return database?.experienceDAO()?.getExperienceForResume(resumeId) as List<Experience>
    }

    suspend fun getProjectsList(resumeId: Long): List<Project> {
        return database?.projectsDAO()?.getProjectForResume(resumeId) as List<Project>
    }
}