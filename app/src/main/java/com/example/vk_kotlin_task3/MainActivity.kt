package com.example.vk_kotlin_task3

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<ArticleFragment>(R.id.fragment_container_view, "article")
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.fragments.last() == supportFragmentManager.findFragmentByTag("article")) {
                supportFragmentManager.findFragmentByTag("article")?.view?.findViewById<ConstraintLayout>(R.id.article_cv)?.foreground = null
                supportFragmentManager.findFragmentByTag("article")?.view?.setOnTouchListener(null)
            }
        }
    }

}