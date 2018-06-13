package com.haroldadmin.kshitijchauhan.resuminator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val text = """This project is completely open source.
This app was created with first year engineering students as its target audience.
You should check out Standard Resume if you need something more professional.
"""
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
    }
}
