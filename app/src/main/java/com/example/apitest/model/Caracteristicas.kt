package com.example.apitest.model

data class Caracteristicas(
    val modelo: String,
    val descricao: String,
    val valor: String,
    //carro
    val rodas:String?,
    //Cadeira
    val material: String?,
    //Casa
    val quartos:String?,
    val banheiros:String?,
    val entrada:String?
)
