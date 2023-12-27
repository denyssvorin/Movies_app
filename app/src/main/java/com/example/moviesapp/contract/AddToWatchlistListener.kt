package com.example.moviesapp.contract

import com.example.moviesapp.data.Movie

interface AddToWatchlistListener {
    fun addToWatchlist(movie: Movie, isFavorite: Boolean)
}