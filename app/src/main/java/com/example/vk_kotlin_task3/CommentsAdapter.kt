package com.example.vk_kotlin_task3

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class CommentsAdapter(private val items: MutableList<Comment>, private val func: (id: Int?) -> Unit) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_view, null)
        context = parent.context
        return CommentsViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: CommentsViewHolder, position: Int) {
        viewHolder.usernameTV.text = items[position].user
        viewHolder.contentTV.text = items[position].content
        viewHolder.likesCounter.text = items[position].likeNumber.toString()
        val startPadding: Int = context.resources.getDimension(R.dimen.childCommentStartPadding).toInt()
        val endPadding: Int = context.resources.getDimension(R.dimen.childCommentEndPadding).toInt()
        if (items[position].parentId != null) {
            viewHolder.usernameTV.setPadding(startPadding, 0, endPadding, 0)
            viewHolder.contentTV.setPadding(startPadding, 0, endPadding, 0)
            viewHolder.replyHolder.setPadding(startPadding, 0, endPadding, 0)
        }
        if (items[position].is_liked == true) {
            viewHolder.likeButton.backgroundTintList = ColorStateList.valueOf(R.color.like_active)
        } else if (items[position].is_liked == false) {
            viewHolder.likeButton.clearColorFilter()
        }

        viewHolder.replyTV.setOnClickListener {
            func(position)
        }

        viewHolder.likeButton.setOnClickListener{
            val currentItem = items[position]
            if (!currentItem.is_liked) {
                currentItem.likeNumber++
                currentItem.is_liked = true
                notifyItemChanged(position)
            } else {
                currentItem.likeNumber--
                currentItem.is_liked = false
                notifyItemChanged(position)
            }
            viewHolder.likesCounter.text = currentItem.likeNumber.toString()

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTV: TextView
        val contentTV: TextView
        val likesCounter: TextView
        val replyHolder: LinearLayout
        val likeButton: ImageView
        val replyTV: TextView

        init {
            usernameTV = itemView.findViewById(R.id.comment_author)
            contentTV = itemView.findViewById(R.id.comment_text)
            likesCounter = itemView.findViewById(R.id.comment_likes_tv)
            replyHolder = itemView.findViewById(R.id.reply_holder)
            likeButton = itemView.findViewById(R.id.like_button)
            replyTV = itemView.findViewById(R.id.reply_tv)
        }
    }


}