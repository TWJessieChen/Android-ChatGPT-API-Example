package com.jc666.androidchatgptexample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.britemed.btecganaar.ConstContent.ConstContent
import com.jc666.androidchatgptexample.R
import com.jc666.androidchatgptexample.database.entity.ContentEntity


class ContentAdapter(val context : Context, private val dataSet : List<ContentEntity>) : RecyclerView.Adapter<ContentAdapter.ViewHolder>() {

    interface DelChatLayoutClick {
        fun onLongClick(view : View, position: Int)
    }
    var delChatLayoutClick : DelChatLayoutClick? = null

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val contentTV : TextView = view.findViewById(R.id.textViewItem)
        val delChatLayout : ConstraintLayout = view.findViewById(R.id.chatGPTLayout)
        val idHolder : TextView = view.findViewById(R.id.textViewId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ConstContent.GPT) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.gpt_content_item, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.user_content_item, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contentTV.text = dataSet[position].content
        holder.idHolder.text = dataSet[position].id.toString()

        holder.delChatLayout.setOnLongClickListener { view ->
            delChatLayoutClick?.onLongClick(view, position)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].gptOrUser == 1) { // Gpt
            ConstContent.GPT
        } else {
            ConstContent.USER
        }
    }

}