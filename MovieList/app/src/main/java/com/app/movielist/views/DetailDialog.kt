package com.app.movielist.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.app.movielist.R
import com.app.movielist.model.Genres
import com.app.movielist.model.MovieDetails
import com.app.movielist.movieapi.ApiContract
import com.squareup.picasso.Picasso

/**
 * This is a dialog fragment that shows all of the Movie details.
 */
class DetailDialog : DialogFragment() {

    private lateinit var selectedMovie: MovieDetails
    private lateinit var movieTitle: TextView
    private lateinit var moviePoster: ImageView
    private lateinit var closeDialog: ImageView
    private lateinit var movieOverview: TextView
    private lateinit var movieGenre: TextView
    private lateinit var genreList: Genres
    private lateinit var genreMap: HashMap<Int, String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.details_layout, container, false)
        movieTitle = root.findViewById(R.id.details_title)
        moviePoster = root.findViewById(R.id.details_poster)
        movieOverview = root.findViewById(R.id.details_overview)
        movieGenre = root.findViewById(R.id.details_genres)
        closeDialog = root.findViewById(R.id.dismiss_dialog)
        closeDialog.setOnClickListener { this.dismiss() }

        setUpDialogViews(selectedMovie)

        this.isCancelable = true

        return root
    }

    private fun setUpDialogViews(selectedMovie: MovieDetails) {
        movieTitle.text = selectedMovie.title
        Picasso.get().load(ApiContract.IMAGE_PREFIX + selectedMovie.poster_path).into(moviePoster)
        movieOverview.text = selectedMovie.overview
        movieGenre.text = buildGenreString(selectedMovie.genre_ids)
    }

    fun setMovie(result: MovieDetails?) {
        if (result != null) {
            selectedMovie = result
        }
    }

    fun setGenreList(genreList: Genres) {
        this.genreList = genreList
        initGenreMap(genreList)
    }

    private fun initGenreMap(genreList: Genres) {
        genreMap = HashMap()
       for (genre in genreList.genres) {
           genreMap[genre.id] = genre.name
       }
    }

    /**
     * Because the popular movies response's genres comes in an arraylist of integers,
     * I had to match up the selected movies genre ids with the overall genre id list which contains
     * key-value paired objects, the key being the id, and the value being the genre itself.
     */
    private fun buildGenreString(selectedMovieGenreIds: ArrayList<Int>) : String {
        var result = ""
        for ((i, genreId) in selectedMovieGenreIds.withIndex()) {
            result += genreMap[genreId]
            if (selectedMovieGenreIds.size > 0
                && i < selectedMovieGenreIds.size - 1) {
                result += ", "
            }
        }

        return result
    }
}