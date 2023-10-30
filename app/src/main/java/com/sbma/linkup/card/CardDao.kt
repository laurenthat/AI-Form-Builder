package com.sbma.linkup.card

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cards: List<Card>)

    @Update
    suspend fun update(item: Card)

    @Upsert
    suspend fun upsert(item: Card)

    @Upsert
    suspend fun upsert(item: List<Card>)

    @Delete
    suspend fun delete(item: Card)

    @Query("DELETE FROM Card WHERE userId = :userId AND id NOT IN (:idList)")
    suspend fun deleteNotInTheListUserItems(userId: UUID, idList: List<UUID>)

    @Query("SELECT * from Card WHERE id = :id")
    fun getItem(id: UUID): Flow<Card?>

    @Query("SELECT * from Card WHERE userId = :userId ORDER BY title DESC")
    fun getUserItems(userId: UUID): Flow<List<Card>>

    @Transaction
    suspend fun syncUserItems(userId: UUID, items: List<Card>) {
        val userItems = items.filter { it.userId == userId }
        insert(userItems)
        deleteNotInTheListUserItems(userId, userItems.map { it.id })
    }
}
