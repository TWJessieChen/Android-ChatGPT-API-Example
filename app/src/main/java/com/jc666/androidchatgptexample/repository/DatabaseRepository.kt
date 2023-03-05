package com.jc666.androidchatgptexample.repository

import com.jc666.androidchatgptexample.ChatGPTApplication
import com.jc666.androidchatgptexample.database.ChatDatabase
import com.jc666.androidchatgptexample.database.entity.ContentEntity


class DatabaseRepository {

    private val context = ChatGPTApplication.appContext!!
    private val database = ChatDatabase.getDatabase(context)

    fun getContentData() = database.contentDAO().getContentData()

    fun insertContent(content : String, gptOrUser : Int, originalJson : String) = database.contentDAO().insertContent(
        ContentEntity(0, content, gptOrUser, originalJson)
    )

    fun deleteSelectedContent(id : Int) = database.contentDAO().deleteSelectedContent(id)

}