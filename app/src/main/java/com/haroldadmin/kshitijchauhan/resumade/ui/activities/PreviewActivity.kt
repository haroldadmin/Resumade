package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.webkit.WebView
import com.haroldadmin.kshitijchauhan.resumade.R

class PreviewActivity : AppCompatActivity() {

	private val EXTRA_HTML: String = "html"
	private lateinit var toolbar: Toolbar
	private lateinit var webView: WebView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_preview)
		webView = findViewById(R.id.previewActivityWebView)
		toolbar = findViewById(R.id.previewActivityToolbar)

		setSupportActionBar(toolbar)
		supportActionBar?.apply {
			title = "Preview"
			setDisplayHomeAsUpEnabled(true)
		}

		val receivedIntent = intent
		val html = receivedIntent.getStringExtra(EXTRA_HTML)

		webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
	}

}
