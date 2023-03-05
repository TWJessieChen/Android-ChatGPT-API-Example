package com.nohjunh.test.repository

import com.google.gson.JsonObject
import com.jc666.androidchatgptexample.http.Apis
import com.jc666.androidchatgptexample.http.RetrofitInstance

class NetWorkRepository {

    private val chatGPTClient = RetrofitInstance.getInstance().create(Apis::class.java)

    suspend fun postResponse(jsonData : JsonObject) = chatGPTClient.postRequest(jsonData)
}