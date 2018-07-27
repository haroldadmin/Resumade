package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ResumeAdapter
import com.haroldadmin.kshitijchauhan.resumade.utilities.ResumeCardClickListener
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ResumeCardClickListener {

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
		collapsingToolbarLayout.title = resources.getString(R.string.app_name)

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

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_main_activity, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return when(item?.itemId) {
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

	private fun toggleNoResumesLayout(size : Int) {
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
}
