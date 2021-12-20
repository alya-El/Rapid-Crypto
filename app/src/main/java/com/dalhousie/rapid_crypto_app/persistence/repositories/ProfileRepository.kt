package com.dalhousie.rapid_crypto_app.persistence.repositories

import androidx.annotation.WorkerThread
import com.dalhousie.rapid_crypto_app.models.Profile
import com.dalhousie.rapid_crypto_app.persistence.dao.ProfileDAO

class ProfileRepository(private val profileDAO: ProfileDAO) {

    @WorkerThread
    suspend fun getProfile(email: String): Profile {
        return profileDAO.getProfile(email)
    }

    @WorkerThread
    suspend fun updateProfile(profile: Profile, email: String): Profile {
        return profileDAO.updateProfile(profile, email)
    }

    @WorkerThread
    suspend fun createProfile(email: String): Profile {
        return profileDAO.createProfile(email)
    }
}