package com.example.apitest.model

data class Item(
    val codigo: Int,
    val tipo: String,
    val url: String,
    var caracteristicas: Caracteristicas
)
