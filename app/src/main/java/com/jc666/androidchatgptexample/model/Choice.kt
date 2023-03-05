package com.jc666.androidchatgptexample.model

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)
