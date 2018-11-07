package com.haroldadmin.kshitijchauhan.resumade.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.utilities.ResumeCardClickListener

class ResumeAdapter(val resumeCardClickListener: ResumeCardClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {

	private var resumesList : List<Resume> = emptyList()

	override fun onCreateViewHolder(parent : ViewGroup, position : Int): ResumeViewHolder = ResumeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_resume, parent, false))

	override fun getItemCount(): Int = resumesList.size

	override fun onBindViewHolder(holder : ResumeViewHolder, position : Int) {
		val resume = resumesList[position]
		holder.apply {
			resumeNameTextView.text = resume.resumeName
			personNameTextView.text = resume.name
			personPhoneTextView.text = resume.phone
			personEmailTextView.text = resume.email
			bindClickListener()
		}
	}

	inner class ResumeViewHolder(itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
		val resumeCard : View = itemView.findViewById(R.id.resumeItemView)
		val resumeNameTextView : TextView = itemView.findViewById(R.id.resumeNameTextView)
		val personNameTextView : TextView = itemView.findViewById(R.id.personalNameTextView)
		val personPhoneTextView : TextView = itemView.findViewById(R.id.personalPhoneTextView)
		val personEmailTextView : TextView = itemView.findViewById(R.id.personalEmailTextView)

		fun bindClickListener() {
			val resumeId = resumesList[adapterPosition].id
			resumeCard.setOnClickListener {
				resumeCardClickListener.onResumeCardClick(resumeId)
			}
		}
	}

	fun getResumeAtPosition(position: Int) = resumesList[position]

	fun updateResumesList(newResumesList : List<Resume>) {
		val resumeDiffUtilCallback = DiffUtilCallback(this.resumesList, newResumesList)
		val diffResult = DiffUtil.calculateDiff(resumeDiffUtilCallback)
		this.resumesList = newResumesList
		diffResult.dispatchUpdatesTo(this)
	}

}