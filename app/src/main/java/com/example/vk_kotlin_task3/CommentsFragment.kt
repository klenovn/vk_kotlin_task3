package com.example.vk_kotlin_task3

import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CommentsFragment : Fragment(R.layout.fragment_comments) {

    private lateinit var commentEditText: EditText
    private lateinit var commentPostButton: Button
    private lateinit var commentList: MutableList<Comment>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommentsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var progressBar: ProgressBar
    private lateinit var commentsWhitespaces: View
    private lateinit var commentsContent: ConstraintLayout

    private var totalComments: Int = 0
    private val delayInMs: Long = 2000
    private val commentsPerLoad = 5

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var orientation: Int = resources.configuration.orientation
        totalComments = arguments?.getInt("answersNumber") ?: 0
        commentList = mutableListOf()
        if (savedInstanceState != null) {
            val restoredItems = savedInstanceState.getParcelableArrayList<Comment>("commentList")
            if (restoredItems != null) {
                commentList.clear()
                commentList.addAll(restoredItems)
            }
        }

        adapter = CommentsAdapter(commentList)
        commentsWhitespaces = view.findViewById(R.id.comments_whitespace)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView = view.findViewById(R.id.comments_rv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        progressBar = view.findViewById(R.id.comments_pb)

        commentsContent = view.findViewById(R.id.comments_content)
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            commentsContent.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        }

        val topTV = view.findViewById<TextView>(R.id.comments_tv)
        val numberOfComments = commentList.size
        topTV.text = when (numberOfComments) {
            0, 1 -> "$numberOfComments comment"
            else -> "$numberOfComments comments"
        }

        commentsWhitespaces.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        commentPostButton = view.findViewById(R.id.comment_post_button)
        commentEditText = view.findViewById(R.id.comment_textarea)
        commentPostButton.setOnClickListener {
            val newComment = Comment(
                id = commentList.size,
                user = "admin",
                content = commentEditText.text.toString(),
                likeNumber = 0,
                parentId = null
            )
            commentList.add(newComment)
            adapter.notifyItemInserted(commentList.size)
            commentEditText.text.clear()
        }
//        commentList.addAll((createFakeComments(20)))
        lifecycleScope.launch {
            showProgressBar()
            delay(delayInMs)

            for (i in 0 until totalComments step commentsPerLoad) {
                val comments = withContext(Dispatchers.IO) {
                    createFakeComments(commentsPerLoad)
                }
                if (isActive) {
                    commentList.addAll(comments)
                    updateUI()
                    hideProgressBar()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("commentList", ArrayList(commentList))
    }

    private fun updateUI() {
        val topTV = view?.findViewById<TextView>(R.id.comments_tv)
        val numberOfComments = when (commentList.size) {
            in 0..totalComments -> totalComments
            else -> commentList.size
        }
        topTV?.text = when (numberOfComments) {
            0, 1 -> "$numberOfComments comment"
            else -> "$numberOfComments comments"
        }

        adapter = CommentsAdapter(commentList)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView = requireView().findViewById(R.id.comments_rv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }
}