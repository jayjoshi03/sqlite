package com.example.usermini.model

data class User(
    var id: Long,
    var name: String,
    var password: String,
    var email: String,
    var gender: String,
    var birthday: String
)
