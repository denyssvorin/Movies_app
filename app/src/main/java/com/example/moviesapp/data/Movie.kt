package com.example.moviesapp.data

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity("movie_table")
@Parcelize
data class Movie(
    @PrimaryKey(autoGenerate = false) val title: String,
    val description: String,
    val rating: Double,
    val duration: String,
    val genre: String,
    val releasedDate: Long,
    val trailerLink: String,

    val isInWatchList: Boolean = false
) : Parcelable {

    @IgnoredOnParcel
    @Ignore
    var photo: Bitmap? = null

}