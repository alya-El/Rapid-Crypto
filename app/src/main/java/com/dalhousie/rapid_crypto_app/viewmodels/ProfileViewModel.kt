package com.dalhousie.rapid_crypto_app.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dalhousie.rapid_crypto_app.models.Profile
import com.dalhousie.rapid_crypto_app.persistence.dao.impl.ProfileDAOImpl
import com.dalhousie.rapid_crypto_app.persistence.repositories.ProfileRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private val profileRepository: ProfileRepository
    private val _profileLiveData: MutableLiveData<Profile> = MutableLiveData()

    val profileLiveData: LiveData<Profile>
        get() = _profileLiveData

    init {
        val profileDAO = ProfileDAOImpl()
        profileRepository = ProfileRepository(profileDAO)
    }

    fun getUserEmail(): String {
        val user = Firebase.auth.currentUser
        return user?.email?.toString() ?: "Error. No email"
    }

    fun loadProfile() {
        viewModelScope.launch {
            val returnedProfile = profileRepository.getProfile(getUserEmail())
            _profileLiveData.value = returnedProfile
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            val returnedProfile = profileRepository.updateProfile(profile, getUserEmail())
            _profileLiveData.value = returnedProfile
        }
    }
}