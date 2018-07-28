package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.FragmentAdapter
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.MainActivity.Companion.EXTRA_RESUME_ID
import com.haroldadmin.kshitijchauhan.resumade.ui.fragments.PreviewFragment
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.activity_create_resume.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat

class CreateResumeActivity : AppCompatActivity() {

	private val TAG: String = this::class.java.simpleName
	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var resumeFragmentAdapter: FragmentAdapter
	private lateinit var createResumeFab: FloatingActionButton
	private lateinit var viewPager : ViewPager
	private lateinit var webView: WebView
	private var addIcon: Drawable? = null
	private var doneIcon: Drawable? = null

	companion object {
		var currentResumeId: Long = -1L
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_resume)

		setSupportActionBar(createResumeToolbar)
		supportActionBar?.title = resources.getString(R.string.createResumeTitle)

		createResumeFab = findViewById(R.id.createResumeFab)
		viewPager = findViewById(R.id.createResumeViewpager)
		addIcon = ContextCompat.getDrawable(this, R.drawable.ic_round_add_24px)
		doneIcon = ContextCompat.getDrawable(this, R.drawable.ic_round_done_24px)

		val intent = intent
		if (intent != null && intent.hasExtra(EXTRA_RESUME_ID)) {
			/*
			This means that we are currently working with
			an existing resume in the database
			 */
			currentResumeId = intent.getLongExtra(EXTRA_RESUME_ID, -1L)
		}

		createResumeViewModel = ViewModelProviders
				.of(this)
				.get(CreateResumeViewModel::class.java)

		resumeFragmentAdapter = FragmentAdapter(supportFragmentManager)
		viewPager.adapter = resumeFragmentAdapter
		viewPager.offscreenPageLimit = 4
		createResumeTabs.setupWithViewPager(createResumeViewpager)
		createResumeFab.hide()

		viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
			override fun onPageScrollStateChanged(p0: Int) {
				// Do nothing
			}

			override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
				// Do nothing
			}

			override fun onPageSelected(position: Int) {
				adjustFabBehaviour(position)
			}
		})

	}

	override fun onBackPressed() {
		/*
		We use the value of saved from the ViewModel
		because the value inside the activity is destroyed
		on every configuration change. It should only be used
		once: while initializing the viewmodel.
		 */
		if (!createResumeViewModel.saved) {
			// Using Appcompat to ensure that the dialogs don't look weird
			alert(Appcompat, "You haven't saved your resume yet") {
				title = "Unsaved Resume"
				positiveButton("Cancel") { /* Do nothing */ }
				negativeButton("Delete") {
					createResumeViewModel.deleteTempResume()
					super.onBackPressed()
				}
			}.show()
		} else {
			super.onBackPressed()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_create_resume_activity, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return when (item?.itemId) {
			R.id.done -> run {
				if (checkIfDetailsSaved()) {
					finish()
					createResumeViewModel.saved = true
				}
				return true
			}
			R.id.print -> run {
				webView = WebView(this)
				webView.loadDataWithBaseURL(null, PreviewFragment.html, "text/html", "UTF-8", null)
				createWebPrintJob(webView)
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	fun adjustFabBehaviour(position: Int) {
		when (position) {
			0 -> fabBehaviourPersonalFragment()
			1 -> fabBehaviourEducationFragment()
			2 -> fabBehaviourExperienceFragment()
			3 -> fabBehaviourProjectFragment()
			4 -> fabBehaviourPreviewFragment()
		}
	}

	private fun fabBehaviourPersonalFragment() {
		createResumeFab.hide()
	}

	private fun fabBehaviourEducationFragment() {
		createResumeFab.apply {
			hide()
			setImageDrawable(addIcon)
			show()
			setOnClickListener {
				createResumeViewModel.apply {
					insertBlankEducation()
					educationDetailsSaved = false
				}
			}
		}
	}

	private fun fabBehaviourExperienceFragment() {
		createResumeFab.apply {
			hide()
			setImageDrawable(addIcon)
			show()
			setOnClickListener {
				createResumeViewModel.apply {
					insertBlankExperience()
					experienceDetailsSaved = false
				}
			}
		}
	}

	private fun fabBehaviourProjectFragment() {
		createResumeFab.apply {
			hide()
			setImageDrawable(addIcon)
			show()
			setOnClickListener {
				createResumeViewModel.apply {
					insertBlankProject()
					projectDetailsSaved = false
				}
			}
		}
	}

	private fun fabBehaviourPreviewFragment() {
		createResumeFab.apply {
			hide()
		}
	}

	fun displaySnackbar(text: String) {
		Snackbar.make(rootCoordinatorLayout, text, Snackbar.LENGTH_SHORT).show()
	}

	private fun createWebPrintJob(webview: WebView) {
		val printManager = this.getSystemService(Context.PRINT_SERVICE) as PrintManager
		val printAdapter = webview.createPrintDocumentAdapter("Resume Document")
		val printJob = printManager.print("Resumade Job", printAdapter, PrintAttributes.Builder().build())
	}

	private fun checkIfDetailsSaved(): Boolean {
		with(createResumeViewModel) {
			if (!personalDetailsSaved) {
				viewPager.setCurrentItem(0, true)
				Snackbar.make(rootCoordinatorLayout, "Personal details unsaved", Snackbar.LENGTH_SHORT).show()
				return false
			}
			if (!educationDetailsSaved) {
				viewPager.setCurrentItem(1, true)
				Snackbar.make(rootCoordinatorLayout, "Education details unsaved", Snackbar.LENGTH_SHORT).show()
				return false
			}
			if (!experienceDetailsSaved) {
				viewPager.setCurrentItem(2, true)
				Snackbar.make(rootCoordinatorLayout, "Experience details unsaved", Snackbar.LENGTH_SHORT).show()
				return false
			}
			if (!projectDetailsSaved) {
				viewPager.setCurrentItem(3, true)
				Snackbar.make(rootCoordinatorLayout, "Project details unsaved", Snackbar.LENGTH_SHORT).show()
				return false
			}
			return true
		}
	}
}

