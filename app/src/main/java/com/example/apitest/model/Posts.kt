package com.example.apitest.model

data class Posts(
    val code: Int,
    val empresa: String,
    val item: MutableList<Item>
)
