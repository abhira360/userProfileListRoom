package com.assignment.userprofiles.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface UserProfileDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun submitUser(userProfile: UserProfile)

    @Query("select * from user_profiles order by id desc")
    fun getAll(): Flow<List<UserProfile>>

    @Delete
    suspend fun deleteUser(userProfile: UserProfile)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(userProfile: UserProfile)


}