package com.example.moviesapp.moviedetails

import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.moviesapp.R
import com.example.moviesapp.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.time.temporal.ChronoField

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MovieDetailsViewModel>()

    private val args: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            args.apply {
                imageViewMovieImage.setImageBitmap(movie.photo)
                textViewMovieTitle.text = movie.title
                textViewRating.text = movie.rating.toString()
                textViewDescription.text = movie.description
                textViewGenre.text = movie.genre

                val releasedDateString = DateTimeFormatterBuilder()
                    .appendPattern("yyyy, d ")
                    .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL_STANDALONE)
                    .toFormatter()
                    .withZone(ZoneId.from(ZoneOffset.UTC))
                    .format(java.time.Instant.ofEpochSecond(movie.releasedDate))
                textViewReleasedDate.text = releasedDateString ?: "released date"

                buttonWatchTrailer.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.trailerLink))
                    startActivity(intent)
                }

                viewModel.isInWatchList = movie.isInWatchList

                buttonToggleAddToWatchlist.isChecked = viewModel.isInWatchList

                if (!buttonToggleAddToWatchlist.isChecked) {
                    buttonToggleAddToWatchlist.setButtonIcon(
                        Icon.createWithResource(
                            requireContext(),
                            R.drawable.ic_add_24
                        )
                    )
                } else {
                    buttonToggleAddToWatchlist.setButtonIcon(null)
                }

                buttonToggleAddToWatchlist.setOnCheckedChangeListener { button, isChecked ->
                    if (!isChecked) {
                        button.setButtonIcon(
                            Icon.createWithResource(
                                requireContext(),
                                R.drawable.ic_add_24
                            )
                        )
                    } else {
                        buttonToggleAddToWatchlist.setButtonIcon(null)
                    }
                }

                buttonToggleAddToWatchlist.setOnClickListener {
                    viewModel.toggleWatchlist(movie)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}