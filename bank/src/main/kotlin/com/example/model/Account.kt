package com.example.model

data class Account(
    var amount: Money,
    val client: Client,
    val isDefault: Boolean
)
