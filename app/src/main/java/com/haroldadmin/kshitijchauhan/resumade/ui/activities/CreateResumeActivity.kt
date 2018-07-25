package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.FragmentAdapter
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.MainActivity.Companion.EXTRA_RESUME_ID
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.activity_create_resume.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.appcompat.v7.Appcompat

class CreateResumeActivity : AppCompatActivity() {

	private val TAG : String = this::class.java.simpleName
	private lateinit var createResumeViewModel: CreateResumeViewModel
	private lateinit var resumeFragmentAdapter: FragmentAdapter
	private lateinit var createResumeFab : FloatingActionButton

	companion object {
		var currentResumeId : Long = -1L
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_create_resume)

		setSupportActionBar(createResumeToolbar)
		createResumeToolbar.title = "Create Resume"

		createResumeFab = findViewById(R.id.createResumeFab)

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
		createResumeViewpager.adapter = resumeFragmentAdapter
		createResumeViewpager.offscreenPageLimit = 4
		createResumeTabs.setupWithViewPager(createResumeViewpager)
		createResumeFab.hide()

		createResumeViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
			override fun onPageScrollStateChanged(p0: Int) {
				// Do nothing
			}

			override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
				// Do nothing
			}

			override fun onPageSelected(position : Int) {
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
			alert(Appcompat,"You haven't saved your resume yet") {
				title = "Unsaved Resume"
				positiveButton("Cancel") { /* Do nothing */ }
				negativeButton("Delete") {
					createResumeViewModel.deleteTempResume()
					super.onBackPressed()
				}
			}.show()
		} else { super.onBackPressed() }
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_create_resume_activity, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		return when(item?.itemId) {
			R.id.done -> run {
				finish()
				createResumeViewModel.saved = true
				true
				}
			else -> super.onOptionsItemSelected(item)
		}
	}

	fun adjustFabBehaviour(position : Int) {
		when(position) {
			0 -> fabBehaviourPersonalFragment()
			1 -> fabBehaviourEducationFragment()
			2 -> fabBehaviourExperienceFragment()
			3 -> fabBehaviourProjectFragment()
		}
	}

	private fun fabBehaviourPersonalFragment() {
		createResumeFab.hide()
	}

	private fun fabBehaviourEducationFragment() {
		createResumeFab.apply {
			show()
			setOnClickListener {
				createResumeViewModel.insertBlankEducation()
			}
		}
	}

	private fun fabBehaviourExperienceFragment() {
		createResumeFab.apply {
			show()
			setOnClickListener {
				createResumeViewModel.insertBlankExperience()
			}
		}
	}

	private fun fabBehaviourProjectFragment() {
		createResumeFab.apply {
			show()
			setOnClickListener {
				createResumeViewModel.insertBlankProject()
			}
		}
	}
}

