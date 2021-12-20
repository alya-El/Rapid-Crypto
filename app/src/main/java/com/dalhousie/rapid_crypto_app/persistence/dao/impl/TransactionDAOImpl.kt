package com.dalhousie.rapid_crypto_app.persistence.dao.impl

import android.content.ContentValues
import android.util.Log
import com.dalhousie.rapid_crypto_app.models.Transaction
import com.dalhousie.rapid_crypto_app.models.TransactionDetails
import com.dalhousie.rapid_crypto_app.persistence.dao.TransactionDAO
import com.dalhousie.rapid_crypto_app.persistence.datasource.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TransactionDAOImpl : TransactionDAO {
    private val firebaseUser = FirebaseAuth.getInstance().currentUser

    override suspend fun getTransactions(): TransactionDetails {
        val document = FirebaseUtils().fireStoreDatabase.collection("transactions")
            .document(firebaseUser?.email.toString())
            .get().await()

        val transactions = ArrayList<Transaction>()
        document.data?.forEach { (key, value) ->
            if ("transactions" == key) {
                value as List<*>

                // in our database, we have list of maps. So, we iterate on the list we get from firestore and pass it to TransactionDetail
                for( valueMap in value)
                {
                    valueMap as HashMap<String, Any>

                    Log.w(ContentValues.TAG, "${valueMap["price"]} transactionDAO value")

                    transactions.add(
                        Transaction(
                            valueMap["coinName"].toString(),
                            valueMap["coinImage"].toString(),
                            valueMap["coinSymbol"].toString(),
                            valueMap["quantity"] as Long,
                            valueMap["total"] as Long,
                            valueMap["price"] as Long,
                            valueMap["transactionType"].toString()
                        )
                    )
                }

            }
        }

        return TransactionDetails(transactions)

    }

    override suspend fun addTransaction(transaction: Transaction): TransactionDetails {

        val transactionDetails = getTransactions()

        val trans = hashMapOf(
            "coinName" to transaction.coinName,
            "coinImage" to transaction.coinImage,
            "coinSymbol" to transaction.coinSymbol,
            "price" to transaction.price,
            "quantity" to transaction.quantity,
            "total" to transaction.total,
            "transactionType" to transaction.transactionType

        )

        if (transactionDetails.transactions.isNotEmpty()) {
            FirebaseUtils().fireStoreDatabase.collection("transactions")
                .document(firebaseUser?.email.toString())
                .update("transactions", FieldValue.arrayUnion(trans))
                .await()
        } else {
            FirebaseUtils().fireStoreDatabase.collection("transactions")
                .document(firebaseUser?.email.toString())
                .set(mapOf("transactions" to Collections.singletonList(trans)))
                .await()
        }

        return getTransactions()
    }

}