package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ResumeAdapter
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.utilities.*
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ResumeCardClickListener, PrintButtonClickListener {

	private val TAG = this::class.java.simpleName
	private lateinit var mainViewModel : MainViewModel
	private lateinit var resumeAdapter: ResumeAdapter
	private lateinit var resumesRecyclerView: RecyclerView
	private lateinit var webView: WebView

	companion object {
		const val EXTRA_RESUME_ID : String = "resumeId"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setSupportActionBar(mainActivityToolbar)
		collapsingToolbarLayout.title = "Resumade"

		mainViewModel = ViewModelProviders
				.of(this)
				.get(MainViewModel::class.java)

		resumesRecyclerView = findViewById(R.id.resumesListRecyclerView)
		webView = WebView(this)

		setupRecyclerView()

		mainViewModel.resumesList
				.observe(this, Observer {
					resumeAdapter.resumesList = it ?: emptyList()
					resumeAdapter.notifyDataSetChanged()
					toggleNoResumesLayout(it?.size ?: 0)
				})

		addResumeFab.setOnClickListener {
			val newResumeId  : Long = -1
			val intent = Intent(this, CreateResumeActivity::class.java)
			intent.putExtra(EXTRA_RESUME_ID, newResumeId)
			startActivity(intent)
		}
	}

	override fun onResumeCardClick(resumeId: Long) {
		/*
		This method would be invoked by the adapter, which
		would know the resumeId of the resume being clicked on
		 */
		val intent = Intent(this, CreateResumeActivity::class.java)
		intent.putExtra(EXTRA_RESUME_ID, resumeId)
		startActivity(intent)
	}

	override fun onPrintButtonClick(resumeId: Long) {
		mainViewModel.getListsAndResume(resumeId)
		val html = genHtml(mainViewModel.resume, mainViewModel.educationList, mainViewModel.experienceList, mainViewModel.projectList)
		webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
		createWebPrintJob(webView)
	}

	private fun toggleNoResumesLayout(size : Int) {
		if (size > 0) {
			resumesListRecyclerView.visibility = View.VISIBLE
			noResumesTextView.visibility = View.INVISIBLE
		} else {
			resumesListRecyclerView.visibility = View.INVISIBLE
			noResumesTextView.visibility = View.VISIBLE
		}
	}

	private fun setupRecyclerView() {
		resumeAdapter = ResumeAdapter(this, this)
		resumesRecyclerView.apply {
			adapter = resumeAdapter
			layoutManager = LinearLayoutManager(this@MainActivity)
			layoutAnimation = AnimationUtils.loadLayoutAnimation(this@MainActivity, R.anim.layout_animation_fall_down)
		}

		/*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
		ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

			override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
				return false
			}

			// Called when a user swipes left or right on a ViewHolder
			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
				mainViewModel.deleteResume(resumeAdapter.getResumeAtPosition(viewHolder.adapterPosition))
			}
		}).attachToRecyclerView(resumesListRecyclerView)
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
		val printManager = this.getSystemService(Context.PRINT_SERVICE) as PrintManager
		val printAdapter = webview.createPrintDocumentAdapter("Resume Document")
		val printJob = printManager.print("Resumade Job", printAdapter, PrintAttributes.Builder().build())
	}
}
