package com.haroldadmin.kshitijchauhan.resuminator.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.haroldadmin.kshitijchauhan.resuminator.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.adapters.ExperienceAdapter
import com.haroldadmin.kshitijchauhan.resuminator.data.Experience
import kotlinx.android.synthetic.main.experience_fragment.*

class ExperienceFragment : Fragment() {

    private lateinit var experienceAdapter: ExperienceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.experience_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        experienceAdapter = ExperienceAdapter(CreateResumeActivity.experienceList, context)
        val animation = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)
        experienceRecyclerView.layoutManager = LinearLayoutManager(this.context)
        experienceRecyclerView.adapter = experienceAdapter
        experienceRecyclerView.layoutAnimation = animation
        addExperience(CreateResumeActivity.resumeId)
    }

    fun addExperience(resumeId : Long) {
        val position = CreateResumeActivity.experienceList?.size ?: 1
        CreateResumeActivity.experienceList?.add(position, Experience(position.toLong(), "", "", "", resumeId))
        experienceAdapter.notifyItemInserted(position)
    }

}