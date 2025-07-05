package com.example.palmscannermachine.repository

import com.example.palmscannermachine.model.Transaction
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

//class FirestoreRepository {
//    private val firestore = FirebaseFirestore.getInstance()
//    private val transactionsCollection = firestore.collection("transactions")
//
//    fun listenToNewTransactions(): Flow<List<Transaction>> = callbackFlow {
//
//        println("Listening to new transaction in repository ...")
//
//        val listener = transactionsCollection
//            .orderBy("createdAt", Query.Direction.DESCENDING)
//            .addSnapshotListener { snapshot, error ->
//
//                println("I think there is an error ...")
//
//                if (error != null) {
//                    println("Ok error: ")
//                    close(error)
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null) {
//                    println("A new snapshot has been found!")
//                    val transactions = snapshot.documents.mapNotNull { doc ->
//                        try {
//                            doc.toObject(Transaction::class.java)?.copy(id = doc.id)
//                        } catch (e: Exception) {
//                            null
//                        }
//                    }
//                    trySend(transactions)
//                }
//            }
//
//        awaitClose { listener.remove() }
//    }
//
//    fun listenToTransactionStatus(transactionId: String): Flow<Transaction?> = callbackFlow {
//        val listener = transactionsCollection.document(transactionId)
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    close(error)
//                    return@addSnapshotListener
//                }
//
//                if (snapshot != null && snapshot.exists()) {
//                    try {
//                        val transaction = snapshot.toObject(Transaction::class.java)?.copy(id = snapshot.id)
//                        trySend(transaction)
//                    } catch (e: Exception) {
//                        trySend(null)
//                    }
//                } else {
//                    trySend(null)
//                }
//            }
//
//        awaitClose { listener.remove() }
//    }
//}