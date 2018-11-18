package com.haroldadmin.kshitijchauhan.resumade.utilities

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.repository.database.ResumeEntity


fun View.showKeyboard(context: Context?) {
	this.requestFocus()
	val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun AppCompatActivity.hideKeyboard() {
	val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	inputManager.hideSoftInputFromWindow(
			if (this.currentFocus == null) {
				null
			} else {
				this.currentFocus.windowToken
			}, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun <T : ResumeEntity> List<T>.isAnyItemUnsaved() : Boolean {
	for (entity in this) {
		if (!entity.saved) {
			return true
		}
	}
	return false
}

fun <T : ResumeEntity> List<T>.areAllItemsSaved() : Boolean = !this.isAnyItemUnsaved()

fun WebView.createPrintJob(context: Context) {
	val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
	val printAdapter = this.createPrintDocumentAdapter("Resumade document")
	val printJob = printManager.print("Resumade Job", printAdapter, PrintAttributes.Builder().build())
}

fun View.gone() {
	this.visibility = View.GONE
}

fun View.visible() {
	this.visibility = View.VISIBLE
}

fun View.invisible() {
	this.visibility = View.INVISIBLE
}