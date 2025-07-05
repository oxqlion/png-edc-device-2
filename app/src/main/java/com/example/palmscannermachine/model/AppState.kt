package com.example.palmscannermachine.model

sealed class AppState {
    object WaitingForTransaction : AppState()
    data class NewDataDetected(val transaction: Transaction) : AppState()
    data class PaymentSuccessful(val transaction: Transaction) : AppState()
    data class ShowSummary(val transaction: Transaction) : AppState()
}