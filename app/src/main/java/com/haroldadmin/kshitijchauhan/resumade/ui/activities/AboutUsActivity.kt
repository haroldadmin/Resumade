package com.haroldadmin.kshitijchauhan.resumade.ui.activities

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.haroldadmin.kshitijchauhan.resumade.R
import kotlinx.android.synthetic.main.activity_about_us.*
import android.support.v4.content.ContextCompat
import android.view.WindowManager


class AboutUsActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_about_us)

		val window = this.window

		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
		window.statusBarColor = ContextCompat.getColor(this, R.color.secondaryColor)

		val text = resources.getString(R.string.aboutUsText)
		aboutLinksTextView.text = text
		appRepoRow.setOnClickListener {
			val url = "https://www.github.com/haroldadmin/resumade"
			val intent = Intent(Intent.ACTION_VIEW)
			intent.data = Uri.parse(url)
			startActivity(intent)
		}

		devProfileRow.setOnClickListener {
			val url = "https://www.github.com/haroldadmin"
			val intent = Intent(Intent.ACTION_VIEW)
			intent.data = Uri.parse(url)
			startActivity(intent)
		}

		standardResumeRow.setOnClickListener {
			val url = "http://www.standardresume.co"
			val intent = Intent(Intent.ACTION_VIEW)
			intent.data = Uri.parse(url)
			startActivity(intent)
		}

		rateAndReviewRow.setOnClickListener {
			val intent = Intent(Intent.ACTION_VIEW)
			intent.data = Uri.parse("market://details?id=com.haroldadmin.kshitijchauhan.resumade")
			startActivity(intent)
		}
	}
}
