package com.example.workify.dataClasses

data class Worker(
    var name: String = "",
    var email: String = "",
    var district: String = "",
    var password: String = "",
    var description: String = "",
    var categories : Array<Category>? = null
)
