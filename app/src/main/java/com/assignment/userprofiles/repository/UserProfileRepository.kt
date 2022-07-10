package com.assignment.userprofiles.repository

import com.assignment.userprofiles.database.UserProfile
import com.assignment.userprofiles.database.UserProfileDAO
import kotlinx.coroutines.flow.Flow


class UserProfileRepository(private val userProfileDAO: UserProfileDAO) {


    fun submitUser(userProfile: UserProfile) =  userProfileDAO.submitUser(userProfile)

    fun updateUser(userProfile: UserProfile) = userProfileDAO.updateUser(userProfile)

    suspend fun deleteUser(userProfile: UserProfile) = userProfileDAO.deleteUser(userProfile)

    fun getAllUser(): Flow<List<UserProfile>> =  userProfileDAO.getAll()


}