package com.app.movielist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.movielist.views.MovieRecyclerView

/**
 * Entry point into the single activity application.
 * Loads a Fragment containing the movie list.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMovieList()

    }

    private fun loadMovieList() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val movieList = MovieRecyclerView()
        transaction.replace(R.id.root, movieList)
        transaction.commit()
    }

}
