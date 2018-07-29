package com.haroldadmin.kshitijchauhan.resumade.utilities

import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity

interface SaveButtonClickListener {
	fun <T : ResumeEntity> onSaveButtonClick(item : T)
}

interface DeleteButtonClickListener {
	fun <T : ResumeEntity> onDeleteButtonClick(item : T)
}

interface ResumeCardClickListener {
	fun onResumeCardClick(resumeId : Long)
}

interface EditButtonClickListener {
	fun <T : ResumeEntity> onEditButtonClicked(item : T)
}