package com.example.moviesapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    fun getMovies(sortOrder: SortOrder): Flow<List<Movie>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated()
            SortOrder.BY_TITLE -> getTasksSortedByName()
        }

    @Query("SELECT * FROM movie_table ORDER BY title ASC")
    fun getTasksSortedByName(): Flow<List<Movie>>

    @Query("SELECT * FROM movie_table ORDER BY releasedDate DESC")
    fun getTasksSortedByDateCreated(): Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)
}