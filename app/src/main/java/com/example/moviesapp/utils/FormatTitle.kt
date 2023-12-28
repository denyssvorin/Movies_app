package com.example.moviesapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.moviesapp.data.Movie
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RequiresApi(Build.VERSION_CODES.O)
fun formatTitle(movie: Movie): String {

    return try {
        val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

        val formattedTitle = "${movie.title} (${
            yearFormatter.format(
                LocalDate.from(Instant.ofEpochSecond(movie.releasedDate).atZone(ZoneId.from(ZoneOffset.UTC)))
            )
        })"

        formattedTitle

    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        movie.title
    }
}

