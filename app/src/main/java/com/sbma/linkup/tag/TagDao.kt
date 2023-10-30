package com.sbma.linkup.tag

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
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Tag)

    @Update
    suspend fun update(item: Tag)

    @Upsert
    suspend fun upsert(item: Tag)

    @Delete
    suspend fun delete(item: Tag)

    @Query("SELECT * from Tag WHERE id = :id")
    fun getItem(id: UUID): Flow<Tag?>

    @Query("SELECT * from Tag")
    fun getAllItems(): Flow<List<Tag>>
}
