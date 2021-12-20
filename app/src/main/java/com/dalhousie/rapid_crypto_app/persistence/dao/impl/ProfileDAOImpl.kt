package com.dalhousie.rapid_crypto_app.persistence.dao.impl

import com.dalhousie.rapid_crypto_app.models.Profile
import com.dalhousie.rapid_crypto_app.persistence.dao.ProfileDAO
import com.dalhousie.rapid_crypto_app.persistence.datasource.FirebaseUtils
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class ProfileDAOImpl: ProfileDAO {
    override suspend fun getProfile(email: String): Profile {
        val document = FirebaseUtils().fireStoreDatabase.collection("users")
            .document(email)
            .get().await()
        return document.toObject<Profile>()!!
    }

    override suspend fun updateProfile(profile: Profile, email: String): Profile {
        val document = FirebaseUtils().fireStoreDatabase.collection("users")
            .document(email).update(mapOf(
                "name" to profile.name,
                "country" to profile.country,
                "phone" to profile.phone,
                "image" to profile.image
            ))
            .await()

        return getProfile(email)
    }

    override suspend fun createProfile(email: String): Profile{
        val profile = Profile("Canada", email, "", 1000.0)
        FirebaseUtils().fireStoreDatabase.collection("users")
            .document(email).set(profile).await()

        return getProfile(email)
    }
}