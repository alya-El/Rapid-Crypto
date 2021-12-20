package com.dalhousie.rapid_crypto_app.persistence.dao

import com.dalhousie.rapid_crypto_app.models.Profile

interface ProfileDAO {
    suspend fun getProfile(email: String): Profile

    suspend fun updateProfile(profile: Profile, email: String): Profile

    suspend fun createProfile(email: String): Profile
}