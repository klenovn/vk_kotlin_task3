package com.example.vk_kotlin_task3

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlin.random.Random

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private val random = Random
    private val maxNumber: Int = 100
    private val answersNumber = random.nextInt(maxNumber)
    private var isScrollLocked: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isScrollLocked) {
            view.setOnTouchListener { _, _ -> true }
        } else {
            view.setOnTouchListener(null)
        }

        if (savedInstanceState != null) {
            isScrollLocked = savedInstanceState.getBoolean("isScrollLocked", false)
        }


        var articleText: String = "The story takes place in the Taishō era Japan, where a secret organization, known as the Demon Slayer Corps, has waged a longtime war against demons for centuries. These demons are former humans who possess supernatural abilities such as super strength, rapid regeneration, and unique powers referred to as \"Blood Demon Art\". Demons can only be killed if they are decapitated with weapons crafted from a rare alloy called Nichirin, injected with a poison extracted from wisteria flowers, or exposed to direct sunlight.\n In contrast, the Demon Slayers are entirely human but employ specialized elemental breathing techniques known as \"Breathing Styles\". These techniques grant them superhuman strength and increased resilience that enable them to fight demons effectively. The most formidable Demon Slayers hold the \"Hashira\" title and gain this rank through killing a member of the Twelve Kizuki, an organization comprising the twelve strongest demons of their species, or killing fifty demons through multiple advancements in their rank. Aided by Tamayo's poison, the Corps succeed into kill Nakime and unleash a desperate battle of attrition as the remaining members fight against Muzan until the morning sun can kill him.\n Muzan is left helpless against the sun but Gyomei, Obanai, and Mitsuri succumb to their injuries. Tanjiro is fatally wounded while he delivers the final blow and Muzan forcefully gives him his remaining blood. Tanjiro transforms into the ultimate being in Muzan's last-ditch effort to have his species survive. Tanjiro begins to attack the Corps but through their efforts and Nezuko, who has been fully restored to her human self, he is turned back into a human. In the aftermath of the battle, the Corps are disbanded, with Giyu, Tengen, and Sanemi as the only Hashira survivors."
        articleText = articleText.replace("\n", "\n\n")

        val article1 = Article(
            heading = "Demon Slayer: Kimetsu no Yaiba",
            text = articleText,
            image = ResourcesCompat.getDrawable(resources, R.drawable.demon_slayer_image, null)
        )

        val articleIV: ImageView? =  view.findViewById(R.id.article_iv)
        val articleImage: Drawable? = article1.image
        articleIV?.apply {
            setImageDrawable(articleImage)
        }

        val articleHeadingTV: TextView? = view.findViewById(R.id.article_heading_tv)
        if (articleHeadingTV != null) {
            articleHeadingTV.text = article1.heading
        }

        val articleContentTV: TextView? = view.findViewById(R.id.article_content_tv)
        articleContentTV?.movementMethod = ScrollingMovementMethod()
        if (articleContentTV != null) {
            articleContentTV.text = article1.text
        }

        val supportFragmentManager = activity?.supportFragmentManager
        val commentsButton: Button = view.findViewById(R.id.article_comment_button)
        var cbText: String = "${answersNumber.toString()} comments"
        commentsButton.text = cbText
        commentsButton.stateListAnimator = null
        commentsButton.setOnClickListener {
            val commentsFragment = CommentsFragment()
            arguments = Bundle()
            arguments?.putInt("answersNumber", answersNumber)
            commentsFragment.arguments = arguments
            val articleCV = supportFragmentManager?.findFragmentByTag("article")?.view?.findViewById<ConstraintLayout>(R.id.article_cv)
            articleCV?.foreground = ColorDrawable(R.color.black_85)
            supportFragmentManager?.beginTransaction()?.apply {
                add(R.id.fragment_container_view, commentsFragment, "comments")
                addToBackStack("comments")
                commit()
            }
            this.view?.setOnTouchListener { _, _ -> true }
            isScrollLocked = true
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isScrollLocked", isScrollLocked)
    }

}