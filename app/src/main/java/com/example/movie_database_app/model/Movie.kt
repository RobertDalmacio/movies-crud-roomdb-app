package com.example.movie_database_app.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movieDb_table")

data class Movie (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val movieTitle: String,
    val studioName: String,
    val criticsRating: Int,
    val moviePoster: Bitmap
): Parcelable


