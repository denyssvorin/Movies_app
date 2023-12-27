package com.example.moviesapp.moviedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val dao: MovieDao
) : ViewModel() {

    var isInWatchList by Delegates.notNull<Boolean>()

    fun toggleWatchlist(movie: Movie) {
        if (isInWatchList) {
            addToWatchlist(movie, false)
        } else {
            addToWatchlist(movie, true)
        }
        isInWatchList = !isInWatchList
    }


    private fun addToWatchlist(movie: Movie, isFavorite: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(movie.copy(isInWatchList = isFavorite))
        }
}