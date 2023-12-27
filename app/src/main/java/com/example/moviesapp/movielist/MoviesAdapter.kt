package com.example.moviesapp.movielist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.data.Movie
import com.example.moviesapp.databinding.MovieListItemBinding

class MoviesAdapter(
    private val listener: OnMovieClickListener,
    private val context: Context
) :
    ListAdapter<Movie, MoviesAdapter.MovieViewHolder>(DiffMovieCallback()) {

    inner class MovieViewHolder(private val binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        listener.onMovieClick(item)
                    }
                }
            }
        }

        fun bind(movie: Movie) {
            binding.apply {
                Glide.with(context)
                    .load(movie.photo)
                    .centerCrop()
                    .into(imageViewMovieImage)
                textViewMovieTitle.text = movie.title
                textViewMovieDuration.text = movie.duration
                textViewMovieGenre.text = movie.genre

                textViewIsInWatchlist.isVisible = movie.isInWatchList
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding =
            MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    interface OnMovieClickListener {
        fun onMovieClick(movie: Movie)
    }

    class DiffMovieCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.title == newItem.title


        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem == newItem
    }
}