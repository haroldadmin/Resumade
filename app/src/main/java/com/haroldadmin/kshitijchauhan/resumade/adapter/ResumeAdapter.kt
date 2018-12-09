package com.haroldadmin.kshitijchauhan.resumade.adapter

import androidx.recyclerview.widget.DiffUtil
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haroldadmin.kshitijchauhan.resumade.databinding.CardResumeBinding
import com.haroldadmin.kshitijchauhan.resumade.repository.database.Resume

class ResumeAdapter(val onResumeCardClick: (resumeId: Long) -> Unit) : RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {

    private var resumesList: List<Resume> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ResumeViewHolder {
        val binding = CardResumeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResumeViewHolder(binding)
    }

    override fun getItemCount(): Int = resumesList.size

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val resume = resumesList[position]
        holder.apply {
            binding.resume = resume
            binding.root.setOnClickListener { onResumeCardClick(resumesList[adapterPosition].id) }
        }
    }

    inner class ResumeViewHolder(val binding: CardResumeBinding) : RecyclerView.ViewHolder(binding.root)

    fun getResumeAtPosition(position: Int) = resumesList[position]

    fun updateResumesList(newResumesList: List<Resume>) {
        val resumeDiffUtilCallback = DiffUtilCallback(this.resumesList, newResumesList)
        val diffResult = DiffUtil.calculateDiff(resumeDiffUtilCallback)
        this.resumesList = newResumesList
        diffResult.dispatchUpdatesTo(this)
    }
}