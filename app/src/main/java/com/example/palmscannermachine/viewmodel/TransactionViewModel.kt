package com.example.palmscannermachine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.util.Log
import com.example.palmscannermachine.model.AppState
import com.example.palmscannermachine.model.Transaction

class TransactionViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val collectionRef = firestore.collection("transactions")

    private val _appState = MutableStateFlow<AppState>(AppState.WaitingForTransaction)
    val appState: StateFlow<AppState> = _appState.asStateFlow()

    private var currentTransactionId: String? = null

    init {
        startListening()
    }

    private fun startListening() {
        collectionRef.addSnapshotListener { snapshots, e ->
            if (e != null) {
                Log.e("TransactionViewModel", "Listen failed", e)
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        // New document added
                        val newDocument = dc.document
                        // Use manual parsing instead of toObject()
                        val transaction = parseTransaction(newDocument.data, newDocument.id)

                        Log.d("TransactionViewModel", "New transaction added: ${transaction.id}")

                        // Update state to show new data detected
                        _appState.value = AppState.NewDataDetected(transaction)
                        currentTransactionId = transaction.id

                        // Check if status is already success
                        if (transaction.status.equals("success", ignoreCase = true)) {
                            handlePaymentSuccess(transaction)
                        }
                    }
                    DocumentChange.Type.MODIFIED -> {
                        // Existing document modified
                        val modifiedDocument = dc.document
                        // Use manual parsing instead of toObject()
                        val transaction = parseTransaction(modifiedDocument.data, modifiedDocument.id)

                        Log.d("TransactionViewModel", "Transaction modified: ${transaction.id}, Status: ${transaction.status}")

                        // Only process if this is the current transaction we're watching
                        if (transaction.id == currentTransactionId && transaction.status.equals("success", ignoreCase = true)) {
                            handlePaymentSuccess(transaction)
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        // Document removed
                        val removedDocument = dc.document
                        Log.d("TransactionViewModel", "Transaction removed: ${removedDocument.id}")
                    }
                }
            }
        }
    }

    private fun parseTransaction(data: Map<String, Any>, documentId: String): Transaction {
        return Transaction(
            id = documentId,
            status = data["status"] as? String ?: "",
            // Ignore the items field that's causing issues
        )
    }

    private fun handlePaymentSuccess(transaction: Transaction) {
        viewModelScope.launch {
            // Show payment successful message
            _appState.value = AppState.PaymentSuccessful(transaction)

            // After 2 seconds, show summary
            delay(2000)
            _appState.value = AppState.ShowSummary(transaction)
        }
    }

    fun resetApp() {
        currentTransactionId = null
        _appState.value = AppState.WaitingForTransaction
    }
}