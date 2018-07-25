package com.haroldadmin.kshitijchauhan.resumade.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.haroldadmin.kshitijchauhan.resumade.R
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Education
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Experience
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Project
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume
import com.haroldadmin.kshitijchauhan.resumade.utilities.PrintButtonClickListener
import com.haroldadmin.kshitijchauhan.resumade.utilities.ResumeCardClickListener
import kotlinx.android.synthetic.main.card_resume.view.*

class ResumeAdapter(val resumeCardClickListener: ResumeCardClickListener,
                    val printButtonClickListener: PrintButtonClickListener) : RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {

	var resumesList : List<Resume> = emptyList()

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

	inner class ResumeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
		val resumeCard : CardView = itemView.resumeCard
		val resumeNameTextView : TextView = itemView.resumeNameTextView
		val personNameTextView : TextView = itemView.personNameTextView
		val personPhoneTextView : TextView = itemView.personPhoneTextView
		val personEmailTextView : TextView = itemView.personEmailTextView
		val printResumeButton : ImageButton = itemView.printResumeButton

		fun bindClickListener() {
			val resumeId = resumesList[adapterPosition].id
			resumeCard.setOnClickListener {
				resumeCardClickListener.onResumeCardClick(resumeId)
			}
			printResumeButton.setOnClickListener {
				printButtonClickListener.onPrintButtonClick(resumeId)
			}
		}
	}

	fun getResumeAtPosition(position: Int) = resumesList[position]

}