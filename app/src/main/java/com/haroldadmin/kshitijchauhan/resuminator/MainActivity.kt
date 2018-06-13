package com.haroldadmin.kshitijchauhan.resuminator

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import com.crashlytics.android.Crashlytics
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity.Companion.resumeId
import com.haroldadmin.kshitijchauhan.resuminator.adapters.ResumeAdapter
import com.haroldadmin.kshitijchauhan.resuminator.data.Resume
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import com.haroldadmin.kshitijchauhan.resuminator.utilities.SavedState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

class MainActivity : AppCompatActivity() {

    private val LOG_TAG = MainActivity::class.java.simpleName
    lateinit var database : ResumeDatabase
    private var numberOfResume : Int? = 0
    private var resumeList : MutableList<Resume>? = mutableListOf()
    private lateinit var resumeAdapter : ResumeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Crashlytics.log(0, LOG_TAG, "Main acitivity created")

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        collapsingToolbar.title = "Resumade"

        database = ResumeDatabase.getInstance(this)!!

        async(UI) {
            var result = bg { database.resumeDAO().getAllResume() }
            resumeList = result.await()
            Crashlytics.log(2, LOG_TAG, "Resumes retrieved from database: \n $resumeList")
            with(resumeList) {
                numberOfResume = this?.size
                if (numberOfResume!! > 0) { noResumesTextView.visibility = View.GONE }
                resumeAdapter = ResumeAdapter(resumeList!!, this@MainActivity)
                resumesRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                resumesRecyclerView.adapter = resumeAdapter
                resumesRecyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(this@MainActivity, R.anim.layout_animation_fall_down)
            }
        }
        fab.setOnClickListener {
            SavedState.isSaved = false
            val intent = Intent(this, CreateResumeActivity::class.java)
            async(UI) {
                // This resume DAO method returns the ID of the last resume inserted
                val result = bg { database.resumeDAO().getNewResumeId() }
                resumeId = result.await()
                // New resume should have an ID one greater than the last resume
                resumeId++
                Crashlytics.log(1, LOG_TAG, "New resumeId = $resumeId")
                intent.putExtra("ResumeID", resumeId)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
            R.id.deleteAllResume -> {
                async(UI) {
                    val task = bg {
                        database.clearAllTables()
                        resumeList?.clear()
                    }
                    task.await()
                    resumeAdapter.notifyDataSetChanged()
                    resumesRecyclerView.visibility = View.INVISIBLE
                    noResumesTextView.visibility = View.VISIBLE
                }
                    return true
                }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {

        super.onResume()
        Crashlytics.log(0, LOG_TAG, "Main Activity resumed")
        Crashlytics.log(1, LOG_TAG, "(MA onResume) Number of resume = $numberOfResume")

        if(SavedState.isSaved) {
            Crashlytics.log(1, LOG_TAG, "New resume added!")
            // Resetting Saved State
            SavedState.isSaved = false
            async(UI) {
                val result = bg {
                    val newResume = database.resumeDAO().getResumeForId(resumeId)
                    Crashlytics.log(2, LOG_TAG, "New resume retrieved : $newResume")
                    resumeList?.add(newResume)
                    numberOfResume?.plus(1)
                }
                result.await()
                Crashlytics.log(1, LOG_TAG, "Notifying resume adapter of new resume")
                resumeAdapter.notifyItemInserted(resumeId.minus(1).toInt())
                // If the user proceeds to add another resume, the resumeID should be incremented by one.
                resumeId++
                /* In the case where there were no resumes when the app started,
                the noResumesTextView would have stayed visible. But now that have at least one resume,
                We can set its visibility to gone.
                 */
                noResumesTextView.visibility = View.GONE
                resumesRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Crashlytics.log(0, LOG_TAG, "Main Activity destroyed")
    }

}