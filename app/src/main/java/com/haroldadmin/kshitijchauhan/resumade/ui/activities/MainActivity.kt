package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ResumeAdapter
import com.haroldadmin.kshitijchauhan.resumade.adapter.SwipeToDeleteCallback
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.utilities.AppDispatchers
import com.haroldadmin.kshitijchauhan.resumade.utilities.ResumeCardClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.buildHtml
import com.haroldadmin.kshitijchauhan.resumade.utilities.createPrintJob
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), ResumeCardClickListener, CoroutineScope {

    private val mainActivityJob = Job()
    override val coroutineContext = Dispatchers.Main + mainActivityJob

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
        val span = SpannableString("About")
        span.setSpan(ForegroundColorSpan(Color.BLACK), 0, span.length, 0)
        menu?.getItem(0)?.title = span
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
                    AlertDialog.Builder(ContextThemeWrapper(this@MainActivity, R.style.MyAlertDialog))
                            .setMessage("Are you sure you want to delete this resume?")
                            .setPositiveButton("Yes") { _, _ ->
                                mainViewModel.deleteResume(resumeAdapter.getResumeAtPosition(position))
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                resumeAdapter.notifyItemChanged(position)
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                } else {
                    launch {
                        lateinit var resume: Resume
                        lateinit var educationList: List<Education>
                        lateinit var experienceList: List<Experience>
                        lateinit var projectList: List<Project>
                        lateinit var html: String

                        withContext(AppDispatchers.diskDispatcher) {
                            resume = mainViewModel.getResumeForId(id)
                            educationList = mainViewModel.getEducationForResume(id)
                            experienceList = mainViewModel.getExperienceForResume(id)
                            projectList = mainViewModel.getProjectForResume(id)
                        }
                        withContext(AppDispatchers.computationDispatcher) {
                            html = buildHtml(resume, educationList, experienceList, projectList)
                        }

                        webView = WebView(this@MainActivity)
                        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                        webView.createPrintJob(this@MainActivity)
                        resumeAdapter.notifyItemChanged(position)
                    }
                }
            }
    })
    itemTouchHelper.attachToRecyclerView(resumesRecyclerView)
}
}
