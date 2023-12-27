package com.example.moviesapp.repo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieDao
import com.example.moviesapp.data.TempData
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MoviesLoadRepository @Inject constructor(
    private val dao: MovieDao
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadData() {
        Log.i("TAG", "repository loadData: called")

        dataList.forEach { data: TempData ->

            val releasedDate = convertStringToTimestamp3(data.releasedDate)
            val movie = Movie(
                title = data.title,
                description = data.description,
                rating = data.rating,
                duration = data.duration,
                genre = data.genre,
                releasedDate = releasedDate,
                trailerLink = data.trailerLink
            )
            Log.i("TAG", "repository loadData: insert: ${data.title}")
            dao.insert(movie)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertStringToTimestamp3(dateString: String): Long {
        val patterns = listOf(
            "d MMMM yyyy",
            "dd MMMM yyyy",
            "d MMM yyyy",
            "dd MMM yyyy"
        )

        for (pattern in patterns) {
            try {
                val formatter = DateTimeFormatter.ofPattern(pattern)
                val date = LocalDate.parse(dateString, formatter)
                val unix = date.atStartOfDay(ZoneId.systemDefault()).toInstant().epochSecond
                return unix
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        throw IllegalArgumentException("Unknown date format: $dateString")
    }


    companion object {
        private val dataList = listOf(
            TempData(
                "Tenet",
                "Armed with only one word, Tenet, and fighting for the survival of the entire world, a Protagonist journeys through a twilight world of international espionage on a mission that will unfold in something beyond real time.",
                7.8,
                "2h 30 min",
                "Action, Sci-Fi",
                "3 September 2020",
                "https://www.youtube.com/watch?v=LdOM0x0XDMo"
            ),
            TempData(
                "Spider-Man: Into the Spider-Verse",
                "Teen Miles Morales becomes the Spider-Man of his universe, and must join with five spider-powered individuals from other dimensions to stop a threat for all realities.",
                8.4,
                "1h 57min",
                "Action, Animation, Adventure",
                "14 December 2018",
                "https://www.youtube.com/watch?v=tg52up16eq0"
            ),
            TempData(
                "Knives Out",
                "A detective investigates the death of a patriarch of an eccentric, combative family.",
                7.9,
                "2h 10min",
                "Comedy, Crime, Drama",
                "27 November 2019",
                "https://www.youtube.com/watch?v=qGqiHJTsRkQ"
            ),
            TempData(
                "Guardians of the Galaxy",
                "A group of intergalactic criminals must pull together to stop a fanatical warrior with plans to purge the universe.",
                8.0,
                "2h 1min",
                "Action, Adventure, Comedy",
                "1 August 2014",
                "https://www.youtube.com/watch?v=d96cjJhvlMA"
            ),
            TempData(
                "Avengers: Age of Ultron",
                "When Tony Stark and Bruce Banner try to jump-start a dormant peacekeeping program called Ultron, things go horribly wrong and it's up to Earth's mightiest heroes to stop the villainous Ultron from enacting his terrible plan.",
                7.3,
                "2h 21min",
                "Action, Adventure, Sci-Fi",
                "1 May 2015",
                "https://www.youtube.com/watch?v=tmeOjFno6Do"
            )
        )
    }
}