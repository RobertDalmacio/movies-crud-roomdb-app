package com.example.movie_database_app.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.movie_database_app.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movieDb_table ORDER BY id DESC")
    fun readAllData(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(movie: Movie)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Delete
    suspend fun deleteMovie(movie: Movie)

    @Query("DELETE FROM movieDb_table")
    suspend fun deleteAllMovies()
}