package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.utilities.buildHtml
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel

class PreviewFragment : Fragment() {

	companion object {
		var html : String = ""
	}
	private lateinit var createResumeViewModel: CreateResumeViewModel

	private lateinit var educationList: List<Education>
	private lateinit var experienceList: List<Experience>
	private lateinit var projectsList: List<Project>
	private lateinit var resume: Resume
	private lateinit var webView: WebView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_preview, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		webView = view.findViewById(R.id.previewWebview)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		createResumeViewModel = ViewModelProviders
				.of(activity!!)
				.get(CreateResumeViewModel::class.java)
		resume = Resume("", "", "", "", "", "", "","")
		educationList = emptyList()
		experienceList = emptyList()
		projectsList = emptyList()
	}

	override fun onStart() {
		super.onStart()
		with(createResumeViewModel) {
			resume.observe(this@PreviewFragment, Observer {
				this@PreviewFragment.resume = it ?: Resume("", "", "", "", "", "", "","")
				generateHtml()
				webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
			})
			educationList.observe(this@PreviewFragment, Observer {
				this@PreviewFragment.educationList = it ?: emptyList()
				generateHtml()
				webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
			})
			experienceList.observe(this@PreviewFragment, Observer {
				this@PreviewFragment.experienceList = it ?: emptyList()
				generateHtml()
				webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
			})
			projectsList.observe(this@PreviewFragment, Observer {
				this@PreviewFragment.projectsList = it ?: emptyList()
				generateHtml()
				webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
			})
		}
	}

	private fun generateHtml() {
		html = buildHtml(resume, educationList, experienceList, projectsList)
	}
}