package com.sbma.linkup.user

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: User)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItemList(item: List<User>)

    @Update
    suspend fun update(item: User)

    @Upsert
    suspend fun upsert(item: User)

    @Delete
    suspend fun delete(item: User)

    @Query("SELECT * from User WHERE id = :id")
    fun getItem(id: UUID): Flow<User?>

    @Query("SELECT * from User ORDER BY name DESC")
    fun getAllItems(): Flow<List<User>>
}
