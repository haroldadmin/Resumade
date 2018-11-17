package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.FragmentAdapter
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.MainActivity.Companion.EXTRA_RESUME_ID
import com.haroldadmin.kshitijchauhan.resumade.utilities.AppDispatchers
import com.haroldadmin.kshitijchauhan.resumade.utilities.buildHtml
import com.haroldadmin.kshitijchauhan.resumade.utilities.createPrintJob
import com.haroldadmin.kshitijchauhan.resumade.utilities.hideKeyboard
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel
import kotlinx.android.synthetic.main.activity_create_resume.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CreateResumeActivity : AppCompatActivity(), CoroutineScope {

    private val createResumeActivityJob = Job()
    override val coroutineContext = Dispatchers.Main + createResumeActivityJob

    private val TAG: String = this::class.java.simpleName
    private val EXTRA_HTML: String = "html"
    private lateinit var createResumeViewModel: CreateResumeViewModel
    private lateinit var resumeFragmentAdapter: FragmentAdapter
    private lateinit var createResumeFab: FloatingActionButton
    private lateinit var viewPager: androidx.viewpager.widget.ViewPager
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

        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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
        if (!createResumeViewModel.personalDetailsSaved || !createResumeViewModel.educationDetailsSaved || !createResumeViewModel.experienceDetailsSaved || !createResumeViewModel.projectDetailsSaved) {
            AlertDialog.Builder(ContextThemeWrapper(this, R.style.MyAlertDialog))
                    .setTitle("Unsaved Details")
                    .setMessage("Some details remain unsaved. Stay to view them.")
                    .setPositiveButton("Stay") { _, _ ->
                        checkIfDetailsSaved()
                    }
                    .setNegativeButton("Delete") { _, _ ->
                        createResumeViewModel.deleteTempResume()
                        super.onBackPressed()
                    }
                    .create()
                    .show()
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
                this@CreateResumeActivity.hideKeyboard()
                if (checkIfDetailsSaved()) {
                    finish()
                }
                return true
            }
            R.id.print -> run {
                this@CreateResumeActivity.hideKeyboard()
                if (checkIfDetailsSaved()) {
                    launch(AppDispatchers.computationDispatcher) {
                        val html = buildHtml(createResumeViewModel.resume.value!!, createResumeViewModel.educationList.value!!, createResumeViewModel.experienceList.value!!, createResumeViewModel.projectsList.value!!)
                        webView = WebView(this@CreateResumeActivity)
                        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                        webView.createPrintJob(this@CreateResumeActivity)
                    }
                }
                true
            }
            R.id.preview -> run {
                this@CreateResumeActivity.hideKeyboard()
                if (checkIfDetailsSaved()) {
                    launch(AppDispatchers.computationDispatcher) {
                        val html = buildHtml(createResumeViewModel.resume.value!!, createResumeViewModel.educationList.value!!, createResumeViewModel.experienceList.value!!, createResumeViewModel.projectsList.value!!)
                        val intent = Intent(this@CreateResumeActivity, PreviewActivity::class.java)
                        intent.putExtra(EXTRA_HTML, html)
                        startActivity(intent)
                    }
                }
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

    fun displaySnackbar(text: String) {
        Snackbar.make(rootCoordinatorLayout, text, Snackbar.LENGTH_SHORT).show()
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

