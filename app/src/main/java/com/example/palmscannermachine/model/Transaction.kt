package com.example.palmscannermachine.model

//data class Transaction(
//    val id: String = "",
//    val createdAt: com.google.firebase.Timestamp? = null,
//    val customerName: String = "",
//    val items: List<Map<String, Any>> = emptyList(),
//    val orderType: String = "",
//    val pajakRestoran: Double = 0.0,
//    val ppn: Double = 0.0,
//    val status: String = "",
//    val subtotal: Double = 0.0,
//    val tipServer: Double = 0.0,
//    val total: Double = 0.0
//)

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

data class Transaction(
    val id: String = "",
    val createdAt: Timestamp? = null,
    val customerName: String = "",
    val items: List<Map<String, Any>> = emptyList(),
    val orderType: String = "",
    val pajakRestoran: Double = 0.0,
    val ppn: Double = 0.0,
    val status: String = "",
    val subtotal: Double = 0.0,
    val tipServer: Double = 0.0,
    val total: Double = 0.0
) {
    companion object {
        fun fromDocumentSnapshot(document: DocumentSnapshot): Transaction {
            val data = document.data ?: return Transaction(id = document.id)

            return Transaction(
                id = document.id,
                createdAt = data["createdAt"] as? Timestamp,
                customerName = data["customerName"] as? String ?: "",
                items = parseItems(data["items"]),
                orderType = data["orderType"] as? String ?: "",
                pajakRestoran = (data["pajakRestoran"] as? Number)?.toDouble() ?: 0.0,
                ppn = (data["ppn"] as? Number)?.toDouble() ?: 0.0,
                status = data["status"] as? String ?: "",
                subtotal = (data["subtotal"] as? Number)?.toDouble() ?: 0.0,
                tipServer = (data["tipServer"] as? Number)?.toDouble() ?: 0.0,
                total = (data["total"] as? Number)?.toDouble() ?: 0.0
            )
        }

        private fun parseItems(itemsObj: Any?): List<Map<String, Any>> {
            return when (itemsObj) {
                is List<*> -> {
                    itemsObj.mapNotNull { item ->
                        when (item) {
                            is Map<*, *> -> {
                                // If it's already a Map, use it
                                item as? Map<String, Any>
                            }
                            is DocumentReference -> {
                                // If it's a DocumentReference, convert to a simple map
                                mapOf(
                                    "documentPath" to item.path,
                                    "documentId" to item.id
                                )
                            }
                            else -> null
                        }
                    }
                }
                else -> emptyList()
            }
        }
    }
}