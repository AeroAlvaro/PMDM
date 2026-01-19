package com.example.nombresapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NameDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(nameEntity: NameEntity)

    @Query("SELECT * from names ORDER BY name ASC")
    fun getNamesAsc(): Flow<List<NameEntity>>

    @Query("SELECT * from names ORDER BY name DESC")
    fun getNamesDesc(): Flow<List<NameEntity>>
}