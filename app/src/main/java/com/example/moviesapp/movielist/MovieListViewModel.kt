package com.example.moviesapp.movielist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieDao
import com.example.moviesapp.data.PreferencesManager
import com.example.moviesapp.data.SortOrder
import com.example.moviesapp.repo.MoviesLoadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: MoviesLoadRepository,
    private val dao: MovieDao,
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    private val preferencesFlow = preferencesManager.preferencesFlow

    private val movieFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { ( query, filterPreferences ) ->
        dao.getMovies(filterPreferences.sortOrder)
    }
    val movies = movieFlow.asLiveData()

    fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        Log.i("TAG", "viewmodel loadData: call")
        repository.loadData()
    }

    fun onMovieClick(movie: Movie) = viewModelScope.launch {
        movieEventChannel.send(MovieEvent.NavigateToMovieDetailsScreen(movie))
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    private val movieEventChannel = Channel<MovieEvent>()
    val movieEvent = movieEventChannel.receiveAsFlow()


    sealed class MovieEvent {
        data class NavigateToMovieDetailsScreen(val movie: Movie) : MovieEvent()
    }

}