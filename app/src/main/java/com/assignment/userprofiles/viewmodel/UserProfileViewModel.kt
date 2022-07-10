package com.assignment.userprofiles.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.userprofiles.database.UserProfile
import com.assignment.userprofiles.database.UserProfileDatabase
import com.assignment.userprofiles.repository.UserProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application): AndroidViewModel(application) {

    private val repository : UserProfileRepository
    init {
        val userProfileDAO = UserProfileDatabase.getDataBase(application).userProfileDao()
        repository = UserProfileRepository(userProfileDAO)
    }

    fun getAllUser(): Flow<List<UserProfile>> = repository.getAllUser()


    fun submitUser(userProfile: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) { repository.submitUser(userProfile) }
    }

    fun updateUser(userProfile: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) { repository.updateUser(userProfile)}
    }

    fun deleteUser(userProfile: UserProfile) {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteUser(userProfile) }
    }




}