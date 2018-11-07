package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ResumeAdapter
import com.haroldadmin.kshitijchauhan.resumade.adapter.SwipeToDeleteCallback
import com.haroldadmin.kshitijchauhan.resumade.utilities.*
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

class MainActivity : AppCompatActivity(), ResumeCardClickListener {

	private val TAG = this::class.java.simpleName
	private lateinit var mainViewModel: MainViewModel
	private lateinit var resumeAdapter: ResumeAdapter
	private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
	private lateinit var resumesRecyclerView: androidx.recyclerview.widget.RecyclerView
	private lateinit var webView: WebView


	companion object {
		const val EXTRA_RESUME_ID: String = "resumeId"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		setSupportActionBar(mainActivityToolbar)
		collapsingToolbarLayout.title = resources.getString(R.string.app_name)

		mainViewModel = ViewModelProviders
				.of(this)
				.get(MainViewModel::class.java)

		resumesRecyclerView = findViewById(R.id.resumesListRecyclerView)
		webView = WebView(this)

		setupRecyclerView()

		mainViewModel.resumesList
				.observe(this, Observer {
					resumeAdapter.updateResumesList(it ?: emptyList())
					toggleNoResumesLayout(it?.size ?: 0)
				})

		addResumeFab.setOnClickListener {
			val newResumeId: Long = -1
			val intent = Intent(this, CreateResumeActivity::class.java)
			intent.putExtra(EXTRA_RESUME_ID, newResumeId)
			startActivity(intent)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_main_activity, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return when (item?.itemId) {
			R.id.about -> {
				startActivity(Intent(this, AboutUsActivity::class.java))
				true
			}
			else -> super.onOptionsItemSelected(item)
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

	private fun toggleNoResumesLayout(size: Int) {
		if (size > 0) {
			resumesListRecyclerView.visibility = View.VISIBLE
			noResumesView.visibility = View.INVISIBLE
		} else {
			resumesListRecyclerView.visibility = View.INVISIBLE
			noResumesView.visibility = View.VISIBLE
			mainActivityAppbarLayout.setExpanded(true, true)
		}
	}

	private fun setupRecyclerView() {
		resumeAdapter = ResumeAdapter(this)
		linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
		val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(resumesRecyclerView.context, linearLayoutManager.orientation)
		dividerItemDecoration.setDrawable(this.getDrawable(R.drawable.list_divider))
		resumesRecyclerView.apply {
			adapter = resumeAdapter
			layoutManager = linearLayoutManager
			addItemDecoration(dividerItemDecoration)
		}
		val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(this) {
			override fun onSwiped(viewholder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
				val position = viewholder.adapterPosition
				val id: Long = resumeAdapter.getResumeAtPosition(position).id
				if (direction == ItemTouchHelper.LEFT) {
					alert(Appcompat, "Are you sure you want to delete this resume?") {
						yesButton {
							mainViewModel.deleteResume(resumeAdapter.getResumeAtPosition(position))
						}
						noButton {
							resumeAdapter.notifyItemChanged(position)
						}
					}.show()
				} else {
					AppExecutors.diskIO.execute {
						val resume = mainViewModel.getResumeForId(id)
						val educationList = mainViewModel.getEducationForResume(id)
						val experienceList = mainViewModel.getExperienceForResume(id)
						val projectList = mainViewModel.getProjectForResume(id)
						AppExecutors.backgroundProcessor.execute {
							val html = buildHtml(resume, educationList, experienceList, projectList)
							AppExecutors.mainThreadExecutor.execute {
								webView = WebView(this@MainActivity)
								webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
								webView.createPrintJob(this@MainActivity)
								resumeAdapter.notifyItemChanged(position)
							}
						}
					}
				}
			}
		})
		itemTouchHelper.attachToRecyclerView(resumesRecyclerView)
	}
}
