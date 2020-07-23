package com.app.movielist.model

/**
 * Model class for all of the objects we will retrieve from the API
 */

data class Result(
    val results: ArrayList<MovieDetails>
)

data class MovieDetails(
    val popularity: String,
    val id: Int,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    val genre_ids: ArrayList<Int>
)

data class Genre (
    val id: Int,
    val name: String
)

data class Genres (
    val genres: ArrayList<Genre>
)