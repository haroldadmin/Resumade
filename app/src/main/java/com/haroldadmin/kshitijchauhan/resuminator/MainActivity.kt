package com.haroldadmin.kshitijchauhan.resuminator

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity.Companion.resumeId
import com.haroldadmin.kshitijchauhan.resuminator.adapters.ResumeAdapter
import com.haroldadmin.kshitijchauhan.resuminator.data.Resume
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import com.haroldadmin.kshitijchauhan.resuminator.utilities.SavedState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    private val LOG_TAG = MainActivity::class.java.simpleName

    val mainActivityContext = this
    var numberOfResume : Int? = 0
    lateinit var database : ResumeDatabase
    var resumeList : MutableList<Resume>? = mutableListOf()
    lateinit var resumeAdapter : ResumeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "Main Activity Created")

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        collapsingToolbar.title = "Resuminator"

        database = ResumeDatabase.getInstance(this)!!

        val animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down)

        launch(UI) {

            async {
                resumeList = database.resumeDAO().getAllResume()
            }.await()

            numberOfResume = resumeList?.size

            if (numberOfResume!! > 0) {
                noResumesTextView.visibility = View.GONE
            }

            resumeAdapter = ResumeAdapter(resumeList, mainActivityContext)
            resumesRecyclerView.layoutManager = LinearLayoutManager(mainActivityContext)
            resumesRecyclerView.adapter = resumeAdapter
            resumesRecyclerView.layoutAnimation = animation
        }

        fab.setOnClickListener {
            SavedState.isSaved = false
            val intent = Intent(this, CreateResumeActivity::class.java)
            // The ID to use for the new Resume being created
            launch(UI) {
                async {
                    resumeId = database.resumeDAO().getNewResumeId() + 1
                }.await()
                intent.putExtra("ResumeID", resumeId)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "Main Activity resumed")
        Log.d(LOG_TAG, "(MA onResume) Number of resume = $numberOfResume")

        if (SavedState.isSaved) {
            Log.d(LOG_TAG, "New resume added!")

            launch(UI) {
                async {
                    resumeList?.add(database.resumeDAO().getResumeForId(resumeId))
                    numberOfResume?.plus(1)
                }.await()
                resumeAdapter.notifyDataSetChanged()
                // If the user proceeds to add another resume, the ID should be updated
                resumeId++
                noResumesTextView.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "Main activity destroyed")
    }

}