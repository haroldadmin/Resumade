package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ResumeAdapter
import com.haroldadmin.kshitijchauhan.resumade.adapter.SwipeToDeleteCallback
import com.haroldadmin.kshitijchauhan.resumade.utilities.ResumeCardClickListener
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
	private lateinit var linearLayoutManager: LinearLayoutManager
	private lateinit var resumesRecyclerView: RecyclerView
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
		linearLayoutManager = LinearLayoutManager(this)
		val dividerItemDecoration = DividerItemDecoration(resumesRecyclerView.context, linearLayoutManager.orientation)
		dividerItemDecoration.setDrawable(this.getDrawable(R.drawable.list_divider))
		resumesRecyclerView.apply {
			adapter = resumeAdapter
			layoutManager = linearLayoutManager
			addItemDecoration(dividerItemDecoration)
		}
		val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(this) {
			override fun onSwiped(viewholder: RecyclerView.ViewHolder, direction: Int) {
				val position = viewholder.adapterPosition
					alert(Appcompat, "Are you sure you want to delete this resume?") {
						yesButton {
							mainViewModel.deleteResume(resumeAdapter.getResumeAtPosition(position))
						}
						noButton {
							resumeAdapter.notifyItemChanged(position)
						}
					}.show()
			}
		})
		itemTouchHelper.attachToRecyclerView(resumesRecyclerView)
	}
}
