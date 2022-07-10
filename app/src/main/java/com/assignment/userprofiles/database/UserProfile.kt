package com.assignment.userprofiles.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "user_profiles")
data class UserProfile(

    @PrimaryKey(autoGenerate = true) val id: Int,
    @NonNull @ColumnInfo(defaultValue = "no name") val name: String,
    @NonNull @ColumnInfo(defaultValue = "no phone") val phone: String,
    @NonNull @ColumnInfo(defaultValue = "no email") val email: String,
    @NonNull @ColumnInfo(defaultValue = "no imageUrl")val imageUrl: String

): Serializable
