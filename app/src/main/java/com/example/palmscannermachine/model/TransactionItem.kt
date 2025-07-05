package com.example.palmscannermachine.model

data class TransactionItem(
    val id: String = "",
    val name: String = "",
    val notes: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val salesType: String = "",
    val variant: String = ""
)