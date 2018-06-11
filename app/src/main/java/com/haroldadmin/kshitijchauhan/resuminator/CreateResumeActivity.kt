package com.haroldadmin.kshitijchauhan.resuminator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.haroldadmin.kshitijchauhan.resuminator.adapters.ResumeFragmentsAdapter
import com.haroldadmin.kshitijchauhan.resuminator.data.*
import com.haroldadmin.kshitijchauhan.resuminator.fragments.EducationFragment
import com.haroldadmin.kshitijchauhan.resuminator.fragments.ExperienceFragment
import com.haroldadmin.kshitijchauhan.resuminator.fragments.PersonalFragment
import com.haroldadmin.kshitijchauhan.resuminator.fragments.ProjectsFragment
import com.haroldadmin.kshitijchauhan.resuminator.utilities.SavedState
import kotlinx.android.synthetic.main.activity_create_resume.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class CreateResumeActivity : AppCompatActivity() {

    companion object {
        var resumeId: Long = 1
        var educationList: MutableList<Education>? = mutableListOf()
        var experienceList: MutableList<Experience>? = mutableListOf()
        var projectsList: MutableList<Project>? = mutableListOf()
        lateinit var tempResume : Resume
    }

    private val LOG_TAG = CreateResumeActivity::class.java.simpleName
    lateinit var fragmentAdapter: ResumeFragmentsAdapter
    lateinit var database: ResumeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d(LOG_TAG, "CR activity created")
        super.onCreate(savedInstanceState)
        database = ResumeDatabase.getInstance(this)!!

        resumeId = intent.getLongExtra("ResumeID", 1000)
        Log.d(LOG_TAG, "(CR onCreate) Creating a new resume with id = $resumeId")
        createTempResume(resumeId)

        setContentView(R.layout.activity_create_resume)
        setSupportActionBar(createResumeToolbar)

        fragmentAdapter = ResumeFragmentsAdapter(supportFragmentManager)
        createResumeViewpager.adapter = fragmentAdapter
        createResumeViewpager.offscreenPageLimit = 4 // I'm sorry, but I hate that slight hiccup when switching pages
        createResumeTabs.setupWithViewPager(createResumeViewpager)

        addToViewPager(PersonalFragment(), "Personal")
        setUpRemainingFragments(resumeId)

        createResumeViewpager.addOnPageChangeListener(
                object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {}
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        adjustFabBehaviour(createResumeViewpager, position)
                    }
                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.create_resume_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.generate_resume) {
            SavedState.isSaved = true
            finish()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "CR destroyed")
        if (!SavedState.isSaved) {
            Log.d(LOG_TAG, "Deleting Temporary Resume")
            deleteTempResume()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "CR activity paused")
    }

    override fun onStop() {
        super.onStop()
        Log.d(LOG_TAG, "CR activity stopped")
    }

    override fun onResume() {
        super.onResume()
        createResumeFab.visibility = View.INVISIBLE // To make sure FAB isn't visible in the personal fragment
        Log.d(LOG_TAG, "CR activity resumed")
    }

    fun setUpRemainingFragments(resumeId: Long) {
        launch(UI) {
            async {
                educationList = database.educationDAO().getEducationForResume(resumeId)
                experienceList = database.experienceDAO().getExperienceForResume(resumeId)
                projectsList = database.projectsDAO().getProjectForResume(resumeId)
            }.await()
            addToViewPager(EducationFragment(), "Education")
            addToViewPager(ExperienceFragment(), "Experience")
            addToViewPager(ProjectsFragment(), "Projects")
        }
    }

    fun addToViewPager(fragment: Fragment, title: String) {
        fragmentAdapter.addFragment(fragment, title)
        fragmentAdapter.notifyDataSetChanged()
    }

    private fun adjustFabBehaviour(viewPager: ViewPager, position: Int) {
        //Adjust visibility of FAB and onClick behaviour according to the Fragment being displayed.
        when (position) {
            0 -> fabBehaviourPersonal(createResumeFab)
            1 -> fabBehaviourEducation(createResumeFab, viewPager)
            2 -> fabBehaviourExperience(createResumeFab, viewPager)
            3 -> fabBehaviourProjects(createResumeFab, viewPager)
        }
    }

    private fun fabBehaviourPersonal(fab: FloatingActionButton) {
        Log.d(LOG_TAG, "FAB in Personal Fragment Mode")
        fab.animate().alpha(0.0f).setDuration(200)
        fab.clearAnimation()
        fab.visibility = View.INVISIBLE
    }

    private fun fabBehaviourEducation(fab: FloatingActionButton, vp: ViewPager) {
        Log.d(LOG_TAG, "FAB in Education Fragment Mode")
        fab.animate().alpha(1.0f).setDuration(200)
        fab.clearAnimation()
        fab.visibility = View.VISIBLE
        fab.setOnClickListener {
            //This is a hack to access an instance of the current fragment from Fragment Manager.
            //Stack Overflow link : https://stackoverflow.com/questions/18609261/getting-the-current-fragment-instance-in-the-viewpager?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
            val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createResumeViewpager + ":" + vp.currentItem) as EducationFragment
            fragment.addEducation(resumeId)
        }
    }

    private fun fabBehaviourExperience(fab: FloatingActionButton, vp: ViewPager) {
        fab.animate().alpha(1.0f).setDuration(200)
        fab.clearAnimation()
        fab.visibility = View.VISIBLE
        fab.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createResumeViewpager + ":" + vp.currentItem) as ExperienceFragment
            fragment.addExperience(resumeId)
        }
    }

    private fun fabBehaviourProjects(fab: FloatingActionButton, vp: ViewPager) {
        fab.animate().alpha(1.0f).setDuration(200)
        fab.clearAnimation()
        fab.visibility = View.VISIBLE
        fab.setOnClickListener {
            val fragment = supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.createResumeViewpager + ":" + vp.currentItem) as ProjectsFragment
            fragment.addProject(resumeId)
        }
    }

    private fun createTempResume(resumeId: Long) {
        launch {
            tempResume = Resume(resumeId, name = "", phone = "", email = "", currentCity = "", skills =  "", hobbies = "", description = "")
            database.resumeDAO().insertResume(tempResume)
        }
    }

    private fun deleteTempResume() {
        launch {
            database.resumeDAO().deleteResume(tempResume)
        }
    }
}
