package com.devfutech.souvenir.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.devfutech.souvenir.data.local.entity.Souvenir
import kotlinx.coroutines.flow.Flow

@Dao
interface SouvenirDao {

    @Query("SELECT * FROM souvenir_table ORDER BY souvenirCode DESC")
    fun getAllPaged(): PagingSource<Int, Souvenir>

    @Query("SELECT * FROM souvenir_table WHERE souvenirName LIKE :key ORDER BY souvenirCode DESC")
    fun getSearch(key: String): PagingSource<Int, Souvenir>

    @Query("SELECT * FROM souvenir_table WHERE souvenirName LIKE :search ORDER BY  souvenirCode DESC")
    fun getAllDataSearch(search: String): Flow<List<Souvenir>>

    @Query("SELECT * FROM souvenir_table WHERE souvenirCategory = :category AND souvenirName LIKE :search ORDER BY souvenirCode DESC")
    fun getAllPagedByCategoryAndSearch(category: String, search: String): Flow<List<Souvenir>>

    @Query("SELECT * FROM souvenir_table GROUP BY souvenirCategory")
    fun getCategory(): Flow<List<Souvenir>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dictionary: List<Souvenir>)

    @Query("DELETE FROM souvenir_table")
    fun deleteAll()

    @Delete
    fun delete(souvenir: Souvenir)
}