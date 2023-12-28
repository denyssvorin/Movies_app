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
            SortOrder.BY_DATE -> getMoviesSortedByReleaseDate()
            SortOrder.BY_TITLE -> getMoviesSortedByTitle()
        }

    @Query("SELECT * FROM movie_table ORDER BY title ASC")
    fun getMoviesSortedByTitle(): Flow<List<Movie>>

    @Query("SELECT * FROM movie_table ORDER BY releasedDate DESC")
    fun getMoviesSortedByReleaseDate(): Flow<List<Movie>>

    @Query("SELECT * FROM movie_table WHERE title LIKE '%' || :searchQuery || '%'")
    suspend fun getMovieByName(searchQuery: String): Movie

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)
}