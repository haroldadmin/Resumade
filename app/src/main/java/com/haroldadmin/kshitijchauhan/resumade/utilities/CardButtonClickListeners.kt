package com.haroldadmin.kshitijchauhan.resumade.utilities

interface SaveButtonClickListener {
	fun <ResumeEntity> onSaveButtonClick(item : ResumeEntity)
}

interface DeleteButtonClickListener {
	fun <ResumeEntity> onDeleteButtonClick(item : ResumeEntity)
}

interface ResumeCardClickListener {
	fun onResumeCardClick(resumeId : Long)
}

interface PrintButtonClickListener {
	fun onPrintButtonClick(resumeId : Long)
}