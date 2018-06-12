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
import com.haroldadmin.kshitijchauhan.resuminator.adapters.EducationAdapter
import com.haroldadmin.kshitijchauhan.resuminator.data.Education
import com.haroldadmin.kshitijchauhan.resuminator.utilities.afterTextChanged
import kotlinx.android.synthetic.main.education_card.*
import kotlinx.android.synthetic.main.education_fragment.*

class EducationFragment : Fragment() {

    private lateinit var educationAdapter : EducationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.education_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        educationAdapter = EducationAdapter(CreateResumeActivity.educationList, context)
        val animation = AnimationUtils.loadLayoutAnimation(activity, R.anim.layout_animation_fall_down)
        educationRecyclerView.layoutManager = LinearLayoutManager(context)
        educationRecyclerView.adapter = educationAdapter
        educationRecyclerView.layoutAnimation = animation
        addEducation(CreateResumeActivity.resumeId)
    }

    fun addEducation(resumeId : Long) {
        val position = CreateResumeActivity.educationList?.size ?: 1
        CreateResumeActivity.educationList?.add(position, Education(position.toLong(), "", "", "", "", resumeId))
        educationAdapter.notifyItemInserted(position)
    }
}