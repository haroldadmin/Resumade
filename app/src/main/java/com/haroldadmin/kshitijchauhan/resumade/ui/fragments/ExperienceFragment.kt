package com.haroldadmin.kshitijchauhan.resumade.ui.fragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.adapter.ExperienceAdapter
import com.haroldadmin.kshitijchauhan.resumade.databinding.FragmentExperienceBinding
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.ui.activities.CreateResumeActivity
import com.haroldadmin.kshitijchauhan.resumade.utilities.*
import com.haroldadmin.kshitijchauhan.resumade.viewmodel.CreateResumeViewModel

class ExperienceFragment : Fragment() {

	private lateinit var experienceAdapter : ExperienceAdapter
	private lateinit var linearLayoutManager : LinearLayoutManager
	private lateinit var createResumeViewModel : CreateResumeViewModel
	private lateinit var binding: FragmentExperienceBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		experienceAdapter = ExperienceAdapter(
				{ item: Experience ->
					// On Save Button Click
					item.saved.set(true)
					createResumeViewModel.apply {
						updateExperience(item)
					}
				},
				{ item: Experience ->
					// On Delete Button Click
					createResumeViewModel.deleteExperience(item)
					(activity as CreateResumeActivity).displaySnackbar("Experience deleted.")
				},
				{ item: Experience ->
					// On Edit Button Click
					item.saved.set(false)
					createResumeViewModel.apply {
						updateExperience(item)
					}
				})
		linearLayoutManager = LinearLayoutManager(context)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = FragmentExperienceBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		activity?.let {
			createResumeViewModel = ViewModelProviders
					.of(it)
					.get(CreateResumeViewModel::class.java)
		}

		createResumeViewModel.experienceList
				.observe(viewLifecycleOwner, Observer {
					experienceAdapter.updateExperienceList(it ?: emptyList())
					createResumeViewModel.experienceDetailsSaved = it == null || it.isEmpty() || it.areAllItemsSaved()
					toggleNoExperienceLayout(it?.size ?: 0)
				})
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.experienceRecyclerView.apply {
			adapter = experienceAdapter
			layoutManager = linearLayoutManager
		}
	}

	private fun toggleNoExperienceLayout(size : Int) {
		binding.apply {
			if (size > 0) {
				experienceRecyclerView.visible()
				noExperienceView.invisible()
			} else {
				experienceRecyclerView.invisible()
				noExperienceView.visible()
			}
		}
	}
}
