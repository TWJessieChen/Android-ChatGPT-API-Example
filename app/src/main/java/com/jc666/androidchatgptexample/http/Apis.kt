package com.jc666.androidchatgptexample.http

import com.google.gson.JsonObject
import com.jc666.androidchatgptexample.model.GptResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Apis {
    @Headers(
        "Content-Type:application/json",
        "Authorization:Bearer <ChatGPT-API-Key write here>")
    @POST("v1/chat/completions")
    suspend fun postRequest(
        @Body json : JsonObject
    ) : GptResponse

}