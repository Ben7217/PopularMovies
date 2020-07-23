package com.app.movielist.movieservice

import com.app.movielist.model.Genre
import com.app.movielist.model.Genres
import com.app.movielist.model.Result
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

////https://api.themoviedb.org/3/movie/popular?api_key=12eea07b2f70e0f67e11aed26db663e2&language=en-US&page=1

interface MovieService {
    @GET("/3/movie/popular")
    fun getPopularMovies(@Query("api_key") key: String): Observable<Result>

    @GET("/3/genre/movie/list")
    fun getGenreList(@Query("api_key") key: String): Observable<Genres>
}