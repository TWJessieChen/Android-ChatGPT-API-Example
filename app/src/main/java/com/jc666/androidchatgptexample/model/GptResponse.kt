package com.jc666.androidchatgptexample.model

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class GptResponse (
//    val choices : JsonArray
    val id: String,
    @SerializedName("object")
    val objectText: String,
    val created: Long,
    val choices: List<Choice>,
    val usage: Usage
)