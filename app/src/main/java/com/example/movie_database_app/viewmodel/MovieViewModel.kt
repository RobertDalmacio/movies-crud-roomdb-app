package com.example.movie_database_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.movie_database_app.data.MovieDatabase
import com.example.movie_database_app.repository.MovieRepository
import com.example.movie_database_app.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieViewModel(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Movie>>
    private val repository: MovieRepository

    init {
        val movieDao = MovieDatabase.getDatabase(application).movieDao()
        repository = MovieRepository(movieDao)
        readAllData = repository.readAllData
    }

    fun addMovie(movie: Movie){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMovie(movie)
        }
    }

    fun updateMovie(movie: Movie){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMovie(movie)
        }
    }

    fun deleteMovie(movie: Movie){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMovie(movie)
        }
    }

    fun deleteAllMovies(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMovies()
        }
    }
}

