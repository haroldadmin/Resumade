package com.haroldadmin.kshitijchauhan.resuminator.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.haroldadmin.kshitijchauhan.resuminator.R
import com.haroldadmin.kshitijchauhan.resuminator.data.Resume
import com.haroldadmin.kshitijchauhan.resuminator.data.ResumeDatabase
import kotlinx.android.synthetic.main.resume_card.view.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class ResumeAdapter(var resumeList : MutableList<Resume>?, val context : Context) : RecyclerView.Adapter<ResumeAdapter.ViewHolder>() {

    val database = ResumeDatabase.getInstance(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.resume_card, parent, false))
    }

    override fun getItemCount() = resumeList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resume = resumeList?.get(position)
        with(holder) {
            resumeName.text = resume?.resumeName
            personalNameTextView.text = resume?.name
            personalEmailTextView.text = resume?.email
            deleteResumeButton.setOnClickListener {
                launch(UI) {
                    async {
                        database?.resumeDAO()?.deleteResume(resume!!)
                        resumeList?.removeAt(position)
                    }.await()
                    notifyItemRemoved(position)
                }
            }
            printResumeButton.setOnClickListener { Toast.makeText(context, "That button doesn't work yet", Toast.LENGTH_SHORT).show() }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resumeName = view.resumeName
        val personalNameTextView = view.resumeCardPersonalNameTextView
        val personalEmailTextView = view.resumeCardPersonalEmailTextView
        val deleteResumeButton = view.resumeDeleteButton
        val printResumeButton = view.resumePrintButton
    }
}